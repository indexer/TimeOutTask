package com.indexer.timeouttask.screen.pomodoroscreen.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.indexer.timeouttask.screen.pomodoroscreen.data.PomodoroTask
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenUseCase
import com.indexer.timeouttask.screen.pomodoroscreen.swap
import com.indexer.timeouttask.ui.utils.ColorGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class PomodoroScreenViewModel(private val useCase: PomodoroScreenUseCase) : ViewModel() {

  private val pomodoroScreenState = MutableStateFlow(PomodoroScreenState())
  val pomodoroScreenStateState: StateFlow<PomodoroScreenState> = pomodoroScreenState

  val timeState = MutableStateFlow(AlarmTimerState())

  val pomodoroListScreenState = MutableStateFlow(emptyList<PomodoroTask>())
  val pomodoroList: StateFlow<List<PomodoroTask>> = pomodoroListScreenState

  private var taskCompleted = MutableStateFlow(false)
  val showCongratulationsScreen: StateFlow<Boolean> = taskCompleted

  val taskAdded = MutableStateFlow(false)
  val showAddScreen: StateFlow<Boolean> = taskAdded

  private val completedTask = MutableStateFlow<PomodoroTask?>(null)
  private var currentTimer: CountDownTimer? = null

  private val pomodoroDurationMinutes = 1
  private val durationInMilliseconds = pomodoroDurationMinutes * 60 * 1000L
  private val colorGenerator = ColorGenerator()

  init {
    setTimePomodoro(pomodoroDurationMinutes, 0)
  }

  fun provideProcessIntent(): (PomodoroScreenIntent) -> Unit {
    return { intent -> processPomodoroIntent(intent) }
  }

  fun getSwapFunction(): (Int, Int) -> Unit {
    return { index1, index2 ->
      swapPomodoroItems(index1, index2)
    }
  }

  private fun swapPomodoroItems(
    fromIndex: Int,
    toIndex: Int
  ) {
    val updatedPomodoroList = pomodoroListScreenState.value.toMutableList()
    val swappedList = updatedPomodoroList.swap(fromIndex, toIndex)
    // Reset the timer for the swapped items
    resetProgressAndRestartTimer(fromIndex)
    resetProgressAndRestartTimer(toIndex)
    pomodoroListScreenState.value = swappedList
    if (toIndex == 0) {
      currentTimer?.cancel()
      startTimerWithDuration(updatedPomodoroList[0].alarmTimerState.elapsedTime, 0)
    }
  }

  private fun resetProgressAndRestartTimer(itemIndex: Int) {
    if (pomodoroListScreenState.value[itemIndex].progress != 100f) {
      pomodoroListScreenState.value[itemIndex].progress = 0f
      val initialTimeMillis =
        pomodoroListScreenState.value[itemIndex].alarmTimerState.elapsedTime.toInt()
      setTimePomodoro(initialTimeMillis, itemIndex)
    }
  }

  private fun startTimerForShortBreak() {
    currentTimer?.cancel()
    val durationInMillis = 10 * 60 * 1000L
    startTimerWithDuration(durationInMillis, 0)
  }

  private fun startTimerWithDuration(
    durationInMillis: Long,
    itemIndex: Int
  ) {
    currentTimer?.cancel()
    currentTimer = object : CountDownTimer(durationInMillis, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        val elapsedTime = durationInMillis - millisUntilFinished
        val remainingTaskTime = durationInMillis - elapsedTime
        timeState.value.remainingTime = formatTime(totalTimeMillis = remainingTaskTime)
        updateElapsedTimeForAlarm(true, elapsedTime, itemIndex)
      }

      override fun onFinish() {
        updateElapsedTimeForAlarm(false, durationInMillis, itemIndex)
      }
    }
    currentTimer?.start()
  }

  private fun onDismiss() {
    val pomodoroTasks = pomodoroListScreenState.value
    val completedTaskIndex = pomodoroTasks.indexOfFirst { it.progress == 100f }

    if (completedTaskIndex != -1) {
      val updatedTasks = useCase.dismissCompletedTask(pomodoroTasks, completedTaskIndex)
      pomodoroListScreenState.value = updatedTasks
      // Start the timer for the next incomplete task if available
      val nextIncompleteTaskIndex = useCase.findIndexOfNextIncompleteTask(updatedTasks, 0)
      if (nextIncompleteTaskIndex != -1) {
        startTimerWithDuration(
          updatedTasks[nextIncompleteTaskIndex].alarmTimerState.elapsedTime,
          nextIncompleteTaskIndex
        )
      }
      taskCompleted.value = false
    }
  }


  private fun setTimePomodoro(
    pomodoroDurationMinutes: Int,
    itemIndex: Int
  ) {
    timeState.value.elapsedTime = pomodoroDurationMinutes * durationInMilliseconds
    // 1 * 60 * 1000L// Convert minutes to milliseconds
    when {
      pomodoroListScreenState.value.isNotEmpty() &&
        pomodoroListScreenState.value[0].alarmTimerState.isActive && itemIndex == 0 -> {
        startTimerWithDuration(timeState.value.elapsedTime, itemIndex)
      }
    }
  }

  private fun convertPomodoroToMilliseconds(pomodoroDurationMinutes: Int): Long {
    return pomodoroDurationMinutes * 25 * 60 * 1000L // Convert minutes to milliseconds
  }

  fun getPomodoroDescription(pomodoroDurationMinutes: Int): String {
    val totalMinutes = pomodoroDurationMinutes * 25
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    val timeText = if (hours > 0) {
      "$hours hr $minutes min"
    } else {
      "$minutes min"
    }
    return timeText
  }

  private fun addPomodoroToList(newTask: PomodoroTask) {
    val currentTasks = pomodoroListScreenState.value.toMutableList()
    when {
      currentTasks.isNotEmpty() && currentTasks[0].progress == 100f -> {
        newTask.alarmTimerState.isActive = true
        currentTasks.add(0, newTask)
        startTimerWithDuration(newTask.alarmTimerState.elapsedTime, 0)
      }
      else -> {
        if (currentTasks.isNotEmpty() && currentTasks.last().progress == 100f) {
          currentTasks.add(currentTasks.size - 1, newTask)
        } else {
          currentTasks.add(newTask)
        }
      }
    }
    pomodoroListScreenState.value = currentTasks
  }


  private fun processPomodoroIntent(intent: PomodoroScreenIntent) {
    when (intent) {
      is PomodoroScreenIntent.IncrementPomodoro -> handleIncrementPomodoro()
      is PomodoroScreenIntent.DecrementPomodoro -> handleDecrementPomodoro()
      is PomodoroScreenIntent.UpdatePomodoroNumber -> handleUpdatePomodoroNumber(intent.value)
      is PomodoroScreenIntent.MakeIt -> handleMakeIt()
      is PomodoroScreenIntent.UpdatePomodoroTitle -> handleUpdatePomodoroTitle(intent.value)
      is PomodoroScreenIntent.DismissCompletedTask -> handleDismissCompletedTask()
      is PomodoroScreenIntent.ShortBreak -> {
        setShortBreakTime()
        startTimerForShortBreak()
      }
    }
  }

  private fun setShortBreakTime() {
    pomodoroList.value.find { it.alarmTimerState.isActive }
      ?.alarmTimerState?.elapsedTime = 10 * 60 * 1000L
  }

  private fun handleIncrementPomodoro() {
    val updatedScreenState = useCase.increasePomodoroNumber(
      pomodoroScreenStateState.value,
      pomodoroScreenStateState.value.pomodoroDurationInMinutes
    )
    pomodoroScreenState.value = updatedScreenState
  }

  private fun handleDecrementPomodoro() {
    val updatedScreenState = useCase.decreasePomodoroNumber(
      pomodoroScreenStateState.value,
      pomodoroScreenStateState.value.pomodoroDurationInMinutes
    )
    pomodoroScreenState.value = updatedScreenState
  }

  private fun handleUpdatePomodoroNumber(newPomodoroDuration: Int) {
    val updatedScreenState = useCase.updatePomodoroNumber(
      pomodoroScreenStateState.value,
      newPomodoroDuration
    )
    pomodoroScreenState.value = updatedScreenState
  }

  private fun handleMakeIt() {
    taskAdded.value = false
    val pomodoroTask = createPomodoroTask(pomodoroScreenState.value)
    addPomodoroToList(pomodoroTask)
    setTimePomodoro(
      pomodoroScreenState.value.pomodoroDurationInMinutes, pomodoroListScreenState.value.size - 1
    )
    pomodoroScreenState.value =
      pomodoroScreenState.value.copy(pomodoroDurationInMinutes = 1, pomodoroTitle = "")
  }

  private fun handleUpdatePomodoroTitle(newTitle: String) {
    val updatedScreenState = useCase.updatePomodoroTitle(pomodoroScreenStateState.value, newTitle)
    pomodoroScreenState.value = updatedScreenState
  }

  private fun handleDismissCompletedTask() {
    onDismiss()
  }

  private fun updateElapsedTimeForAlarm(
    isTimerRunning: Boolean,
    currentElapsedTime: Long,
    taskIndex: Int
  ) {

    timeState.value.isActive = isTimerRunning
    timeState.value.elapsedTime = currentElapsedTime

    val updatedTaskList = pomodoroListScreenState.value.toMutableList()
    val initialTaskTime = updatedTaskList[taskIndex].alarmTimerState.elapsedTime
    val remainingTaskTime = initialTaskTime - currentElapsedTime

    val taskProgress = useCase.calculateProgress(remainingTaskTime, initialTaskTime)
    updatedTaskList[taskIndex] = updatedTaskList[taskIndex].copy(progress = taskProgress)
    if (taskProgress == 100f) {
      if (showCongratulationsScreen.value) {
        taskCompleted.value = false
      } else {
        updatedTaskList[taskIndex].date = getCurrentDateTime()
        completedTask.value = updatedTaskList[taskIndex]
        taskCompleted.value = pomodoroListScreenState.value.any { it.progress != 100f }
      }
    }
    pomodoroListScreenState.value = updatedTaskList
  }

  private fun formatTime(totalTimeMillis: Long): String {
    val totalHours = TimeUnit.MILLISECONDS.toHours(totalTimeMillis)
    val remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeMillis) % 60
    val remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeMillis) % 60
    return String.format("%02d:%02d:%02d", totalHours, remainingMinutes, remainingSeconds)
  }


   fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
  }

  private fun createPomodoroTask(pomodoroScreenState: PomodoroScreenState): PomodoroTask {
    return PomodoroTask(
      pomodoroScreenState.pomodoroTitle,
      getPomodoroDescription(pomodoroScreenState.pomodoroDurationInMinutes),
      AlarmTimerState(
        60000L,
        //convertPomodoroToMilliseconds(pomodoroScreenState.pomodoroDurationInMinutes),
        pomodoroListScreenState.value.isEmpty(), "",
      ), 0f, colorGenerator.getRandomColor(), getCurrentDateTime()
    )
  }
}

