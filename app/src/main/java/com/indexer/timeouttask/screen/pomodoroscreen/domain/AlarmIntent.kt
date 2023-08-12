package com.indexer.timeouttask.screen.pomodoroscreen.domain

sealed class AlarmIntent {
  object StartTimer : AlarmIntent()
  object ResetTimer : AlarmIntent()
  object TogglePause : AlarmIntent()
}