package com.indexer.timeouttask.screen.mainscreen.addscreen

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
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.indexer.timeouttask.commonbutton.CommonOutlineButton
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenIntent
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenIntent.DecrementPomodoro
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenIntent.IncrementPomodoro
import com.indexer.timeouttask.screen.mainscreen.addscreen.domain.AddScreenIntent.UpdatePomodoroNumber
import com.indexer.timeouttask.textfield.CommonTextField
import com.indexer.timeouttask.ui.theme.Dimensions
import com.indexer.timeouttask.ui.theme.RowBackgroundColor

@Composable
fun AddScreen(
  processIntentWithCurrentValue: (AddScreenIntent) -> Unit,
  currentValue: Int
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

      // Estimate Pomodoro
      Text(
        "Estimate Pomodoro:", Modifier.padding(
        Dimensions.spacing.small,
        Dimensions.spacing.medium, 0.dp, Dimensions.spacing.small
      )
      )

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
          onClick = {},
          buttonColor = Color.Black,
          textColor = Color.White
        )
      }
    }
  }
}





