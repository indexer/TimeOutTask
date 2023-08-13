package com.indexer.timeouttask.screen.pomodoroscreen

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.Job

@Composable
fun rememberDragDropListState(
  lazyListState: LazyListState = rememberLazyListState(),
  onMove: (Int, Int) -> Unit,
): ItemListDragAndDropState {
  return remember { ItemListDragAndDropState(lazyListState, onMove) }
}


class ItemListDragAndDropState(
  private val lazyListState: LazyListState,
  private val onMove: (Int, Int) -> Unit
) {
  private var draggedDistance by mutableStateOf(0f)
  private var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)
  private var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)
  private var overscrollJob by mutableStateOf<Job?>(null)


  private val currentElement: LazyListItemInfo?
    get() = currentIndexOfDraggedItem?.let {
      lazyListState.getVisibleItemInfoFor(absoluteIndex = it)
    }

  private val initialOffsets: Pair<Int, Int>?
    get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }

  val elementDisplacement: Float?
    get() = currentIndexOfDraggedItem
      ?.let { lazyListState.getVisibleItemInfoFor(absoluteIndex = it) }
      ?.let { item ->
        (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset
      }


  // Functions for handling drag gestures
  fun onDragStart(offset: Offset) {
    lazyListState.layoutInfo.visibleItemsInfo
      .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
      ?.also {
        currentIndexOfDraggedItem = it.index
        initiallyDraggedElement = it
      }
  }

  fun onDragInterrupted() {
    draggedDistance = 0f
    currentIndexOfDraggedItem = null
    initiallyDraggedElement = null
    overscrollJob?.cancel()
  }

  fun onDrag(offset: Offset) {
    draggedDistance += offset.y
    initialOffsets?.let { (topOffset, bottomOffset) ->
      val startOffset = topOffset + draggedDistance
      val endOffset = bottomOffset + draggedDistance

      currentElement?.let { hovered ->
        lazyListState.layoutInfo.visibleItemsInfo
          .filterNot { item -> item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index }
          .firstOrNull { item ->
            val delta = startOffset - hovered.offset
            when {
              delta > 0 -> (endOffset > item.offsetEnd)
              else -> (startOffset < item.offset)
            }
          }
          ?.also { item ->
            currentIndexOfDraggedItem?.let { current -> onMove.invoke(current, item.index) }
            currentIndexOfDraggedItem = item.index
          }
      }
  }
}

  fun checkForOverScroll(): Float {
    return initiallyDraggedElement?.let {
      val startOffset = it.offset + draggedDistance
      val endOffset = it.offsetEnd + draggedDistance
      return@let when {
        draggedDistance > 0 -> (endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
        draggedDistance < 0 -> (startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
        else -> null
      }
    } ?: 0f
  }

  fun getLazyListState(): LazyListState {
    return lazyListState
  }

  fun getCurrentIndexOfDraggedItem(): Int? {
    return currentIndexOfDraggedItem
  }
}



fun LazyListState.getVisibleItemInfoFor(absoluteIndex: Int): LazyListItemInfo? {
  return this.layoutInfo.visibleItemsInfo.getOrNull(absoluteIndex - this.layoutInfo.visibleItemsInfo.first().index)
}

/*
  Bottom offset of the element in Vertical list
*/
val LazyListItemInfo.offsetEnd: Int
  get() = this.offset + this.size

/*
   Moving element in the list
*/
fun <T> MutableList<T>.move(from: Int, to: Int) {
  if (from == to)
    return

  val element = this.removeAt(from) ?: return
  this.add(to, element)
}