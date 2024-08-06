package presentation.components

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun <T> DraggableLazyColumn(
    items: List<T>,
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable (T, Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var draggedDistance by remember { mutableStateOf(0f) }
    var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
    var overscrollJob by remember { mutableStateOf<Job?>(null) }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDrag = { change, offset ->
                    change.consume()
                    draggedDistance += offset.y

                    draggingItemIndex?.let { index ->
                        val itemSize = size.height / items.size
                        val newPosition = index + (draggedDistance / itemSize).toInt()

                        if (newPosition in items.indices && newPosition != index) {
                            onMove(index, newPosition)
                            draggingItemIndex = newPosition
                            draggedDistance = 0f
                        }

                        coroutineScope.launch {
                            val overscrollY = when {
                                draggedDistance > 0 -> draggedDistance.coerceAtMost(size.height.toFloat())
                                draggedDistance < 0 -> draggedDistance.coerceAtLeast(-size.height.toFloat())
                                else -> 0f
                            }
                            if (overscrollY != 0f) {
                                overscrollJob?.cancel()
                                overscrollJob = coroutineScope.launch {
                                    withContext(Dispatchers.Main) {
                                        lazyListState.animateScrollBy(overscrollY)
                                    }
                                }
                            }
                        }
                    }
                },
                onDragStart = { offset ->
                    lazyListState.layoutInfo.visibleItemsInfo
                        .firstOrNull { item -> offset.y.toInt() in item.offset..item.offset + item.size }
                        ?.let { item ->
                            draggingItemIndex = item.index
                        }
                },
                onDragEnd = {
                    onDragEnd()
                    draggingItemIndex = null
                    draggedDistance = 0f
                },
                onDragCancel = {
                    draggingItemIndex = null
                    draggedDistance = 0f
                }
            )
        }
    ) {
        items(items, key = key) { item ->
            val isDragging = items.indexOf(item) == draggingItemIndex
            val zIndex = if (isDragging) 1f else 0f

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndex)
            ) {
                itemContent(item, isDragging)
            }
        }
    }
}
