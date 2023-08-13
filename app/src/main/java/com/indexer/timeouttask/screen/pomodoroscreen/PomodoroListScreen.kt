package com.indexer.timeouttask.screen.pomodoroscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun PomodoroListScreen(
  items: List<String>,
  onMove: (Int, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  val mutableItems = remember { mutableStateOf(items) }
  val overscrollJob = remember { mutableStateOf<Job?>(null) }
  val dragDropListState = rememberDragDropListState(onMove = onMove)

  LazyColumn(
    modifier = modifier.dragGestureHandler(coroutineScope, dragDropListState, overscrollJob),
    state = dragDropListState.getLazyListState()
  ) {
    itemsIndexed(mutableItems.value) { index, item ->
      val displacementOffset = if (index == dragDropListState.getCurrentIndexOfDraggedListItem()) {
        dragDropListState.elementDisplacement.takeIf { it != 0f }
      } else {
        null
      }
      DrawDraggableItem(item, displacementOffset)
    }
  }
}

fun Modifier.dragGestureHandler(
  scope: CoroutineScope,
  itemListDragAndDropState: ItemListDragAndDropState,
  overscrollJob: MutableState<Job?>
): Modifier = this.pointerInput(Unit) {
  detectDragGesturesAfterLongPress(
    onDrag = { change, offset ->
      change.consume()
      itemListDragAndDropState.onDrag(offset)
      handleOverscrollJob(overscrollJob, scope, itemListDragAndDropState)
    },
    onDragStart = { offset -> itemListDragAndDropState.onDragStart(offset) },
    onDragEnd = { itemListDragAndDropState.onDragInterrupted() },
    onDragCancel = { itemListDragAndDropState.onDragInterrupted() }
  )
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
private fun DrawDraggableItem(
  item: String,
  displacementOffset: Float?
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
    Card(
      shape = RoundedCornerShape(8.dp), backgroundColor = backgroundColor,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Text(text = item, modifier = Modifier.padding(16.dp))
    }
  }
}







