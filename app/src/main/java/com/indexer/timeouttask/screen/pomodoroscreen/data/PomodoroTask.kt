package com.indexer.timeouttask.screen.pomodoroscreen.data

import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState

data class PomodoroTask(
  var title: String,
  var description: String,
  val alarmTimerState: AlarmTimerState,
  var progress: Float,
  var backgroundColor: Int,
  var date: String
)