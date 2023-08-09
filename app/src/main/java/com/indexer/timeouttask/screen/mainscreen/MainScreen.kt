package com.indexer.timeouttask.screen.mainscreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.indexer.timeouttask.screen.mainscreen.addscreen.AddScreen
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenUseCase
import com.indexer.timeouttask.screen.mainscreen.addscreen.viewmodel.AddScreenViewModel
import com.indexer.timeouttask.ui.theme.TimeOutTaskTheme

@Preview(showBackground = true)
@Composable
fun MainScreen() {
  TimeOutTaskTheme {
    val useCase = AddScreenUseCase()
    val viewModelFactory = remember { AddScreenViewModelFactory(useCase) }
    val viewModel: AddScreenViewModel = viewModel(factory = viewModelFactory)
    val processIntentWithCurrentValue = viewModel.provideProcessIntent()
    val state = viewModel.addScreenStateState.collectAsState()
    AddScreen(processIntentWithCurrentValue, state.value.pomodoroNumber)
  }
}

class AddScreenViewModelFactory(private val useCase: AddScreenUseCase) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(AddScreenViewModel::class.java)) {
      return AddScreenViewModel(useCase) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}




