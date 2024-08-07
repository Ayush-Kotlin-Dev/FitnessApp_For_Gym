package presentation.screens.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel

data class ExerciseDetailScreen(
    val exerciseName: String,
    val onBackClick: () -> Unit
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val exerciseViewModel = koinScreenModel<ExerciseViewModel>()
        val exerciseDetails by exerciseViewModel.exerciseDetailsFlow.collectAsState()

        LaunchedEffect(exerciseName) {
            exerciseViewModel.loadExerciseDetails(exerciseName)
        }

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

            ExerciseDetailItem("Description", exerciseDetails.description)
            ExerciseDetailItem("Muscle Group", exerciseDetails.muscleGroup)
            ExerciseDetailItem("Equipment", exerciseDetails.equipment)

            Text(
                text = "Last Week's Performance",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            var weightText by remember { mutableStateOf(exerciseDetails.lastWeekWeight.toString()) }
            var repsText by remember { mutableStateOf(exerciseDetails.lastWeekReps.toString()) }
            var setsText by remember { mutableStateOf(exerciseDetails.lastWeekSets.toString()) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Weight:", Modifier.width(80.dp))
                TextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Text("kg", Modifier.padding(start = 8.dp))
                Button(
                    onClick = {
                        weightText.toDoubleOrNull()?.let {
                            exerciseViewModel.updateWeight(exerciseName, it)
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Update")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Reps:", Modifier.width(80.dp))
                TextField(
                    value = repsText,
                    onValueChange = { repsText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        repsText.toIntOrNull()?.let {
                            exerciseViewModel.updateReps(exerciseName, it)
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Update")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sets:", Modifier.width(80.dp))
                TextField(
                    value = setsText,
                    onValueChange = { setsText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        setsText.toIntOrNull()?.let {
                            exerciseViewModel.updateSets(exerciseName, it)
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Update")
                }
            }

            Button(
                onClick = { exerciseViewModel.resetExerciseStats(exerciseName) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Reset Stats")
            }

            Text(
                text = "Progress Graph",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            // TODO: Add a graph component to show progress over time

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