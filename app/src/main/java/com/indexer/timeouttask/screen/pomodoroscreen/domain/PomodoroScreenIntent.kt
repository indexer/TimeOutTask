package com.indexer.timeouttask.screen.pomodoroscreen.domain

import com.indexer.timeouttask.screen.mainscreen.PomodoroTask

sealed class PomodoroScreenIntent {
  object IncrementPomodoro : PomodoroScreenIntent()
  object DecrementPomodoro : PomodoroScreenIntent()
  data class UpdatePomodoroNumber(val value: Int) : PomodoroScreenIntent()
  data class UpdatePomodoroTitle(val value: String) : PomodoroScreenIntent()
  object DismissCompletedTask : PomodoroScreenIntent()

  object MakeIt : PomodoroScreenIntent()
}