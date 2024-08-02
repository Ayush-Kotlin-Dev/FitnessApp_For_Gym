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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import data.models.WorkoutDayDb
import presentation.screens.tabs.SharedWorkoutViewModel


data class WorkoutDay(
    val day: String,
    val focus: String,
    val exercises: MutableList<String>
)

@Composable
fun WorkoutDayCard(
    workoutDay: WorkoutDayDb,
    onEditClick: () -> Unit,
    onExercisesChanged: (List<String>) -> Unit,
    onSaveClick: () -> Unit
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
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Green.copy(alpha = 0.9f)
                    ),
                    border = BorderStroke(1.dp, Color.Green.copy(alpha = 0.5f))
                ) {
                    Text("Save")
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
            ){
                Column {
                    workoutDay.exercises.forEach { exercise ->
                        Text("• $exercise", color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
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
    var isSearching by remember { mutableStateOf(false) }

    val groupedExercises = remember {
        when (workoutDay.focus) {
            "Chest and Triceps" -> listOf(
                "Chest" to chestExercises,
                "Triceps" to tricepsExercises
            )
            "Back and Biceps" -> listOf(
                "Back" to backExercises,
                "Biceps" to bicepsExercises
            )
            "Legs" -> listOf("Legs" to legExercises)
            "Shoulders and Forearms" -> listOf(
                "Shoulders" to shoulderExercises,
                "Forearms" to forearmExercises
            )
            "Arms (Biceps and Triceps)" -> listOf(
                "Biceps" to bicepsExercises,
                "Triceps" to tricepsExercises
            )
            "Abs" -> listOf("Abs" to abdominalExercises)
            "Chest" -> listOf("Chest" to chestExercises)
            "Back" -> listOf("Back" to backExercises)
            "Shoulders" -> listOf("Shoulders" to shoulderExercises)
            "Arms" -> listOf(
                "Biceps" to bicepsExercises,
                "Triceps" to tricepsExercises
            )
            "Full Body" -> listOf(
                "Chest" to chestExercises,
                "Back" to backExercises,
                "Legs" to legExercises,
                "Shoulders" to shoulderExercises,
                "Biceps" to bicepsExercises,
                "Triceps" to tricepsExercises
            )
            "Chest, Shoulders, and Triceps" -> listOf(
                "Chest" to chestExercises,
                "Shoulders" to shoulderExercises,
                "Triceps" to tricepsExercises
            )
            "Chest and Back" -> listOf(
                "Chest" to chestExercises,
                "Back" to backExercises
            )
            "Shoulders and Arms" -> listOf(
                "Shoulders" to shoulderExercises,
                "Biceps" to bicepsExercises,
                "Triceps" to tricepsExercises
            )
            else -> emptyList()
        }
    }

    val filteredExercises = remember(searchQuery, groupedExercises) {
        if (searchQuery.isEmpty()) {
            groupedExercises
        } else {
            groupedExercises.map { (muscleGroup, exercises) ->
                muscleGroup to exercises.filter { it.contains(searchQuery, ignoreCase = true) }
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
                        colors = TextFieldDefaults.outlinedTextFieldColors(
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
                                        label = { Text(exercise, color = Color.White) },
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
                    sharedViewModel.saveExercisesToDb(planName, workoutDay.day, selectedExercises)
                    onSave()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Save", color = Color.White)
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
val chestExercises = listOf(
    "Barbell Bench Press",
    "Incline Dumbbell Bench Press",
    "Decline Dumbbell Bench Press",
    "Push-Ups",
    "Diamond Push-Ups",
    "Dips (Chest Version)",
    "Cable Flyes",
    "Dumbbell Flyes",
    "Pec Deck Machine",
    "Smith Machine Bench Press",
    "Single-Arm Dumbbell Bench Press",
    "Chest Dips",
    "Hammer Strength Chest Press",
    "Dumbbell Pullover",
    "Cable Crossover",
    "Incline Cable Flyes",
    "Decline Barbell Bench Press"
)
val tricepsExercises = listOf(
    "Close-Grip Bench Press",
    "Tricep Pushdowns",
    "Overhead Tricep Extension",
    "Dips",
    "Skull Crushers",
    "Rope Pushdowns",
    "Bench Dips",
    "Single-Arm Tricep Extensions",
    "Tricep Push-Ups",
    "Reverse Grip Tricep Pushdowns",
    "Lying Tricep Extensions",
    "Overhead Cable Tricep Extensions",
    "Close-Grip Push-Ups",
    "Tricep Dumbbell Pullover"
)
val backExercises = listOf(
    "Deadlifts",
    "Bent-Over Rows",
    "Lat Pulldowns",
    "T-Bar Rows",
    "Single-Arm Dumbbell Rows",
    "Seated Cable Rows",
    "Face Pulls",
    "Chin-Ups",
    "Hyperextensions",
    "Inverted Rows",
    "Dumbbell Pullovers",
    "Cable Pullovers",
    "Low Rows",
    "Barbell Shrugs",
    "Dumbbell Shrugs",
    "Reverse Flyes",
)
val bicepsExercises = listOf(
    "Barbell Curls",
    "Dumbbell Curls",
    "Hammer Curls",
    "Preacher Curls",
    "Incline Dumbbell Curls",
    "Concentration Curls",
    "EZ-Bar Curls",
    "Cable Curls",
    "Cross-Body Hammer Curls",
    "Rope Hammer Curls",
    "Alternating Dumbbell Curls",
    "Seated Incline Curls",
    "Standing Cable Curls",
    "Bicep Curl to Press"
)
val legExercises = listOf(
    "Back Squats",
    "Barbell Squats",
    "Leg Press",
    "Lunges",
    "Walking Lunges",
    "Step-Ups",
    "Leg Extensions",
    "Leg Curls",
    "Calf Raises",
    "Seated Calf Raises",
    "Bulgarian Split Squats",
    "Goblet Squats",
    "Hack Squats",
    "Box Jumps",
    "Pistol Squats",
    "Sumo Squats",
    "Glute Bridges",
    "Hip Thrusts",
    "Leg Press Calf Raises",
    "Wall Sits",
    "Sled Pushes",
    "Jump Squats",
    "Farmer's Walks",
    "Barbell Hip Thrusts",
    "Reverse Lunges",
    "Side Lunges",
    "Curtsy Lunges",
    "Smith Machine Squats",
    "Dumbbell Squats",
    "Kettlebell Swings"
)
val shoulderExercises = listOf(
    "Overhead Press",
    "Military Press",
    "Dumbbell Shoulder Press",
    "Arnold Press",
    "Lateral Raises",
    "Front Raises",
    "Reverse Flyes",
    "Face Pulls",
    "Upright Rows",
    "Shrugs",
    "Cable Lateral Raises",
    "Machine Shoulder Press",
    "Bent-Over Lateral Raises",
    "Plate Front Raises",
    "Seated Dumbbell Press",
    "Behind-the-Neck Press",
    "Dumbbell Shoulder Circles",
    "Bradford Press",
    "Single-Arm Dumbbell Press",
    "Kettlebell Upright Rows",
    "Dumbbell Y-Raises",
    "Battling Ropes",
    "Cable Face Pulls",
    "Resistance Band Lateral Raises",
)
val forearmExercises = listOf(
    "Wrist Curls",
    "Reverse Wrist Curls",
    "Barbell Hold",
    "Dumbbell Hold",
    "Grip Strengthener",
    "Plate Rotations",
    "Dead Hangs",
    "Forearm Roller",
    "Dumbbell Wrist Rotations",
    "Suitcase Carries",
    "Bottoms-Up Kettlebell Hold",
    "Cable Wrist Curls"
)
val abdominalExercises = listOf(
    "Crunches",
    "Sit-Ups",
    "Planks",
    "Russian Twists",
    "Leg Raises",
    "Dead Bug",
    "Mountain Climbers",
    "Hanging Leg Raises",
    "Ab Wheel Rollouts",
    "Cable Crunches",
    "Pallof Press",
    "Side Planks",
    "Woodchoppers",
    "Dragon Flags",
    "Hollow Body Hold",
    "Reverse Crunches",
    "Flutter Kicks",
    "Plank to Push-Up",
    "Medicine Ball Slams",
    "Windshield Wipers",
    "Toe Touches",
    "V-Ups",
    "L-Sit Hold",
    "Decline Bench Sit-Ups"
)