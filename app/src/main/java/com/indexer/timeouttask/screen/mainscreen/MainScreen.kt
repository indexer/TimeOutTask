package com.indexer.timeouttask.screen.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indexer.timeouttask.screen.pomodoroscreen.PomodoroAddScreen
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.viewmodel.PomodoroScreenViewModel
import com.indexer.timeouttask.ui.theme.TimeOutTaskTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
  TimeOutTaskTheme {
    val viewModel: PomodoroScreenViewModel = koinViewModel()
    val processIntentWithCurrentValue = viewModel.provideProcessIntent()
    val state = viewModel.pomodoroScreenStateState.collectAsState()
    val alarmTimerState = viewModel.alarmState.collectAsState()
    PomodoroAddScreen(processIntentWithCurrentValue, state.value.pomodoroNumber, state.value.pomodoroTitle)
    AlarmTimerContent(state = alarmTimerState.value)
  }
}

@Preview
@Composable
fun PomodoroAddScreen(){
  PomodoroAddScreen({}, 2,"What you wanna to do")
}

@Composable
fun AlarmTimerContent(state: AlarmTimerState) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    if (state.isCompleted) {
      Text("Completed Your Pomodoro", style = MaterialTheme.typography.h6)
    } else {
      Text("Time: ${state.time} seconds", style = MaterialTheme.typography.h6)
      Spacer(modifier = Modifier.padding(8.dp))
    }
  }
}






