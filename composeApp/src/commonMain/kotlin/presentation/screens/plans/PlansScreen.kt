package presentation.screens.plans

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import presentation.screens.tabs.SharedWorkoutViewModel

class WorkoutPlanScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SharedWorkoutViewModel>()
        val plans = listOf(
            "5-Day Split",
            "6-Day Push/Pull/Legs",
            "4-Day Upper/Lower",
            "5-Day Upper/Lower/Full",
            "4-Day Push/Pull",
            "6-Day Body Part Split"
        )
        var selectedPlan by remember { mutableStateOf(plans.first()) }
        var expanded by remember { mutableStateOf(false) }
        val workoutDays by remember(selectedPlan) {
            mutableStateOf(viewModel.getWorkoutDaysForPlan(selectedPlan))
        }
        val allSelectedExercises by viewModel.selectedExercises.collectAsState()
        val currentPlanExercises = allSelectedExercises[selectedPlan] ?: emptyMap()
        var editingDay by remember { mutableStateOf<String?>(null) }
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Workout Plan") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.7f),
                        titleContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    OutlinedTextField(
                        value = selectedPlan,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Red,
                            unfocusedIndicatorColor = Color.Gray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        plans.forEach { plan ->
                            DropdownMenuItem(
                                text = { Text(plan) },
                                onClick = {
                                    selectedPlan = plan
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(workoutDays, key = { it.day }) { originalWorkoutDay ->
                        val day = originalWorkoutDay.day
                        val exercises = currentPlanExercises[day] ?: originalWorkoutDay.exercises

                        WorkoutDayCard(
                            workoutDay = WorkoutDay(
                                day,
                                originalWorkoutDay.focus,
                                exercises.toMutableList()
                            ),
                            onEditClick = { editingDay = day },
                            onExercisesChanged = { newExercises ->
                                viewModel.updateSelectedExercises(
                                    selectedPlan,
                                    day,
                                    newExercises
                                )
                            },
                            onSaveClick = {
                                viewModel.saveWorkoutPlanToDb(selectedPlan)
                            }
                        )
                    }

                }
            }
        }

        if (editingDay != null) {
            val originalWorkoutDay = workoutDays.find { it.day == editingDay }
            if (originalWorkoutDay != null) {
                val currentExercises =
                    currentPlanExercises[editingDay] ?: originalWorkoutDay.exercises
                EditExercisesDialog(
                    workoutDay = WorkoutDay(
                        editingDay!!,
                        originalWorkoutDay.focus,
                        currentExercises.toMutableList()
                    ),
                    onDismiss = { editingDay = null },
                    onSave = { updatedExercises ->
                        viewModel.updateSelectedExercises(
                            selectedPlan,
                            editingDay!!,
                            updatedExercises
                        )
                        editingDay = null
                    },
                    sharedViewModel = viewModel
                )
            }
        }
    }
}
