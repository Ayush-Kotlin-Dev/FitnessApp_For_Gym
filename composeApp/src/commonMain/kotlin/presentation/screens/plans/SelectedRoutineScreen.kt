package presentation.screens.plans

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.models.WorkoutDayDb
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
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = AccentColor)
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AccentColor)
                } else {
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
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    currentWorkoutPlan?.days?.let { days ->
                                        items(days) { workoutDay ->
                                            ExercisesCard(workoutDay = workoutDay)
                                        }
                                    }
                                }
                            } ?: Text("No routine selected. Tap the edit button to choose a routine.", color = SecondaryTextColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExercisesCard(workoutDay: WorkoutDayDb) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    Divider(color = DividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
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