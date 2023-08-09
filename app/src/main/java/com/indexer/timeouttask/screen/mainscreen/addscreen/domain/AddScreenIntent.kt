package com.indexer.timeouttask.screen.mainscreen.addscreen.domain

sealed class AddScreenIntent {
  object IncrementPomodoro : AddScreenIntent()
  object DecrementPomodoro : AddScreenIntent()
  data class UpdatePomodoroNumber(val value: Int) : AddScreenIntent()
  object MakeIt : AddScreenIntent()
}