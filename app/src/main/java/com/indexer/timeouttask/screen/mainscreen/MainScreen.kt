package com.indexer.timeouttask.screen.mainscreen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.tooling.preview.Preview
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroAddScreen
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroListScreen
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.move
import com.indexer.timeouttask.screen.pomodoroscreen.viewmodel.PomodoroScreenViewModel
import com.indexer.timeouttask.ui.theme.TimeOutTaskTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
  TimeOutTaskTheme {
    val viewModel: PomodoroScreenViewModel = koinViewModel()
    val processIntentWithCurrentValue = viewModel.provideProcessIntent()
    val state = viewModel.pomodoroScreenStateState.collectAsState()
    PomodoroAddScreen(
      processIntentWithCurrentValue, state.value.pomodoroNumber, state.value.pomodoroTitle
    )
    PomodoroScreenList()
  }
}

data class PomodoroData(
  var title: String,
  var description: String,
  val alarmTimerState: AlarmTimerState
)

@Preview
@Composable
fun PomodoroAddScreen() {
  PomodoroAddScreen({}, 2, "What you wanna to do")
}

@Composable
fun PomodoroScreenList() {
  val list = listOf(
    PomodoroData(
      "Learning How to Talk", "you will never know if you don’t talk to them",
      AlarmTimerState(25, isRunning = false, isPaused = false, isCompleted = false, isReset = false)
    ),
    PomodoroData(
      "Learning How to Drink Alcohol", "you will never know if you don’t drink",
      AlarmTimerState(
        25, isRunning = false, isPaused = false, isCompleted = false,
        isReset = false
      )
    )
  ).toMutableStateList()
  PomodoroListScreen(list, onMove = { fromIndex, toIndex ->
    //resetTimerForOtherItems(list,0)
    list.move(fromIndex, toIndex)
  })
}

private fun resetTimerForOtherItems(items: List<PomodoroData>, currentIndex: Int) {
  items.forEachIndexed { index, item ->
    if (index != currentIndex) {
      item.alarmTimerState.isRunning = false
    }
  }
}








