package com.indexer.timeouttask.screen.mainscreen.addscreen.viewmodel

import androidx.lifecycle.ViewModel
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenIntent
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenState
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddScreenViewModel(private val useCase: AddScreenUseCase) : ViewModel() {

  private var mAddScreenState: MutableStateFlow<AddScreenState> =
    MutableStateFlow(AddScreenState(pomodoroNumber = 1))
  val addScreenStateState: StateFlow<AddScreenState> = mAddScreenState

  fun provideProcessIntent(): (AddScreenIntent) -> Unit {
    return { intent -> processIntent(intent) }
  }

  private fun processIntent(intent: AddScreenIntent) {
    when (intent) {
      is AddScreenIntent.IncrementPomodoro -> mAddScreenState.value =
        useCase.increasePomodoroNumber(
          addScreenStateState.value, addScreenStateState.value.pomodoroNumber)

      is AddScreenIntent.DecrementPomodoro -> mAddScreenState.value =
        useCase.decreasePomodoroNumber(
          addScreenStateState.value, addScreenStateState.value.pomodoroNumber)

      is AddScreenIntent.UpdatePomodoroNumber -> mAddScreenState.value =
        useCase.updatePomodoroNumber(
          addScreenStateState.value, intent.value)

      is AddScreenIntent.MakeIt -> {
        mAddScreenState.value = mAddScreenState.value
      }
    }
  }
}
