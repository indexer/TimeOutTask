package com.indexer.timeouttask.screen.pomodoroscreen

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.indexer.timeouttask.ui.theme.Purple200
import com.indexer.timeouttask.ui.theme.rowBackgroundColor

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
      PomodoroTitleInput(
        currentTitle = currentTitle, processIntentWithCurrentValue = processIntentWithCurrentValue
      )
      Row(modifier = Modifier.padding(8.dp, 8.dp)) {
        PomodoroNumberInput(currentValue, processIntentWithCurrentValue)
        Spacer(modifier = Modifier.width(Dimensions.spacing.small))
        IncrementDecrementButtons(processIntentWithCurrentValue)
      }
      TotalPomodoroTime(
        currentValue = currentValue, processIntentWithCurrentValue = processIntentWithCurrentValue
      )
    }
  }
}

@Composable
private fun PomodoroTitleInput(
  currentTitle: String,
  processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit
) {
  CommonTextField(
    label = stringResource(id = R.string.title_text),
    value = currentTitle,
    onValueChange = {
      processIntentWithCurrentValue(UpdatePomodoroTitle(it))
    },
    modifier = Modifier
      .fillMaxWidth()
      .height(Dimensions.toolbarSize.medium),
    keyboardOptions = KeyboardOptions.Default.copy(
      imeAction = ImeAction.Done // Specify the desired IME action
    ),
    onImeActionPerformed = { _: ImeAction ->
    })
  Text(
    stringResource(id = R.string.estimate_pomodoro),
    Modifier.padding(
      Dimensions.spacing.small, Dimensions.spacing.medium,
      0.dp, Dimensions.spacing.small
    ), style = MaterialTheme.typography.h6, fontWeight = FontWeight.Normal
  )
}

@Composable
private fun TotalPomodoroTime(
  currentValue: Int,
  processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit,
) {
  Row(
    modifier = Modifier
      .background(rowBackgroundColor)
      .fillMaxWidth()
      .wrapContentHeight(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    DisplayTotalTime(currentValue)
    MakeItButton(processIntentWithCurrentValue)
  }
}

@Composable
private fun DisplayTotalTime(currentValue: Int) {
  val totalMinutes = currentValue * 25
  val hours = totalMinutes / 60
  val minutes = totalMinutes % 60
  val timeText = if (hours > 0) {
    "Total Time: $hours hr $minutes min"
  } else {
    "Total Time: $minutes min"
  }
  Text(
    timeText, Modifier.padding(Dimensions.spacing.medium),
    style = MaterialTheme.typography.body1,
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.SemiBold
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MakeItButton(processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit) {
  val keyboardController = LocalSoftwareKeyboardController.current
  CommonOutlineButton(
    modifier = Modifier.padding(Dimensions.spacing.small),
    text = "Make It !",
    onClick = {
      keyboardController?.hide()
      processIntentWithCurrentValue(MakeIt)
    },
    buttonColor = Purple200,
    textColor = Color.White
  )
}

@Composable
private fun IncrementDecrementButtons(processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit) {
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

@Composable
private fun PomodoroNumberInput(
  currentValue: Int,
  processIntentWithCurrentValue: (PomodoroScreenIntent) -> Unit
) {
  CommonTextField(value = currentValue.toString(), onValueChange = {
    if (it.isNotBlank()) {
      processIntentWithCurrentValue(UpdatePomodoroNumber(it.toInt()))
    }
  },
    modifier = Modifier
      .width(100.dp)
      .height(Dimensions.toolbarSize.medium),
    keyboardOptions = KeyboardOptions.Default.copy(
      imeAction = ImeAction.Done // Specify the desired IME action
    ),
    onImeActionPerformed = { _: ImeAction ->
      // Handle IME actions
    }
  )
}







