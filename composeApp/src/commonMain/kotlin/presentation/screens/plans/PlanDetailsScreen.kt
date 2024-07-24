package presentation.screens.plans

import cafe.adriel.voyager.core.screen.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import presentation.screens.homescreen.HomeScreenViewModel


data class PlanDetailScreen(
    val planName: String,
    val updateCurrentScreen: (Screen) -> Unit
) : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SharedWorkoutViewModel>()
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()
        val workoutDays by remember { mutableStateOf(viewModel.getWorkoutDaysForPlan(planName)) }
        var editingDay by remember { mutableStateOf<String?>(null) }

        // Update the current screen when this screen is displayed
        LaunchedEffect(Unit) {
            println("PlanDetailScreen: updating current screen")
            updateCurrentScreen(this@PlanDetailScreen)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(homeScreenViewModel.currentScreen.value.toString())
            Text(planName, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            workoutDays.forEach { workoutDay ->
                WorkoutDayCard(
                    workoutDay = workoutDay,
                    onEditClick = { editingDay = workoutDay.day },
                    onExercisesChanged = { newExercises ->
                        workoutDay.exercises.clear()
                        workoutDay.exercises.addAll(newExercises)
                    }
                )
            }
        }

        if (editingDay != null) {
            EditExercisesDialog(
                workoutDay = workoutDays.first { it.day == editingDay },
                onDismiss = { editingDay = null },
                onSave = { updatedExercises ->
                    workoutDays.first { it.day == editingDay }.exercises.apply {
                        clear()
                        addAll(updatedExercises)
                    }
                    viewModel.updateSelectedExercises(planName, editingDay!!, updatedExercises.toList())
                    editingDay = null
                },
                sharedViewModel = viewModel
            )
        }
    }
}