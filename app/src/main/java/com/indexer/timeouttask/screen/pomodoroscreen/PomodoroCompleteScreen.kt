package com.indexer.timeouttask.screen.pomodoroscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indexer.timeouttask.R.string
import com.indexer.timeouttask.commonbutton.CommonOutlineButton
import com.indexer.timeouttask.screen.mainscreen.PomodoroTask
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.DismissCompletedTask
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.ShortBreak
import com.indexer.timeouttask.ui.theme.Dimensions
import com.indexer.timeouttask.ui.theme.rowBackgroundColor

@Composable
fun PomodoroCompleteScreen(
  processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit,
  completedTask: PomodoroTask,
  remainingTime: String
) {
  val isPlayIconVisible = remember { mutableStateOf(true) }
  val icon = if (isPlayIconVisible.value) Icons.Filled.PlayCircle else Icons.Filled.PauseCircle

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(completedTask.backgroundColor))
      .padding(Dimensions.spacing.medium),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Card(
      shape = RoundedCornerShape(8.dp),
      backgroundColor = Color.White,
      modifier = Modifier
        .padding(Dimensions.spacing.medium)
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(Dimensions.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = stringResource(id = string.congratulation_title),
          style = MaterialTheme.typography.h4,
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Bold,
          color = Color(completedTask.backgroundColor)
        )
        Spacer(modifier = Modifier.height(Dimensions.spacing.medium))
        Text(
          text = stringResource(id = string.congratulation_description),
          fontSize = 16.sp,
          style = MaterialTheme.typography.caption,
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Normal,
          color = Color.Black
        )
        Spacer(modifier = Modifier.height(Dimensions.spacing.large))
        Box(
          modifier = Modifier
            .padding(Dimensions.spacing.small)
            .size(300.dp)
            .background(Color(completedTask.backgroundColor), shape = CircleShape),
          contentAlignment = Alignment.Center
        ) {
          val progress = completedTask.progress / 100
          CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(300.dp),
            backgroundColor = rowBackgroundColor,
            color = rowBackgroundColor,
            strokeWidth = 16.dp
          )
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight()
              .padding(Dimensions.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            // Add a play icon using Icon composable
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier
                .size(48.dp)
                .clickable {
                  isPlayIconVisible.value = !isPlayIconVisible.value
                  processIntentWithCurrentValue(ShortBreak)
                }
            )
            Text(
              text = remainingTime,
              fontSize = 60.sp,
              style = MaterialTheme.typography.h3,
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Short Break Time",
              fontSize = 20.sp,
              style = MaterialTheme.typography.h3,
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
        if (isPlayIconVisible.value) {
          Spacer(modifier = Modifier.height(Dimensions.spacing.small))
          CommonOutlineButton(
            modifier = Modifier.padding(Dimensions.spacing.small),
            text = stringResource(id = string.let_go_next),
            onClick = {
              processIntentWithCurrentValue(DismissCompletedTask)
            },
            buttonColor = Color(completedTask.backgroundColor),
            textColor = Color.White
          )
        }
      }
    }
  }
}

