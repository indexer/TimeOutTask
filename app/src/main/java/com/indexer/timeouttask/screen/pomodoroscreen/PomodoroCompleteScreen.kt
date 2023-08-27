package com.indexer.timeouttask.screen.pomodoroscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.indexer.timeouttask.ui.theme.Purple200

@Composable
fun PomodoroCompleteScreen(
  onDismiss: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = "Congratulations!",
      style = MaterialTheme.typography.h4,
      color = Purple200
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = "Congratulations, you've completed the task!",
      style = MaterialTheme.typography.body1,
      color = Color.Black
    )
    Spacer(modifier = Modifier.height(32.dp))
    Button(onClick = onDismiss) {
      Text(text = "Dismiss")
    }
  }
}
