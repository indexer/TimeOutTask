package com.indexer.timeouttask.commonbutton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.indexer.timeouttask.ui.theme.Dimensions

@Composable
fun CommonOutlineButton(
  modifier: Modifier? = Modifier,
  text: String? = "",
  onClick: () -> Unit,
  buttonColor: Color = MaterialTheme.colors.primary,
  textColor: Color = contentColorFor(buttonColor),
  icon: ImageVector? = null,
  iconTint: Color = textColor
) {
  var defaultModifier = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .padding(Dimensions.spacing.small)
  when {
    modifier != null -> {
      defaultModifier = modifier
    }
  }
  OutlinedButton(
    modifier = defaultModifier,
    onClick = { onClick() },
    colors = ButtonDefaults.textButtonColors(
      backgroundColor = buttonColor, contentColor = textColor
    )
  ) {
    if (icon != null) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.padding(end = 4.dp)
      )
    }
    if (text != null)
      Text(text)
  }
}