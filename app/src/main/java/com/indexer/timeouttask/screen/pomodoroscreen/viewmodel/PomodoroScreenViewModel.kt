package com.indexer.timeouttask.screen.pomodoroscreen.viewmodel

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

  private val pomodoroScreenState = MutableStateFlow(PomodoroScreenState(pomodoroNumber = 1, ""))
  val pomodoroScreenStateState: StateFlow<PomodoroScreenState> = pomodoroScreenState

  private val alarmState = MutableStateFlow(
    AlarmTimerState(time = 1, isRunning = false, isPaused = false, isCompleted = false, false)
  )

  private val pomodoroListScreenState = MutableStateFlow(emptyList<PomodoroTask>())
  val pomodoroList: StateFlow<List<PomodoroTask>> = pomodoroListScreenState

  fun provideProcessIntent(): (PomodoroScreenIntent) -> Unit {
    return { intent -> processPomodoroIntent(intent) }
  }

  fun getSwapFunction(): (Int, Int) -> Unit {
    return { index1, index2 -> swapPomodoroItems(index1, index2) }
  }

  private fun swapPomodoroItems(
    fromIndex: Int,
    toIndex: Int
  ) {
    pomodoroListScreenState.value = pomodoroListScreenState.value.swap(fromIndex, toIndex)
    if (toIndex == 0) {
      stopTimerRunningExceptFirstPosition()
    }
  }

  private fun stopTimerRunningExceptFirstPosition() {
    pomodoroListScreenState.value.forEachIndexed { index, pomodoroTask ->
      pomodoroTask.alarmTimerState.isRunning = !pomodoroTask.alarmTimerState.isRunning && index == 0
    }
  }

  private fun setTimePomodoro(pomodoroNumber: Int) {
    alarmState.value.time = convertPomodoroToSeconds(pomodoroNumber)
  }

  private fun convertPomodoroToSeconds(pomodoroNumber: Int): Int {
    return 25 * pomodoroNumber * 60
  }

  private fun pomodoroDescriptionText(pomodoroNumber: Int): String {
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
    pomodoroListScreenState.value = pomodoroListScreenState.value + newPomodoro
  }

  private fun processPomodoroIntent(intent: PomodoroScreenIntent) {
    when (intent) {
      is PomodoroScreenIntent.IncrementPomodoro -> {
        pomodoroScreenState.value =
          useCase.increasePomodoroNumber(
            pomodoroScreenStateState.value, pomodoroScreenStateState.value.pomodoroNumber
          )
      }

      is PomodoroScreenIntent.DecrementPomodoro -> {
        pomodoroScreenState.value =
          useCase.decreasePomodoroNumber(
            pomodoroScreenStateState.value, pomodoroScreenStateState.value.pomodoroNumber
          )
      }

      is PomodoroScreenIntent.UpdatePomodoroNumber -> {
        pomodoroScreenState.value =
          useCase.updatePomodoroNumber(pomodoroScreenStateState.value, intent.value)
      }

      is PomodoroScreenIntent.MakeIt -> {
        setTimePomodoro(pomodoroScreenState.value.pomodoroNumber)
        val pomodoroTask = pomodoroTask(pomodoroScreenState.value)
        addPomodoroToList(pomodoroTask)
        pomodoroScreenState.value = pomodoroScreenState.value.copy(pomodoroNumber = 1, pomodoroTitle = "")
      }

      is PomodoroScreenIntent.UpdatePomodoroTitle -> {
        pomodoroScreenState.value =
          useCase.updatePomodoroTitle(pomodoroScreenStateState.value, intent.value)
      }
    }
  }

  private fun pomodoroTask(pomodoroScreenState: PomodoroScreenState): PomodoroTask {
    return PomodoroTask(
      pomodoroScreenState.pomodoroTitle,
      pomodoroDescriptionText(pomodoroScreenState.pomodoroNumber),
      AlarmTimerState(pomodoroScreenState.pomodoroNumber, pomodoroListScreenState.value.isEmpty())
    )
  }
}
