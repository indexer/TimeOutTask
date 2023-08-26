package com.indexer.timeouttask.screen.pomodoroscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indexer.timeouttask.screen.mainscreen.PomodoroTask
import com.indexer.timeouttask.ui.theme.Purple200
import com.indexer.timeouttask.ui.theme.circularIndicatorBackgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable fun PomodoroListScreen(
  items: List<PomodoroTask>,
  onMove: (Int, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  val overscrollJob = remember { mutableStateOf<Job?>(null) }
  val dragDropListState = rememberDragDropListState(onMove = onMove)

  LazyColumn(
    modifier = modifier.dragGestureHandler(coroutineScope, dragDropListState, overscrollJob),
    state = dragDropListState.getLazyListState()
  ) {
    itemsIndexed(items) { index, item ->
      val displacementOffset = if (index == dragDropListState.getCurrentIndexOfDraggedListItem()) {
        dragDropListState.elementDisplacement.takeIf { it != 0f }
      } else {
        null
      }
      PomodoroListItem(item, displacementOffset)
    }
  }
}

fun Modifier.dragGestureHandler(
  scope: CoroutineScope,
  itemListDragAndDropState: ItemListDragAndDropState,
  overscrollJob: MutableState<Job?>
): Modifier = this.pointerInput(Unit) {
  detectDragGesturesAfterLongPress(onDrag = { change, offset ->
    change.consume()
    itemListDragAndDropState.onDrag(offset)
    handleOverscrollJob(overscrollJob, scope, itemListDragAndDropState)
  }, onDragStart = { offset -> itemListDragAndDropState.onDragStart(offset) },
    onDragEnd = { itemListDragAndDropState.onDragInterrupted() },
    onDragCancel = { itemListDragAndDropState.onDragInterrupted() })
}

private fun handleOverscrollJob(
  overscrollJob: MutableState<Job?>,
  scope: CoroutineScope,
  itemListDragAndDropState: ItemListDragAndDropState
) {
  if (overscrollJob.value?.isActive == true) return
  val overscrollOffset = itemListDragAndDropState.checkForOverScroll()
  if (overscrollOffset != 0f) {
    overscrollJob.value = scope.launch {
      itemListDragAndDropState.getLazyListState().scrollBy(overscrollOffset)
    }
  } else {
    overscrollJob.value?.cancel()
  }
}

@Composable
private fun PomodoroListItem(
  item: PomodoroTask,
  displacementOffset: Float?,
) {
  val isBeingDragged = displacementOffset != null
  val backgroundColor = if (isBeingDragged) {
    Color.LightGray
  } else {
    Color.White
  }

  Column(
    modifier = Modifier
      .graphicsLayer { translationY = displacementOffset ?: 0f }
      .background(Color.White, shape = RoundedCornerShape(4.dp))
      .fillMaxWidth()
      .fillMaxHeight()
  ) {
    Column(
      modifier = Modifier.padding(start = 8.dp, 0.dp, 8.dp, 8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      PomodoroProgressCard(item, backgroundColor)
    }
  }
}

@Composable
private fun PomodoroProgressCard(
  item: PomodoroTask,
  backgroundColor: Color,
) {

  Card(
    shape = RoundedCornerShape(8.dp),
    backgroundColor = backgroundColor,
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (item.progress < 100f) {
        PomodoroProgressBox(item.progress)
      } else {
        PomodoroCompletionBox()
      }
      PomodoroDetailsColumn(item)
    }
  }
}

@Composable
private fun PomodoroProgressBox(progressValue: Float) {
  Box(
    modifier = Modifier
      .padding(8.dp)
      .size(60.dp)
      .background(
        circularIndicatorBackgroundColor,
        shape = CircleShape
      ),
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator(
      progress = progressValue / 100,
      modifier = Modifier.size(60.dp),
      backgroundColor = circularIndicatorBackgroundColor,
      color = Purple200,
      strokeWidth = 8.dp
    )
    Text(
      text = "${progressValue.toInt()}%",
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = Color.Black
    )
  }
}

@Composable
private fun PomodoroCompletionBox() {
  Box(
    modifier = Modifier
      .padding(8.dp)
      .size(60.dp)
      .background(Purple200, shape = CircleShape),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Default.Check,
      contentDescription = null,
      tint = Color.White,
      modifier = Modifier.size(32.dp)
    )
  }
}

@Composable
private fun PomodoroDetailsColumn(item: PomodoroTask) {
  Column(
    modifier = Modifier.padding(start = 8.dp, 0.dp, 8.dp, 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(
      text = item.title,
      modifier = Modifier.fillMaxWidth(),
      fontSize = 20.sp
    )
    Text(
      text = item.description,
      modifier = Modifier.fillMaxWidth(),
      fontSize = 13.sp
    )
  }
}










