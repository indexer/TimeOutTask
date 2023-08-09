package com.indexer.timeouttask.screen.mainscreen.addscreen.domain

class AddScreenUseCase {
  fun updatePomodoroNumber(
    state: AddScreenState,
    value: Int
  ): AddScreenState {
    return state.copy(pomodoroNumber = value)
  }

  fun increasePomodoroNumber(
    state: AddScreenState,
    value: Int
  ): AddScreenState {
    return state.copy(pomodoroNumber = value.plus(1))
  }

  fun decreasePomodoroNumber(
    state: AddScreenState,
    value: Int
  ): AddScreenState {
    return if (value > 1) {
      state.copy(pomodoroNumber = value.minus(1))
    } else {
      state.copy(pomodoroNumber = 1)
    }
  }
}