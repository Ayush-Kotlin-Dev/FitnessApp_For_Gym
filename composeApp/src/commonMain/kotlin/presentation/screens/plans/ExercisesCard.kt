package presentation.screens.plans

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.models.Exercise
import presentation.screens.tabs.ExerciseListState
import presentation.screens.tabs.SharedWorkoutViewModel


data class WorkoutDay(
    val day: String,
    val focus: String,
    val exercises: MutableList<Exercise>
)

@Composable
fun WorkoutDayCard(
    workoutDay: WorkoutDay,
    onEditClick: () -> Unit,
    onExercisesChanged: (List<String>) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.7f),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workoutDay.day,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
            Text(
                text = workoutDay.focus,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red.copy(alpha = 0.9f)
                    ),
                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
                ) {
                    Text(if (expanded) "Hide" else "View")
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red.copy(alpha = 0.9f)
                    ),
                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
                ) {
                    Text("Edit")
                }
            }
            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 800)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 600)
                )
            ) {
                Column {
                    workoutDay.exercises.forEach { exercise ->
                        Text("• ${exercise.name}", color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun EditExercisesDialog(
    workoutDay: WorkoutDay,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    sharedViewModel: SharedWorkoutViewModel,
    planName: String
) {
    val selectedExercises = remember { workoutDay.exercises.toMutableStateList() }
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) } //TODO implement search from Server
    val exerciseListState by sharedViewModel.exerciseListState.collectAsState()

    when (val state = exerciseListState) {
        is ExerciseListState.Loading -> {
            // Show loading indicator
            CircularProgressIndicator()
        }

        is ExerciseListState.Error -> {
            // Show error message
            Text("Error loading exercises: ${state.message}")
        }

        is ExerciseListState.Loaded -> {
            // Proceed with the dialog content
            val exerciseList = state.exercises

            val groupedExercises = remember(exerciseList) {
                when (workoutDay.focus) {
                    "Chest and Triceps" -> listOf(
                        "Chest" to exerciseList.chestExercises,
                        "Triceps" to exerciseList.tricepsExercises
                    )

                    "Back and Biceps" -> listOf(
                        "Back" to exerciseList.backExercises,
                        "Biceps" to exerciseList.bicepsExercises
                    )

                    "Legs" -> listOf("Legs" to exerciseList.legExercises)
                    "Shoulders and Forearms" -> listOf(
                        "Shoulders" to exerciseList.shoulderExercises,
                        "Forearms" to exerciseList.forearmExercises
                    )

                    "Arms (Biceps and Triceps)" -> listOf(
                        "Biceps" to exerciseList.bicepsExercises,
                        "Triceps" to exerciseList.tricepsExercises
                    )

                    "Abs" -> listOf("Abs" to exerciseList.abdominalExercises)
                    "Chest" -> listOf("Chest" to exerciseList.chestExercises)
                    "Back" -> listOf("Back" to exerciseList.backExercises)
                    "Shoulders" -> listOf("Shoulders" to exerciseList.shoulderExercises)
                    "Arms" -> listOf(
                        "Biceps" to exerciseList.bicepsExercises,
                        "Triceps" to exerciseList.tricepsExercises
                    )

                    "Full Body" -> listOf(
                        "Chest" to exerciseList.chestExercises,
                        "Back" to exerciseList.backExercises,
                        "Shoulders" to exerciseList.shoulderExercises,
                        "Biceps" to exerciseList.bicepsExercises,
                        "Triceps" to exerciseList.tricepsExercises,
                        "Legs" to exerciseList.legExercises,
                    )

                    "Chest, Shoulders, and Triceps" -> listOf(
                        "Chest" to exerciseList.chestExercises,
                        "Shoulders" to exerciseList.shoulderExercises,
                        "Triceps" to exerciseList.tricepsExercises
                    )

                    "Chest and Back" -> listOf(
                        "Chest" to exerciseList.chestExercises,
                        "Back" to exerciseList.backExercises
                    )

                    "Shoulders and Arms" -> listOf(
                        "Shoulders" to exerciseList.shoulderExercises,
                        "Biceps" to exerciseList.bicepsExercises,
                        "Triceps" to exerciseList.tricepsExercises
                    )

                    else -> emptyList()
                }
            }

            val filteredExercises = remember(searchQuery, groupedExercises) {
                if (searchQuery.isEmpty()) {
                    groupedExercises
                } else {
                    groupedExercises.map { (muscleGroup, exercises) ->
                        muscleGroup to exercises.filter {
                            it.name.contains(
                                searchQuery,
                                ignoreCase = true
                            )
                        }
                    }.filter { (_, exercises) -> exercises.isNotEmpty() }
                }
            }

            AlertDialog(
                containerColor = Color.Black.copy(alpha = 0.7f),
                onDismissRequest = onDismiss,
                title = { Text("Edit Exercises for ${workoutDay.day}", color = Color.White) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search exercises", color = Color.White) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White,
                                focusedBorderColor = Color.Red,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color.Red,
                                unfocusedLabelColor = Color.Gray
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear search",
                                        tint = Color.White
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn {
                            filteredExercises.forEach { (muscleGroup, exercises) ->
                                stickyHeader {
                                    Surface(
                                        color = Color.DarkGray.copy(alpha = 0.9f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp, horizontal = 16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = muscleGroup,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                }
                                item {
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        exercises.forEach { exercise ->
                                            FilterChip(
                                                selected = exercise in selectedExercises,
                                                onClick = {
                                                    if (exercise in selectedExercises) {
                                                        selectedExercises.remove(exercise)
                                                    } else {
                                                        selectedExercises.add(exercise)
                                                    }
                                                },
                                                label = {
                                                    Text(
                                                        exercise.name,
                                                        color = Color.White
                                                    )
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = Color.Red.copy(alpha = 0.5f),
                                                    selectedLabelColor = Color.White
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            sharedViewModel.saveExercisesToDb(
                                planName,
                                workoutDay.day,
                                selectedExercises
                            )
                            onSave()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Save", color = AccentColor)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                }
            )
        }
    }
}
