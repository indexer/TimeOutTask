package com.indexer.timeouttask.screen.pomodoroscreen.domain

data class AlarmTimerState(var time: Int, var isRunning: Boolean,
  var isPaused: Boolean, var isCompleted : Boolean, var isReset : Boolean)
