package presentation.screens.plans

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.models.WorkoutDayDb
import presentation.components.DraggableLazyColumn
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
                                ) { workoutDay, isDragging ->
                                    ExercisesCard(workoutDay = workoutDay, isDragging = isDragging)
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
fun ExercisesCard(workoutDay: WorkoutDay, isDragging: Boolean = false) {
    var isPressed by remember { mutableStateOf(false) }

    val elevation by animateDpAsState(
        targetValue = if (isDragging) 16.dp else 4.dp
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed || isDragging) 0.95f else 1f
    )
    val shadowColor = if (isDragging) Color.Gray else Color.Transparent

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
                scaleX = scale
                scaleY = scale
                shadowElevation = if (isDragging) 8.dp.toPx() else 0f
                shape = RoundedCornerShape(12.dp)
                clip = true
            }.border(
                BorderStroke(1.dp, if (isDragging) AccentColor else Color.Transparent),
                shape = RoundedCornerShape(12.dp)
            )
            .shadow(elevation, shape = RoundedCornerShape(12.dp), isPressed, shadowColor)
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = if (isDragging) CardBackgroundColor.copy(alpha = 0.7f) else CardBackgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )

        ) {
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
                        text = exercise.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryTextColor
                    )
                }
            }
        }
    }
}
