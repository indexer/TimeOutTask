package com.indexer.timeouttask.screen.pomodoroscreen.domain

sealed class PomodoroScreenIntent {
  object IncrementPomodoro : PomodoroScreenIntent()
  object DecrementPomodoro : PomodoroScreenIntent()
  data class UpdatePomodoroNumber(val value: Int) : PomodoroScreenIntent()
  data class UpdatePomodoroTitle(val value: String) : PomodoroScreenIntent()
  object MakeIt : PomodoroScreenIntent()
}