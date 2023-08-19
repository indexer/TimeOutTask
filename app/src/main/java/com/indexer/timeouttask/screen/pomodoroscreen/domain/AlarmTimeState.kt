package com.indexer.timeouttask.screen.pomodoroscreen.domain

data class AlarmTimerState(var time: Int =0, var isRunning: Boolean = false,
  var isPaused: Boolean = false, var isCompleted : Boolean = false, var isReset : Boolean = false)
