package com.indexer.timeouttask.screen.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroAddScreen
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroCompleteScreen
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroListScreen
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.viewmodel.PomodoroScreenViewModel
import com.indexer.timeouttask.ui.theme.TimeOutTaskTheme
import com.indexer.timeouttask.ui.utils.ColorGenerator
import org.koin.androidx.compose.koinViewModel

@Composable
fun PomodoroScreen() {
  val viewModel: PomodoroScreenViewModel = koinViewModel()
  val processIntentWithCurrentValue = viewModel.provideProcessIntent()
  val onMoveItem = viewModel.getSwapFunction()

  val state = viewModel.pomodoroScreenStateState.collectAsState()
  val pomodoroList = viewModel.pomodoroList.collectAsState()
  val showCongratulationsScreen = viewModel.showCongratulationsScreen.collectAsState()
  val showAddScreen = viewModel.showAddScreen.collectAsState()

  val content: @Composable (PaddingValues) -> Unit = { _ ->
    // Your screen content here
    if (!showCongratulationsScreen.value) {
      Column {
        if (showAddScreen.value) {
          PomodoroAddScreen(
            processIntentWithCurrentValue,
            state.value.pomodoroDurationInMinutes,
            state.value.pomodoroTitle
          )
        }
        if (pomodoroList.value.isNotEmpty()) {
          PomodoroList(list = pomodoroList.value.toMutableList(), onMoveItem)
        }else{

        }
      }
    } else {
      PomodoroCompleteScreen(processIntentWithCurrentValue)
    }
  }
  TimeOutTaskTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Start
            ) {
              Text(text = "Pomodoro") // Replace with your desired title text
            }
          })
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = {
            viewModel.taskAdded.value = true
          },
          backgroundColor = MaterialTheme.colors.primary,
          contentColor = Color.White
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
          )
        }
      },
      floatingActionButtonPosition = FabPosition.End,
      isFloatingActionButtonDocked = true,
      content = content
    )
  }
}

data class PomodoroTask(
  var title: String,
  var description: String,
  val alarmTimerState: AlarmTimerState,
  var progress: Float,
  var backgroundColor: Int? = ColorGenerator().getRandomColor()
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









