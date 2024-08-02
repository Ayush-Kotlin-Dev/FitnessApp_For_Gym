package presentation.screens.plans

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.models.WorkoutDayDb
import presentation.screens.tabs.SharedWorkoutViewModel

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
                if (routineName != null) {
                    isLoading = true
                    viewModel.loadWorkoutPlanFromDb(routineName)
                    isLoading = false
                } else {
                    isLoading = false
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Selected Routine") },
                    actions = {
                        IconButton(onClick = { navigator.push(WorkoutPlanScreen()) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    AnimatedVisibility(
                        visible = !isLoading,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            if (selectedRoutine != null) {
                                Text(
                                    text = selectedRoutine!!,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    currentWorkoutPlan?.days?.let { days ->
                                        items(days) { workoutDay ->
                                            ExercisesCard(workoutDay = workoutDay)
                                        }
                                    }
                                }
                            } else {
                                Text("No routine selected. Tap the edit button to choose a routine.")
                            }
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = workoutDay.day, style = MaterialTheme.typography.titleLarge)
            Text(text = workoutDay.focus, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            workoutDay.exercises.forEach { exercise ->
                Text("• $exercise")
            }
        }
    }
}