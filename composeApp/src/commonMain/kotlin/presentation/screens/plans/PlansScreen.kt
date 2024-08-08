package presentation.screens.plans

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
        var selectedPlan by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        var editingDay by remember { mutableStateOf<String?>(null) }
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val currentWorkoutPlan by viewModel.currentWorkoutPlan.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getSelectedRoutineFlow().collect { routineName ->
                selectedPlan = routineName ?: ""
                if (routineName != null) {
                    viewModel.loadWorkoutPlanFromDb(routineName)
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Workout Plan") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.7f),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    }

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
                                    viewModel.saveSelectedRoutine(plan)
                                    viewModel.loadWorkoutPlanFromDb(plan)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.saveWorkoutPlanToDb(selectedPlan)
                        viewModel.saveLastSelectedPlan(selectedPlan)
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Save Routine")
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currentWorkoutPlan?.days?.forEach { workoutDay ->
                        item(key = workoutDay.day) {
                            WorkoutDayCard(
                                workoutDay = workoutDay,
                                onEditClick = { editingDay = workoutDay.day },
                                onExercisesChanged = { newExercises ->
                                    viewModel.updateSelectedExercises(
                                        selectedPlan,
                                        workoutDay.day,
                                        newExercises
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        if (editingDay != null) {
            val workoutDay = currentWorkoutPlan?.days?.find { it.day == editingDay }
            if (workoutDay != null) {
                EditExercisesDialog(
                    workoutDay = WorkoutDay(
                        editingDay!!,
                        workoutDay.focus,
                        workoutDay.exerciseDbs.toMutableList()
                    ),
                    onDismiss = { editingDay = null },
                    onSave = {
                        viewModel.reloadCurrentWorkoutPlan()
                        editingDay = null
                    },
                    sharedViewModel = viewModel,
                    planName = selectedPlan
                )
            }
        }
    }
}