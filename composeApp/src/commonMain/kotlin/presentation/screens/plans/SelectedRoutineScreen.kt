package presentation.screens.plans

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.models.WorkoutDayDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.screens.tabs.SharedWorkoutViewModel

val BackgroundColor = Color(0xFF121212)
val CardBackgroundColor = Color(0xFF1E1E1E)
val PrimaryTextColor = Color(0xFFE0E0E0)
val SecondaryTextColor = Color(0xFFB0B0B0)
val AccentColor = Color(0xFFFF4444)
val DividerColor = Color(0xFF2A2A2A)

class SelectedRoutineScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SharedWorkoutViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var selectedRoutine by remember { mutableStateOf<String?>(null) }
        val currentWorkoutPlan by viewModel.currentWorkoutPlan.collectAsState()
        var isLoading by remember { mutableStateOf(true) }
        val lazyListState = rememberLazyListState()

        LaunchedEffect(Unit) {
            viewModel.getSelectedRoutineFlow().collect { routineName ->
                selectedRoutine = routineName
                isLoading = if (routineName != null) {
                    viewModel.loadWorkoutPlanFromDb(routineName)
                    false
                } else {
                    false
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Selected Routine", color = PrimaryTextColor) },
                    actions = {
                        IconButton(onClick = { navigator.push(WorkoutPlanScreen()) }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = AccentColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBackgroundColor)
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AccentColor
                    )
                }
                AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        selectedRoutine?.let { routine ->
                            Text(
                                text = routine,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryTextColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tip: Press and hold to reorder workout days",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SecondaryTextColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            currentWorkoutPlan?.days?.let { days ->
                                DraggableLazyColumn(
                                    items = days,
                                    lazyListState = lazyListState,
                                    onMove = { from, to ->
                                        viewModel.reorderWorkoutDays(from, to)
                                    },
                                    onDragEnd = {
                                    }
                                ) { workoutDay ->
                                    ExercisesCard(workoutDay = workoutDay)
                                }
                            } ?: Text(
                                "No workout plan found for the selected routine.",
                                color = SecondaryTextColor
                            )
                        } ?: Text(
                            "No routine selected. Tap the edit button to choose a routine.",
                            color = SecondaryTextColor
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun <T> DraggableLazyColumn(
    items: List<T>,
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable (T) -> Unit
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
            val elevation = if (isDragging) 8.dp else 0.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndex)
            ) {
                itemContent(item)
            }
        }
    }
}

@Composable
fun ExercisesCard(workoutDay: WorkoutDayDb) {
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
            .graphicsLayer {
                scaleX = if (isPressed) 0.95f else 1f
                scaleY = if (isPressed) 0.95f else 1f
            }
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Drag to reorder",
                tint = SecondaryTextColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = workoutDay.day,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AccentColor
                )
                Text(
                    text = workoutDay.focus,
                    style = MaterialTheme.typography.titleMedium,
                    color = SecondaryTextColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                workoutDay.exercises.forEachIndexed { index, exercise ->
                    if (index > 0) {
                        Divider(
                            color = DividerColor,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(AccentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = exercise,
                            style = MaterialTheme.typography.bodyMedium,
                            color = PrimaryTextColor
                        )
                    }
                }
            }
        }
    }
}