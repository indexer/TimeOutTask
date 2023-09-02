package com.indexer.timeouttask.ui.utils

class ColorGenerator {
  private val colors = mutableListOf(
    android.graphics.Color.parseColor("#2F3559"),
    android.graphics.Color.parseColor("#238E4B"),
    android.graphics.Color.parseColor("#E8308C"),
    android.graphics.Color.parseColor("#3080E8"),
    android.graphics.Color.parseColor("#EE6D66")
  )

  private val usedColors = mutableSetOf<Int>()

  @Synchronized
  fun getRandomColor(): Int {
    if (usedColors.size == colors.size) {
      // All colors have been used, reset the used colors set
      usedColors.clear()
    }
    var color: Int
    do {
      color = colors.random()
    } while (color in usedColors)

    usedColors.add(color)
    return color
  }
}