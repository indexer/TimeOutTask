package com.indexer.timeouttask.screen.pomodoroscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PomodoroScreenViewModel(private val useCase: PomodoroScreenUseCase) : ViewModel() {

  private var mPomodoroScreenState: MutableStateFlow<PomodoroScreenState> =
    MutableStateFlow(PomodoroScreenState(pomodoroNumber = 1, ""))
  val pomodoroScreenStateState: StateFlow<PomodoroScreenState> = mPomodoroScreenState
  private val mAlarmState = MutableStateFlow(
    AlarmTimerState(time = 1, isRunning = false, isPaused = false, isCompleted = false,false))

  fun provideProcessIntent(): (PomodoroScreenIntent) -> Unit {
    return { intent -> processPomodoroIntent(intent) }
  }

  private fun processAlarmIntent(intent: AlarmIntent) {
    when (intent) {
      is AlarmIntent.StartTimer -> startTimer()
      is AlarmIntent.ResetTimer -> resetTimer()
      is AlarmIntent.TogglePause -> togglePause()
    }
  }

  private fun startTimer() {
    when {
      !mAlarmState.value.isRunning -> {
        viewModelScope.launch {
          mAlarmState.value = mAlarmState.value.copy(isRunning = true, isCompleted = false)
          repeat(mAlarmState.value.time) {
            if (!mAlarmState.value.isPaused) {
              delay(1000)
              mAlarmState.value = mAlarmState.value.copy(time = mAlarmState.value.time - 1)
            }
          }
          mAlarmState.value =
            mAlarmState.value.copy(isRunning = false, isPaused = false, isCompleted = true)
        }
      }
    }
  }

  private fun resetTimer() {
    if (!mAlarmState.value.isRunning) {
      mAlarmState.value = mAlarmState.value.copy(time = 0)
    }
  }

  private fun togglePause() {
    if (mAlarmState.value.isRunning) {
      mAlarmState.value = mAlarmState.value.copy(isPaused = !mAlarmState.value.isPaused)
    }
  }

  private fun setTimePomodoro(pomodoroNumber: Int) {
    mAlarmState.value.time = convertPomodoroToSeconds(pomodoroNumber)
  }

  private fun convertPomodoroToSeconds(pomodoroNumber: Int): Int {
    return 25 * pomodoroNumber * 60
  }

  private fun processPomodoroIntent(intent: PomodoroScreenIntent) {
    when (intent) {
      is PomodoroScreenIntent.IncrementPomodoro -> {
        mPomodoroScreenState.value =
          useCase.increasePomodoroNumber(
            pomodoroScreenStateState.value, pomodoroScreenStateState.value.pomodoroNumber
          )
      }
      is PomodoroScreenIntent.DecrementPomodoro -> {
        mPomodoroScreenState.value =
          useCase.decreasePomodoroNumber(
            pomodoroScreenStateState.value, pomodoroScreenStateState.value.pomodoroNumber
          )
      }
      is PomodoroScreenIntent.UpdatePomodoroNumber -> {
        mPomodoroScreenState.value =
          useCase.updatePomodoroNumber(pomodoroScreenStateState.value, intent.value)
      }
      is PomodoroScreenIntent.MakeIt -> {
        mPomodoroScreenState.value = mPomodoroScreenState.value
        setTimePomodoro(mPomodoroScreenState.value.pomodoroNumber)
        processAlarmIntent(AlarmIntent.StartTimer)
      }
      is PomodoroScreenIntent.UpdatePomodoroTitle -> {
        mPomodoroScreenState.value =
          useCase.updatePomodoroTitle(pomodoroScreenStateState.value, intent.value)
      }
    }
  }
}
