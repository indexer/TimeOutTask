package com.indexer.timeouttask.screen.mainscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.indexer.timeouttask.R
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroAddScreen
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroCompleteScreen
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroListScreen
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenState
import com.indexer.timeouttask.screen.pomodoroscreen.viewmodel.PomodoroScreenViewModel
import com.indexer.timeouttask.ui.theme.Purple700
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
    PomodoroContent(
      state = state.value, pomodoroList = pomodoroList.value,
      showCongratulationsScreen = showCongratulationsScreen.value,
      showAddScreen = showAddScreen.value,
      onTaskAdded = {
        viewModel.taskAdded.value = true
      }, onMoveItem = onMoveItem, processIntentWithCurrentValue
    )
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
              Text(
                text = stringResource(id = R.string.app_title), maxLines = 1
              )
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

@Composable
fun PomodoroContent(
  state: PomodoroScreenState,
  pomodoroList: List<PomodoroTask>,
  showCongratulationsScreen: Boolean,
  showAddScreen: Boolean,
  onTaskAdded: () -> Unit,
  onMoveItem: (Int, Int) -> Unit,
  processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit
) {
  Column {
    if (!showCongratulationsScreen) {
      AnimatedVisibility(
        visible = showAddScreen,
        enter = fadeIn(animationSpec = tween(durationMillis = 200)),
        exit = fadeOut(animationSpec = tween(durationMillis = 100))
      ) {
        PomodoroAddScreen(
          processIntentWithCurrentValue = processIntentWithCurrentValue,
          currentValue = state.pomodoroDurationInMinutes,
          currentTitle = state.pomodoroTitle
        )
      }
      AnimatedVisibility(
        visible = pomodoroList.isNotEmpty(),
        enter = fadeIn(
          animationSpec = tween(durationMillis = 1000)
        )
      ) {
        PomodoroList(list = pomodoroList.toMutableList(), onMoveItem)
      }

      if (!showAddScreen && pomodoroList.isEmpty()) {
        NewTaskButton(onTaskAdded = onTaskAdded)
      }
    } else {
      PomodoroCompleteScreen(processIntentWithCurrentValue = processIntentWithCurrentValue)
    }
  }
}

data class PomodoroTask(
  var title: String,
  var description: String,
  val alarmTimerState: AlarmTimerState,
  var progress: Float,
  var backgroundColor: Int? = ColorGenerator().getRandomColor()
)

@Composable
fun NewTaskButton(onTaskAdded: () -> Unit) {
  val stroke = Stroke(
    width = 4f,
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
  )
  Box(
    modifier = Modifier
      .padding(8.dp)
      .fillMaxWidth()
      .size(56.dp)
      .drawBehind {
        drawRoundRect(color = Purple700, style = stroke)
      }
      .clickable {
        onTaskAdded()
      },
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = stringResource(id = R.string.create_new_task),
      textAlign = TextAlign.Center,
      color = Color.Black
    )
  }
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









