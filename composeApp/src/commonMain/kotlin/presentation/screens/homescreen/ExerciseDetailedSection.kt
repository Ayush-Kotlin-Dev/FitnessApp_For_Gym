package presentation.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import data.models.Exercise
import presentation.screens.plans.AccentColor
import presentation.screens.plans.CardBackgroundColor
import util.getCurrentDay

data class ExerciseDetailScreen(
    val exerciseName: String,
    val onBackClick: () -> Unit
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val exerciseViewModel = koinScreenModel<ExerciseViewModel>()
        val exerciseDetails by exerciseViewModel.exerciseDetailsFlow.collectAsState()
        val dayName = getCurrentDay()
        val currentPlanName by exerciseViewModel.getSelectedRoutineFlow()
            .collectAsState(initial = null)
        LaunchedEffect(exerciseName) {
            exerciseViewModel.loadExerciseDetails(exerciseName)
        }
        val isLoading by exerciseViewModel.isLoading.collectAsState()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = exerciseName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = fadeOut()
            ) {
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)

                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Exercise Details",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            ExerciseDetailItem("Description", exerciseDetails.description)
                            ExerciseDetailItem("Muscle Group", exerciseDetails.muscleGroup)
                            ExerciseDetailItem("Equipment", exerciseDetails.equipment)
                        }
                    }

//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp),
//                        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
//
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                text = "Last Week's Performance",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.padding(bottom = 8.dp)
//                            )
//                            PerformanceMetric("Weight", exerciseDetails.lastWeekWeight, exerciseDetails.lastWeekWeight, "kg")
//                            PerformanceMetric("Reps", exerciseDetails.lastWeekReps.toDouble(), exerciseDetails.lastWeekReps .toDouble(), "")
//                            PerformanceMetric("Sets", exerciseDetails.lastWeekSets.toDouble(), exerciseDetails.lastWeekSets .toDouble(), "")
//                        }
//                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)

                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Previous Performance",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            UpdatePerformanceFields(exerciseViewModel, exerciseDetails, currentPlanName, dayName, exerciseName)
                        }
                    }

                    Button(
                        onClick = { exerciseViewModel.resetExerciseStats(exerciseName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Reset Stats")
                    }


                    Button(
                        onClick = { /* TODO: Implement start exercise functionality */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text("Start Exercise")
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        Text(text = value)
    }
}

//@Composable
//fun PerformanceMetric(label: String, currentValue: Double, previousValue: Double, unit: String) {
//    Column(modifier = Modifier.padding(vertical = 4.dp)) {
//        Text("$label: $currentValue $unit")
//        LinearProgressIndicator(
//            progress = { calculateProgress(currentValue, previousValue) },
//            modifier = Modifier.fillMaxWidth(),
//        )
//        Text(
//            text = if (currentValue > previousValue) "Improved by ${currentValue - previousValue} $unit"
//            else if (currentValue < previousValue) "Decreased by ${previousValue - currentValue} $unit"
//            else "No change",
//            style = MaterialTheme.typography.bodySmall,
//            color = when {
//                currentValue > previousValue -> Color.Green
//                currentValue < previousValue -> AccentColor
//                else -> Color.Gray
//            }
//        )
//    }
//    // TODO: Add a graph component to show progress over time
//
//
//}

//fun calculateProgress(current: Double, previous: Double): Float {
//    return if (previous == 0.0) 0f else (current / previous).coerceIn(0.0, 1.0).toFloat()
//}

@Composable
fun UpdatePerformanceFields(
    exerciseViewModel: ExerciseViewModel,
    exerciseDetails: Exercise,
    currentPlanName: String?,
    dayName: String,
    exerciseName: String
) {
    var weightText by remember { mutableStateOf(exerciseDetails.lastWeekWeight.toString()) }
    var repsText by remember { mutableStateOf(exerciseDetails.lastWeekReps.toString()) }
    var setsText by remember { mutableStateOf(exerciseDetails.lastWeekSets.toString()) }

    PerformanceUpdateField("Weight", weightText, "kg") { newWeight ->
        weightText = newWeight
        newWeight.toDoubleOrNull()?.let {
            exerciseViewModel.updateWeight(currentPlanName!!, dayName, exerciseName, it)
        }
    }

    PerformanceUpdateField("Reps", repsText, "") { newReps ->
        repsText = newReps
        newReps.toIntOrNull()?.let {
            exerciseViewModel.updateReps(currentPlanName!!, dayName, exerciseName, it)
        }
    }

    PerformanceUpdateField("Sets", setsText, "") { newSets ->
        setsText = newSets
        newSets.toIntOrNull()?.let {
            exerciseViewModel.updateSets(currentPlanName!!, dayName, exerciseName, it)
        }
    }
}

@Composable
fun PerformanceUpdateField(label: String, value: String, unit: String, onUpdate: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label:", Modifier.width(80.dp))
        TextField(
            value = value,
            onValueChange = { onUpdate(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        if (unit.isNotEmpty()) {
            Text(unit, Modifier.padding(start = 8.dp))
        }
    }
}