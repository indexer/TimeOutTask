package com.indexer.timeouttask.screen.pomodoroscreen.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
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

  private val completedTask = MutableStateFlow<PomodoroTask?>(null)

  private var currentTimer: CountDownTimer? = null

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

    // Reset progress and restart timer for the swapped items
    if (pomodoroListScreenState.value[fromIndex].alarmTimerState.isActive && pomodoroListScreenState
        .value[toIndex].progress != 100f
    ) {
      resetProgressAndRestartTimer(fromIndex)
    }
    if (pomodoroListScreenState.value[toIndex].alarmTimerState.isActive && pomodoroListScreenState
        .value[toIndex].progress != 100f
    ) {
      resetProgressAndRestartTimer(toIndex)
    }

    // Swap the items in the list
    val updatedList = pomodoroListScreenState.value.toMutableList()
    val swapList = updatedList.swap(fromIndex, toIndex)
    pomodoroListScreenState.value = swapList
  }

  private fun resetProgressAndRestartTimer(itemIndex: Int) {
    pomodoroListScreenState.value[itemIndex].progress = 0f
    val initialTime = pomodoroListScreenState.value[itemIndex].alarmTimerState.elapsedTime.toInt()
    setTimePomodoro(initialTime, itemIndex)
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

  private fun updateElapsedTimeForAlarm(
    isTimerRunning: Boolean,
    currentElapsedTime: Long,
    taskIndex: Int
  ) {
    timeState.value.isActive = isTimerRunning
    timeState.value.elapsedTime = currentElapsedTime

    val updatedTaskList = pomodoroListScreenState.value.toMutableList()
    val initialTaskTime = updatedTaskList[taskIndex].alarmTimerState.elapsedTime
    // Timer not completed, update progress
    val remainingTaskTime = initialTaskTime - currentElapsedTime
    val taskProgress = calculateProgress(remainingTaskTime, initialTaskTime)
    updatedTaskList[taskIndex] = updatedTaskList[taskIndex].copy(progress = taskProgress)

    if (taskProgress == 100f) {
      completedTask.value = updatedTaskList[taskIndex]
      taskCompleted.value = pomodoroListScreenState.value.any { it.progress != 100f }
    }
    pomodoroListScreenState.value = updatedTaskList
  }

  fun onDismiss() {
    val updatedTaskList = pomodoroListScreenState.value.toMutableList()
    val taskIndex = updatedTaskList.indexOf(completedTask.value)
    val lastIndex = updatedTaskList.withIndex()
      .lastOrNull { it.index != taskIndex && it.value.progress != 100f }?.index
    if (lastIndex != null) {
      val completedTask = updatedTaskList.removeAt(taskIndex)
      updatedTaskList.add(lastIndex, completedTask)
      pomodoroListScreenState.value[0].alarmTimerState.isActive = true
      startTimer(pomodoroListScreenState.value[0].alarmTimerState.elapsedTime, 0)
    }
    pomodoroListScreenState.value = updatedTaskList
    taskCompleted.value = false
  }

  private fun calculateProgress(
    remainingTime: Long,
    initialTime: Long
  ): Float {
    val progress = ((initialTime - remainingTime) / initialTime.toFloat()) * 100
    return progress.coerceIn(0f, 100f)
  }

  private fun setTimePomodoro(
    pomodoroNumber: Int,
    itemIndex: Int
  ) {
    timeState.value.elapsedTime = 1 * 60 * 1000L // 2 minutes in milliseconds
    if (pomodoroListScreenState.value[0].alarmTimerState.isActive && itemIndex == 0) {
      startTimer(timeState.value.elapsedTime, itemIndex)
    }
  }

  private fun convertPomodoroToSeconds(pomodoroNumber: Int): Int {
    return 25 * pomodoroNumber * 60
  }

   fun getPomodoroDescription(pomodoroNumber: Int): String {
    val totalMinutes = pomodoroNumber * 25
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
      is PomodoroScreenIntent.IncrementPomodoro -> {
        pomodoroScreenState.value =
          useCase.increasePomodoroNumber(
            pomodoroScreenStateState.value, pomodoroScreenStateState.value.pomodoroDurationInMinutes
          )
      }

      is PomodoroScreenIntent.DecrementPomodoro -> {
        pomodoroScreenState.value =
          useCase.decreasePomodoroNumber(
            pomodoroScreenStateState.value, pomodoroScreenStateState.value.pomodoroDurationInMinutes
          )
      }

      is PomodoroScreenIntent.UpdatePomodoroNumber -> {
        pomodoroScreenState.value =
          useCase.updatePomodoroNumber(pomodoroScreenStateState.value, intent.value)
      }

      is PomodoroScreenIntent.MakeIt -> {
        val pomodoroTask = createPomodoroTask(pomodoroScreenState.value)
        addPomodoroToList(pomodoroTask)
        setTimePomodoro(
          pomodoroScreenState.value.pomodoroDurationInMinutes,
          pomodoroListScreenState.value.size - 1
        )
        pomodoroScreenState.value =
          pomodoroScreenState.value.copy(pomodoroDurationInMinutes = 1, pomodoroTitle = "")
      }

      is PomodoroScreenIntent.UpdatePomodoroTitle -> {
        pomodoroScreenState.value =
          useCase.updatePomodoroTitle(pomodoroScreenStateState.value, intent.value)
      }
    }
  }

  private fun createPomodoroTask(pomodoroScreenState: PomodoroScreenState): PomodoroTask {
    return PomodoroTask(
      pomodoroScreenState.pomodoroTitle,
      getPomodoroDescription(pomodoroScreenState.pomodoroDurationInMinutes),
      AlarmTimerState(
        pomodoroScreenState.pomodoroDurationInMinutes.toLong() * 60 * 1000,
        pomodoroListScreenState.value.isEmpty()
      ), 0f
    )
  }
}
