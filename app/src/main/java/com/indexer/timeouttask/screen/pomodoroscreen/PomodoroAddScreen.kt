package com.indexer.timeouttask.screen.pomodoroscreen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.indexer.timeouttask.R
import com.indexer.timeouttask.commonbutton.CommonOutlineButton
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.DecrementPomodoro
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.IncrementPomodoro
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.MakeIt
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.UpdatePomodoroNumber
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenIntent.UpdatePomodoroTitle
import com.indexer.timeouttask.textfield.CommonTextField
import com.indexer.timeouttask.ui.theme.Dimensions
import com.indexer.timeouttask.ui.theme.RowBackgroundColor

@Composable
fun PomodoroAddScreen(
  processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit,
  currentValue: Int,
  currentTitle: String
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(Dimensions.spacing.medium),
    elevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {

      CommonTextField(
        label= stringResource(id = R.string.title_text),
        value = currentTitle,
        onValueChange = { it ->
            processIntentWithCurrentValue(UpdatePomodoroTitle(it))
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(Dimensions.toolbarSize.medium),
        keyboardOptions = KeyboardOptions.Default.copy(
          imeAction = ImeAction.Done // Specify the desired IME action
        ),
        onImeActionPerformed = { action: ImeAction ->
        })
      // Estimate Pomodoro
      Text(
        stringResource(id = R.string.estimate_pomodoro), Modifier.padding(
        Dimensions.spacing.small, Dimensions.spacing.medium, 0.dp, Dimensions.spacing.small
      ))

      // Pomodoro Number Input
      Row(modifier = Modifier.padding(Dimensions.spacing.small)) {
        CommonTextField(
          value = currentValue.toString(),
          onValueChange = {
            if (it.isNotBlank()) {
              processIntentWithCurrentValue(UpdatePomodoroNumber(it.toInt()))
            }
          },
          modifier = Modifier
            .width(100.dp)
            .height(Dimensions.toolbarSize.medium),
          onImeActionPerformed = { action: ImeAction ->
            // Handle IME actions
          }
        )

        Spacer(modifier = Modifier.width(Dimensions.spacing.small))

        // Increment and Decrement Buttons
        CommonOutlineButton(
          modifier = Modifier.height(56.dp),
          onClick = {
            processIntentWithCurrentValue(IncrementPomodoro)
          },
          icon = Filled.KeyboardArrowUp,
          buttonColor = Color.White
        )

        Spacer(modifier = Modifier.width(Dimensions.spacing.small))

        CommonOutlineButton(
          modifier = Modifier.height(Dimensions.toolbarSize.medium),
          onClick = {
            processIntentWithCurrentValue(DecrementPomodoro)

          },
          icon = Filled.KeyboardArrowDown,
          buttonColor = Color.White
        )
      }
      Spacer(modifier = Modifier.height(Dimensions.spacing.small))
      // Total Pomodoro Time
      Row(
        modifier = Modifier
          .background(RowBackgroundColor)
          .fillMaxWidth()
          .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Display total time
        val totalMinutes = currentValue * 25
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        val timeText = if (hours > 0) {
          "Total Time: $hours hr $minutes min"
        } else {
          "Total Time: $minutes min"
        }
        Text(timeText, Modifier.padding(Dimensions.spacing.small))
        // Make It button
        CommonOutlineButton(
          modifier = Modifier.padding(Dimensions.spacing.small),
          text = "Make It !",
          onClick = {
            processIntentWithCurrentValue(MakeIt)
          },
          buttonColor = Color.Black,
          textColor = Color.White
        )
      }
    }
  }
}





