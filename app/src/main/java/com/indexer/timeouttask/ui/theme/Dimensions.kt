package com.indexer.timeouttask.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Spacing(
  val small: Dp,
  val medium: Dp,
  val large: Dp
)

data class RounderCornerSize(
  val small: Dp,
  val medium: Dp,
)

data class TextSize(
  val small: TextUnit,
  val normal: TextUnit,
  val medium: TextUnit,
  val large: TextUnit
)

data class ToolbarSize(
  val normal: Dp,
  val medium: Dp
)

object Dimensions {
  val spacing = Spacing(
    small = 8.dp,
    medium = 16.dp,
    large = 24.dp
  )

  val roundedCorner = RounderCornerSize(
    small = 8.dp,
    medium = 10.dp
  )

  val toolbarSize = ToolbarSize(
    normal = 48.dp,
    medium = 56.dp
  )

  val textSize = TextSize(
    small = 12.sp,
    normal = 14.sp,
    medium = 16.sp,
    large = 20.sp
  )
}