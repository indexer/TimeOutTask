package com.indexer.timeouttask.screen.mainscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroAddScreen
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroListScreen
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.viewmodel.PomodoroScreenViewModel
import com.indexer.timeouttask.ui.theme.TimeOutTaskTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PomodoroScreen() {
  TimeOutTaskTheme {
    val viewModel: PomodoroScreenViewModel = koinViewModel()
    val processIntentWithCurrentValue = viewModel.provideProcessIntent()
    val onMoveItem = viewModel.getSwapFunction()

    val state = viewModel.pomodoroScreenStateState.collectAsState()
    val pomodoroList = viewModel.pomodoroList.collectAsState()

    PomodoroAddScreen(
      processIntentWithCurrentValue, state.value.pomodoroDurationInMinutes, state.value.pomodoroTitle
    )
    PomodoroList(list = pomodoroList.value.toMutableList(), onMoveItem)
  }
}

data class PomodoroTask(
  var title: String,
  var description: String,
  val alarmTimerState: AlarmTimerState,
  var progress: Float
)

@Preview
@Composable
fun PomodoroAddScreen() {
  PomodoroAddScreen({}, 2, "What you wanna to do Now")
}

@Composable
fun PomodoroList(
  list: MutableList<PomodoroTask>,
  swapItm: (Int, Int) -> Unit
) {
  PomodoroListScreen(list, onMove = { fromIndex, toIndex ->
    swapItm(fromIndex, toIndex)
  })
}









