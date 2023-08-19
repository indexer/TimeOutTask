package com.indexer.timeouttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.indexer.timeouttask.screen.mainscreen.PomodoroScreen
import com.indexer.timeouttask.ui.theme.TimeOutTaskTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TimeOutTaskTheme{
        Surface(
          modifier = Modifier
            .fillMaxSize()
            ,color = MaterialTheme.colors.background
        ) {
          Column {
            PomodoroScreen()
          }
        }
      }
    }
  }
}
