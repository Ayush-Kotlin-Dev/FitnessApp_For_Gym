package presentation.screens.plans

import cafe.adriel.voyager.core.screen.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import presentation.screens.homescreen.HomeScreenViewModel
import presentation.screens.tabs.SharedWorkoutViewModel


data class PlanDetailScreen(
    val planName: String
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SharedWorkoutViewModel>()
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()
        val allSelectedExercises by viewModel.selectedExercises.collectAsState()
        val currentPlanExercises = allSelectedExercises[planName] ?: emptyMap()
        var editingDay by remember { mutableStateOf<String?>(null) }
        val navigator: Navigator = LocalNavigator.currentOrThrow

        // Get the original workout plan to access the correct body parts
        val originalPlan = viewModel.getWorkoutDaysForPlan(planName)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(planName) },
                    navigationIcon = {
                        Button(onClick = {
                            navigator.pop()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                )
            }
        ) { innerPadding: PaddingValues ->
            Box(
                modifier = Modifier.padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    originalPlan.forEach { originalWorkoutDay ->
                        val day = originalWorkoutDay.day
                        val exercises = currentPlanExercises[day] ?: originalWorkoutDay.exercises
                        WorkoutDayCard(
                            workoutDay = WorkoutDay(day, originalWorkoutDay.focus, exercises.toMutableList()),
                            onEditClick = { editingDay = day },
                            onExercisesChanged = { newExercises ->
                                viewModel.updateSelectedExercises(planName, day, newExercises)
                            }
                        )
                    }
                }

                if (editingDay != null) {
                    val originalWorkoutDay = originalPlan.find { it.day == editingDay }
                    if (originalWorkoutDay != null) {
                        val currentExercises = currentPlanExercises[editingDay] ?: originalWorkoutDay.exercises
                        EditExercisesDialog(
                            workoutDay = WorkoutDay(editingDay!!, originalWorkoutDay.focus, currentExercises.toMutableList()),
                            onDismiss = { editingDay = null },
                            onSave = { updatedExercises ->
                                viewModel.updateSelectedExercises(planName, editingDay!!, updatedExercises)
                                editingDay = null
                            },
                            sharedViewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}