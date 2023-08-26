package com.indexer.timeouttask.screen.pomodoroscreen.domain

class PomodoroScreenUseCase {
  fun updatePomodoroNumber(
    state: PomodoroScreenState,
    value: Int
  ): PomodoroScreenState {
    return state.copy(pomodoroDurationInMinutes = value)
  }

  fun updatePomodoroTitle(
    state: PomodoroScreenState,
    value: String
  ): PomodoroScreenState {
    return state.copy(pomodoroTitle = value)
  }

  fun increasePomodoroNumber(
    state: PomodoroScreenState,
    value: Int
  ): PomodoroScreenState {
    return state.copy(pomodoroDurationInMinutes = value.plus(1))
  }

  fun decreasePomodoroNumber(
    state: PomodoroScreenState,
    value: Int
  ): PomodoroScreenState {
    return if (value > 1) {
      state.copy(pomodoroDurationInMinutes = value.minus(1))
    } else {
      state.copy(pomodoroDurationInMinutes = 1)
    }
  }
}