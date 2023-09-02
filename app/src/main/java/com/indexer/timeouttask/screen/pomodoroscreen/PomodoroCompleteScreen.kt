package com.indexer.timeouttask.screen.pomodoroscreen

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.indexer.timeouttask.R
import com.indexer.timeouttask.R.string
import com.indexer.timeouttask.commonbutton.CommonOutlineButton
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.DismissCompletedTask
import com.indexer.timeouttask.ui.theme.Dimensions
import com.indexer.timeouttask.ui.theme.Blue200

@Composable
fun PomodoroCompleteScreen(processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit) {
  val mContext = LocalContext.current
  val mMediaPlayer = MediaPlayer.create(mContext, R.raw.task_complete)
  mMediaPlayer.start()
  Column(
    modifier = Modifier
      .fillMaxSize().padding(Dimensions.spacing.medium),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(id = string.congratulation_title),
      style = MaterialTheme.typography.h4,
      color = Blue200
    )
    Spacer(modifier = Modifier.height(Dimensions.spacing.medium))
    Text(
      text = stringResource(id = string.congratulation_description),
      style = MaterialTheme.typography.body1,
      color = Color.Black
    )
    Spacer(modifier = Modifier.height(Dimensions.spacing.large))
    CommonOutlineButton(
      modifier = Modifier.padding(Dimensions.spacing.small),
      text = stringResource(id = string.let_go_next),
      onClick = {
        mMediaPlayer.stop()
        processIntentWithCurrentValue(DismissCompletedTask)
      },
      buttonColor = Blue200,
      textColor = Color.White
    )
  }
}
