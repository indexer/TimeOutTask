package com.indexer.timeouttask.screen.pomodoroscreen.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.indexer.timeouttask.R
import com.indexer.timeouttask.screen.mainscreen.PomodoroTask
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenUseCase
import com.indexer.timeouttask.screen.pomodoroscreen.swap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PomodoroScreenViewModel(private val useCase: PomodoroScreenUseCase) : ViewModel() {

  private val pomodoroScreenState = MutableStateFlow(PomodoroScreenState())
  val pomodoroScreenStateState: StateFlow<PomodoroScreenState> = pomodoroScreenState

  private val timeState = MutableStateFlow(AlarmTimerState())

  val pomodoroListScreenState = MutableStateFlow(emptyList<PomodoroTask>())
  val pomodoroList: StateFlow<List<PomodoroTask>> = pomodoroListScreenState

  private val taskCompleted = MutableStateFlow(false)
  val showCongratulationsScreen: StateFlow<Boolean> = taskCompleted

  val taskAdded = MutableStateFlow(false)
  val showAddScreen: StateFlow<Boolean> = taskAdded

  private val completedTask = MutableStateFlow<PomodoroTask?>(null)

  private var currentTimer: CountDownTimer? = null

  private val pomodoroDurationMinutes = 25
  private val durationInMilliseconds =pomodoroDurationMinutes * 60 * 1000L


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
      startTimer(updatedPomodoroList[0].alarmTimerState.elapsedTime, 0)
    }
  }

  private fun resetProgressAndRestartTimer(itemIndex: Int) {
    if (pomodoroListScreenState.value[itemIndex].progress != 100f)
    pomodoroListScreenState.value[itemIndex].progress = 0f
    val initialTimeMillis =
      pomodoroListScreenState.value[itemIndex].alarmTimerState.elapsedTime.toInt()
    setTimePomodoro(initialTimeMillis, itemIndex)
  }

  private fun startTimer(
    durationInMillis: Long,
    itemIndex: Int
  ) {
    currentTimer?.cancel()
    currentTimer = object : CountDownTimer(durationInMillis, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        val elapsedTime = durationInMillis - millisUntilFinished
        updateElapsedTimeForAlarm(true, elapsedTime, itemIndex)
      }

      override fun onFinish() {
        updateElapsedTimeForAlarm(false, durationInMillis, itemIndex)
      }
    }
    currentTimer?.start()
  }

  private fun onDismiss() {
    val currentTaskList = pomodoroListScreenState.value
    val completedTaskIndex = currentTaskList.indexOfFirst { it.progress == 100f }

    if (completedTaskIndex != -1) {
      val updatedTaskList = useCase.dismissCompletedTask(currentTaskList, completedTaskIndex)
      pomodoroListScreenState.value = updatedTaskList

      // Start the timer for the next task if available
      val nextTaskIndex = useCase.findIndexOfNextIncompleteTask(updatedTaskList, 0)
      if (nextTaskIndex != -1) {
        startTimer(updatedTaskList[nextTaskIndex].alarmTimerState.elapsedTime, nextTaskIndex)
      }
      taskCompleted.value = false
    }
  }

  private fun setTimePomodoro(
    pomodoroDurationMinutes: Int,
    itemIndex: Int
  ) {
    timeState.value.elapsedTime =
      1 * 60 * 1000L// Convert minutes to milliseconds
    if (pomodoroListScreenState.value.isNotEmpty() &&
      pomodoroListScreenState.value[0].alarmTimerState.isActive && itemIndex == 0
    ) {
      startTimer(timeState.value.elapsedTime, itemIndex)
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
      "Total Time: $hours hr $minutes min"
    } else {
      "Total Time: $minutes min"
    }
    return timeText
  }

  private fun addPomodoroToList(newPomodoro: PomodoroTask) {
    val updatedPomodoroList = pomodoroListScreenState.value.toMutableList()
    if (updatedPomodoroList.isNotEmpty() && updatedPomodoroList[0].progress == 100f) {
      newPomodoro.alarmTimerState.isActive = true
      updatedPomodoroList.add(0, newPomodoro)
      startTimer(newPomodoro.alarmTimerState.elapsedTime, 0)
    } else {
      if (updatedPomodoroList.isNotEmpty() && updatedPomodoroList[updatedPomodoroList.size - 1].progress == 100f) {
        updatedPomodoroList.add(updatedPomodoroList.size - 1, newPomodoro)
      } else {
        updatedPomodoroList.add(newPomodoro)
      }
    }
    pomodoroListScreenState.value = updatedPomodoroList
  }

  private fun processPomodoroIntent(intent: PomodoroScreenIntent) {
    when (intent) {
      is PomodoroScreenIntent.IncrementPomodoro -> handleIncrementPomodoro()
      is PomodoroScreenIntent.DecrementPomodoro -> handleDecrementPomodoro()
      is PomodoroScreenIntent.UpdatePomodoroNumber -> handleUpdatePomodoroNumber(intent.value)
      is PomodoroScreenIntent.MakeIt -> handleMakeIt()
      is PomodoroScreenIntent.UpdatePomodoroTitle -> handleUpdatePomodoroTitle(intent.value)
      is PomodoroScreenIntent.DismissCompletedTask -> handleDismissCompletedTask()
    }
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
      completedTask.value = updatedTaskList[taskIndex]
      taskCompleted.value = pomodoroListScreenState.value.any { it.progress != 100f }
    }
    pomodoroListScreenState.value = updatedTaskList
  }

  private fun createPomodoroTask(pomodoroScreenState: PomodoroScreenState): PomodoroTask {
    return PomodoroTask(
      pomodoroScreenState.pomodoroTitle,
      getPomodoroDescription(pomodoroScreenState.pomodoroDurationInMinutes),
      AlarmTimerState(
        1 * 60 * 1000L,
        /*convertPomodoroToMilliseconds(pomodoroScreenState.pomodoroDurationInMinutes)*/
        pomodoroListScreenState.value.isEmpty()
      ), 0f
    )
  }
}

