package com.indexer.timeouttask.screen.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroAddScreen
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroCompleteScreen
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
    val showCongratulationsScreen = viewModel.showCongratulationsScreen.collectAsState()
    TopAppBar(
      title = {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
        ) {
          Text(text = "Pomodoro") // Replace with your desired title text
        }
      })
    if (!showCongratulationsScreen.value) {
      PomodoroAddScreen(
        processIntentWithCurrentValue, state.value.pomodoroDurationInMinutes,
        state.value.pomodoroTitle
      )
      PomodoroList(list = pomodoroList.value.toMutableList(), onMoveItem)
    } else {
      PomodoroCompleteScreen(processIntentWithCurrentValue)
    }
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









