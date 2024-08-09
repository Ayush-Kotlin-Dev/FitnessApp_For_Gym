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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import data.models.Exercise
import data.models.WorkoutDayDb
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
            ){
                Column {
                    workoutDay.exercises.forEach { exercise ->
                        Text("• ${exercise.name}", color = Color.White.copy(alpha = 0.9f))
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
    var isSearching by remember { mutableStateOf(false) } //TODO implement search from Server

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
                muscleGroup to exercises.filter { it.name.contains(searchQuery, ignoreCase = true) }
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
                                        label = { Text(exercise.name, color = Color.White) },
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
val chestExercises = listOf(
    Exercise(name = "Barbell Bench Press", description = "Compound exercise that targets the chest, shoulders, and triceps.", muscleGroup = "Chest", equipment = "Barbell"),
    Exercise(name = "Incline Dumbbell Bench Press", description = "Compound exercise that targets the upper chest.", muscleGroup = "Chest", equipment = "Dumbbells"),
    Exercise(name = "Decline Dumbbell Bench Press", description = "Compound exercise that targets the lower chest.", muscleGroup = "Chest", equipment = "Dumbbells"),
    Exercise(name = "Push-Ups", description = "Bodyweight exercise that targets the chest, shoulders, and triceps.", muscleGroup = "Chest", equipment = "Bodyweight"),
    Exercise(name = "Diamond Push-Ups", description = "Bodyweight exercise that targets the inner chest and triceps.", muscleGroup = "Chest", equipment = "Bodyweight"),
    Exercise(name = "Dips (Chest Version)", description = "Compound exercise that targets the lower chest and triceps.", muscleGroup = "Chest", equipment = "Parallel Bars"),
    Exercise(name = "Cable Flyes", description = "Isolation exercise that targets the chest muscles.", muscleGroup = "Chest", equipment = "Cable Machine"),
    Exercise(name = "Dumbbell Flyes", description = "Isolation exercise that targets the chest muscles.", muscleGroup = "Chest", equipment = "Dumbbells"),
    Exercise(name = "Pec Deck Machine", description = "Isolation exercise that targets the chest muscles.", muscleGroup = "Chest", equipment = "Pec Deck Machine"),
    Exercise(name = "Smith Machine Bench Press", description = "Compound exercise that targets the chest, shoulders, and triceps.", muscleGroup = "Chest", equipment = "Smith Machine"),
    Exercise(name = "Single-Arm Dumbbell Bench Press", description = "Unilateral exercise that targets the chest, shoulders, and triceps.", muscleGroup = "Chest", equipment = "Dumbbell"),
    Exercise(name = "Chest Dips", description = "Compound exercise that targets the lower chest and triceps.", muscleGroup = "Chest", equipment = "Parallel Bars"),
    Exercise(name = "Hammer Strength Chest Press", description = "Compound exercise that targets the chest, shoulders, and triceps.", muscleGroup = "Chest", equipment = "Hammer Strength Machine"),
    Exercise(name = "Dumbbell Pullover", description = "Compound exercise that targets the chest and lats.", muscleGroup = "Chest", equipment = "Dumbbell"),
    Exercise(name = "Cable Crossover", description = "Isolation exercise that targets the chest muscles.", muscleGroup = "Chest", equipment = "Cable Machine"),
    Exercise(name = "Incline Cable Flyes", description = "Isolation exercise that targets the upper chest.", muscleGroup = "Chest", equipment = "Cable Machine"),
    Exercise(name = "Decline Barbell Bench Press", description = "Compound exercise that targets the lower chest.", muscleGroup = "Chest", equipment = "Barbell")
)
val backExercises = listOf(
    Exercise(
        name = "Deadlifts",
        description = "Compound exercise that targets the entire posterior chain, including the back, glutes, and hamstrings.",
        muscleGroup = "Back",
        equipment = "Barbell"
    ),
    Exercise(
        name = "Bent-Over Rows",
        description = "Compound exercise that targets the upper and middle back, as well as the lats and biceps.",
        muscleGroup = "Back",
        equipment = "Barbell or Dumbbells"
    ),
    Exercise(
        name = "Lat Pulldowns",
        description = "Isolation exercise that targets the latissimus dorsi muscles.",
        muscleGroup = "Back",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "T-Bar Rows",
        description = "Compound exercise that targets the middle back, lats, and biceps.",
        muscleGroup = "Back",
        equipment = "T-Bar Machine"
    ),
    Exercise(
        name = "Single-Arm Dumbbell Rows",
        description = "Isolation exercise that targets the lats and upper back.",
        muscleGroup = "Back",
        equipment = "Dumbbell"
    ),
    Exercise(
        name = "Seated Cable Rows",
        description = "Compound exercise that targets the middle back, lats, and biceps.",
        muscleGroup = "Back",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "Face Pulls",
        description = "Isolation exercise that targets the upper back and rear deltoids.",
        muscleGroup = "Back",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "Chin-Ups",
        description = "Bodyweight exercise that targets the lats, biceps, and upper back.",
        muscleGroup = "Back",
        equipment = "Pull-Up Bar"
    ),
    Exercise(
        name = "Hyperextensions",
        description = "Isolation exercise that targets the lower back, glutes, and hamstrings.",
        muscleGroup = "Back",
        equipment = "Hyperextension Bench"
    ),
    Exercise(
        name = "Inverted Rows",
        description = "Bodyweight exercise that targets the upper and middle back.",
        muscleGroup = "Back",
        equipment = "Smith Machine or Suspension Trainer"
    ),
    Exercise(
        name = "Dumbbell Pullovers",
        description = "Isolation exercise that targets the lats and chest.",
        muscleGroup = "Back",
        equipment = "Dumbbell"
    ),
    Exercise(
        name = "Cable Pullovers",
        description = "Isolation exercise that targets the lats and helps improve the mind-muscle connection.",
        muscleGroup = "Back",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "Low Rows",
        description = "Compound exercise that targets the middle back and lats.",
        muscleGroup = "Back",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "Barbell Shrugs",
        description = "Isolation exercise that targets the trapezius muscles.",
        muscleGroup = "Back",
        equipment = "Barbell"
    ),
    Exercise(
        name = "Dumbbell Shrugs",
        description = "Isolation exercise that targets the trapezius muscles.",
        muscleGroup = "Back",
        equipment = "Dumbbells"
    ),
    Exercise(
        name = "Reverse Flyes",
        description = "Isolation exercise that targets the rear deltoids and upper back.",
        muscleGroup = "Back",
        equipment = "Dumbbells or Cable Machine"
    )
)
val tricepsExercises = listOf(
    Exercise("Close-Grip Bench Press", "This is a close-grip bench press exercise", "Triceps", "Barbell"),
    Exercise("Tricep Pushdowns", "This is a tricep pushdowns exercise", "Triceps", "Cables or Tricep Pushdown Machine"),
    Exercise("Overhead Tricep Extension", "This is an overhead tricep extension exercise", "Triceps", "Dumbbells or Barbell"),
    Exercise("Dips", "This is a dips exercise", "Triceps", "Parallel Bars or Dips Machine"),
    Exercise("Skull Crushers", "This is a skull crushers exercise", "Triceps", "Dumbbells"),
    Exercise("Rope Pushdowns", "This is a rope pushdowns exercise", "Triceps", "Cables or Rope Pushdown Machine"),
    Exercise("Bench Dips", "This is a bench dips exercise", "Triceps", "Bench or Dips Machine"),
    Exercise("Single-Arm Tricep Extensions", "This is a single-arm tricep extensions exercise", "Triceps", "Dumbbells"),
    Exercise("Tricep Push-Ups", "This is a tricep push-ups exercise", "Triceps", "No equipment"),
    Exercise("Reverse Grip Tricep Pushdowns", "This is a reverse grip tricep pushdowns exercise", "Triceps", "Cables or Tricep Pushdown Machine"),
    Exercise("Lying Tricep Extensions", "This is a lying tricep extensions exercise", "Triceps", "Dumbbells"),
    Exercise("Overhead Cable Tricep Extensions", "This is an overhead cable tricep extensions exercise", "Triceps", "Cables"),
    Exercise("Close-Grip Push-Ups", "This is a close-grip push-ups exercise", "Triceps", "No equipment"),
    Exercise("Tricep Dumbbell Pullover", "This is a tricep dumbbell pullover exercise", "Triceps", "Dumbbells")
)
val bicepsExercises = listOf(
    Exercise(
        name = "Barbell Curls",
        description = "Isolation exercise that targets the biceps.",
        muscleGroup = "Biceps",
        equipment = "Barbell"
    ),
    Exercise(
        name = "Dumbbell Curls",
        description = "Isolation exercise that targets the biceps, allowing for unilateral training.",
        muscleGroup = "Biceps",
        equipment = "Dumbbells"
    ),
    Exercise(
        name = "Hammer Curls",
        description = "Isolation exercise that targets the biceps and brachialis.",
        muscleGroup = "Biceps",
        equipment = "Dumbbells"
    ),
    Exercise(
        name = "Preacher Curls",
        description = "Isolation exercise that targets the biceps with support for strict form.",
        muscleGroup = "Biceps",
        equipment = "EZ Curl Bar or Dumbbell"
    ),
    Exercise(
        name = "Incline Dumbbell Curls",
        description = "Isolation exercise that targets the biceps, emphasizing the stretch at the bottom of the movement.",
        muscleGroup = "Biceps",
        equipment = "Dumbbells"
    ),
    Exercise(
        name = "Concentration Curls",
        description = "Isolation exercise that targets the biceps with a focus on peak contraction.",
        muscleGroup = "Biceps",
        equipment = "Dumbbell"
    ),
    Exercise(
        name = "EZ-Bar Curls",
        description = "Isolation exercise that targets the biceps with a more comfortable grip.",
        muscleGroup = "Biceps",
        equipment = "EZ Curl Bar"
    ),
    Exercise(
        name = "Cable Curls",
        description = "Isolation exercise that targets the biceps with constant tension throughout the range of motion.",
        muscleGroup = "Biceps",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "Cross-Body Hammer Curls",
        description = "Isolation exercise that targets the biceps and brachialis with an emphasis on the forearms.",
        muscleGroup = "Biceps",
        equipment = "Dumbbells"
    ),
    Exercise(
        name = "Rope Hammer Curls",
        description = "Isolation exercise that targets the biceps and brachialis with a rope attachment for a neutral grip.",
        muscleGroup = "Biceps",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "Alternating Dumbbell Curls",
        description = "Isolation exercise that targets the biceps with unilateral movement.",
        muscleGroup = "Biceps",
        equipment = "Dumbbells"
    ),
    Exercise(
        name = "Seated Incline Curls",
        description = "Isolation exercise that targets the biceps with an incline bench to increase the stretch.",
        muscleGroup = "Biceps",
        equipment = "Dumbbells"
    ),
    Exercise(
        name = "Standing Cable Curls",
        description = "Isolation exercise that targets the biceps with constant tension provided by the cable machine.",
        muscleGroup = "Biceps",
        equipment = "Cable Machine"
    ),
    Exercise(
        name = "Bicep Curl to Press",
        description = "Combination exercise that targets the biceps and shoulders in a single movement.",
        muscleGroup = "Biceps",
        equipment = "Dumbbells"
    )
)

val legExercises = listOf(
    Exercise("Back Squats", "This is a compound exercise", "Legs", "No equipment"),
    Exercise("Barbell Squats", "Another compound exercise", "Legs", "Barbell"),
    Exercise("Leg Press", "This is a leg press exercise", "Legs", "Leg Press Machine"),
    Exercise("Lunges", "This is a lunges exercise", "Legs", "No equipment"),
    Exercise("Walking Lunges", "This is a walking lunges exercise", "Legs", "No equipment"),
    Exercise("Step-Ups", "This is a step-ups exercise", "Legs", "No equipment"),
    Exercise("Leg Extensions", "This is a leg extensions exercise", "Legs", "Leg Press Machine"),
    Exercise("Leg Curls", "This is a leg curls exercise", "Legs", "Leg Press Machine"),
    Exercise("Calf Raises", "This is a calf raises exercise", "Legs", "No equipment"),
    Exercise("Seated Calf Raises", "This is a seated calf raises exercise", "Legs", "No equipment"),
    Exercise("Bulgarian Split Squats", "This is a bulgarian split squats exercise", "Legs", "No equipment"),
    Exercise("Goblet Squats", "This is a goblet squats exercise", "Legs", "Goblet"),
    Exercise("Hack Squats", "This is a hack squats exercise", "Legs", "Hack Squat Machine"),
    Exercise("Box Jumps", "This is a box jumps exercise", "Legs", "Box"),
    Exercise("Pistol Squats", "This is a pistol squats exercise", "Legs", "No equipment"),
    Exercise("Sumo Squats", "This is a sumo squats exercise", "Legs", "No equipment"),
    Exercise("Glute Bridges", "This is a glute bridges exercise", "Glutes", "No equipment"),
    Exercise("Hip Thrusts", "This is a hip thrusts exercise", "Glutes", "No equipment"),
    Exercise("Leg Press Calf Raises", "This is a leg press calf raises exercise", "Legs", "Leg Press Machine"),
    Exercise("Wall Sits", "This is a wall sits exercise", "Legs", "No equipment"),
    Exercise("Sled Pushes", "This is a sled pushes exercise", "Legs", "Sled"),
    Exercise("Jump Squats", "This is a jump squats exercise", "Legs", "No equipment"),
    Exercise("Farmer's Walks", "This is a farmer's walks exercise", "Legs", "Dumbbells"),
    Exercise("Barbell Hip Thrusts", "This is a barbell hip thrusts exercise", "Glutes", "Barbell"),
    Exercise("Reverse Lunges", "This is a reverse lunges exercise", "Legs", "No equipment"),
    Exercise("Side Lunges", "This is a side lunges exercise", "Legs", "No equipment"),
    Exercise("Curtsy Lunges", "This is a curtsy lunges exercise", "Legs", "No equipment"),
    Exercise("Smith Machine Squats", "This is a smith machine squats exercise", "Legs", "Smith Machine"),
    Exercise("Dumbbell Squats", "This is a dumbbell squats exercise", "Legs", "Dumbbells"),
    Exercise("Kettlebell Swings", "This is a kettlebell swings exercise", "Legs", "Kettlebell")
)
val shoulderExercises = listOf(
    Exercise("Overhead Press", "This is an overhead press exercise", "Shoulders", "Barbell or Dumbbells"),
    Exercise("Military Press", "This is a military press exercise", "Shoulders", "Barbell or Dumbbells"),
    Exercise("Dumbbell Shoulder Press", "This is a dumbbell shoulder press exercise", "Shoulders", "Dumbbells"),
    Exercise("Arnold Press", "This is an arnold press exercise", "Shoulders", "Barbell or Dumbbells"),
    Exercise("Lateral Raises", "This is a lateral raises exercise", "Shoulders", "Dumbbells or Cables"),
    Exercise("Front Raises", "This is a front raises exercise", "Shoulders", "Dumbbells or Cables"),
    Exercise("Reverse Flyes", "This is a reverse flyes exercise", "Shoulders", "Cables or Resistance Bands"),
    Exercise("Face Pulls", "This is a face pulls exercise", "Shoulders", "Cables"),
    Exercise("Upright Rows", "This is an upright rows exercise", "Shoulders", "Dumbbells or Cables"),
    Exercise("Shrugs", "This is a shrugs exercise", "Shoulders", "Dumbbells or Cables"),
    Exercise("Cable Lateral Raises", "This is a cable lateral raises exercise", "Shoulders", "Cables"),
    Exercise("Machine Shoulder Press", "This is a machine shoulder press exercise", "Shoulders", "Machine"),
    Exercise("Bent-Over Lateral Raises", "This is a bent-over lateral raises exercise", "Shoulders", "Dumbbells or Cables"),
    Exercise("Plate Front Raises", "This is a plate front raises exercise", "Shoulders", "Dumbbells or Cables"),
    Exercise("Seated Dumbbell Press", "This is a seated dumbbell press exercise", "Shoulders", "Dumbbells"),
    Exercise("Behind-the-Neck Press", "This is a behind-the-neck press exercise", "Shoulders", "Barbell or Dumbbells"),
    Exercise("Dumbbell Shoulder Circles", "This is a dumbbell shoulder circles exercise", "Shoulders", "Dumbbells"),
    Exercise("Bradford Press", "This is a bradford press exercise", "Shoulders", "Barbell or Dumbbells"),
    Exercise("Single-Arm Dumbbell Press", "This is a single-arm dumbbell press exercise", "Shoulders", "Dumbbells"),
    Exercise("Kettlebell Upright Rows", "This is a kettlebell upright rows exercise", "Shoulders", "Kettlebell"),
    Exercise("Dumbbell Y-Raises", "This is a dumbbell y-raises exercise", "Shoulders", "Dumbbells"),
    Exercise("Battling Ropes", "This is a battling ropes exercise", "Shoulders", "Battling Ropes"),
    Exercise("Cable Face Pulls", "This is a cable face pulls exercise", "Shoulders", "Cables"),
    Exercise("Resistance Band Lateral Raises", "This is a resistance band lateral raises exercise", "Shoulders", "Resistance Bands")
)
val forearmExercises = listOf(
    Exercise("Wrist Curls", "This is a wrist curls exercise", "Forearms", "Dumbbells or Wrist Curls Machine"),
    Exercise("Reverse Wrist Curls", "This is a reverse wrist curls exercise", "Forearms", "Dumbbells or Wrist Curls Machine"),
    Exercise("Barbell Hold", "This is a barbell hold exercise", "Forearms", "Barbell"),
    Exercise("Dumbbell Hold", "This is a dumbbell hold exercise", "Forearms", "Dumbbells"),
    Exercise("Grip Strengthener", "This is a grip strengthener exercise", "Forearms", "Grip Strengthener"),
    Exercise("Plate Rotations", "This is a plate rotations exercise", "Forearms", "Plate"),
    Exercise("Dead Hangs", "This is a dead hangs exercise", "Forearms", "Bar or Ring"),
    Exercise("Forearm Roller", "This is a forearm roller exercise", "Forearms", "Forearm Roller"),
    Exercise("Dumbbell Wrist Rotations", "This is a dumbbell wrist rotations exercise", "Forearms", "Dumbbells"),
    Exercise("Suitcase Carries", "This is a suitcase carries exercise", "Forearms", "Weight Plate or Kettlebell"),
    Exercise("Bottoms-Up Kettlebell Hold", "This is a bottoms-up kettlebell hold exercise", "Forearms", "Kettlebell"),
    Exercise("Cable Wrist Curls", "This is a cable wrist curls exercise", "Forearms", "Cables")
)
val abdominalExercises = listOf(
    Exercise("Crunches", "This is a crunches exercise", "Abdominals", "No equipment"),
    Exercise("Sit-Ups", "This is a sit-ups exercise", "Abdominals", "No equipment"),
    Exercise("Planks", "This is a planks exercise", "Abdominals", "No equipment"),
    Exercise("Russian Twists", "This is a russian twists exercise", "Abdominals", "Medicine Ball or Dumbbells"),
    Exercise("Leg Raises", "This is a leg raises exercise", "Abdominals", "No equipment"),
    Exercise("Dead Bug", "This is a dead bug exercise", "Abdominals", "No equipment"),
    Exercise("Mountain Climbers", "This is a mountain climbers exercise", "Abdominals", "No equipment"),
    Exercise("Hanging Leg Raises", "This is a hanging leg raises exercise", "Abdominals", "Pull-Up Bar"),
    Exercise("Ab Wheel Rollouts", "This is an ab wheel rollouts exercise", "Abdominals", "Ab Wheel"),
    Exercise("Cable Crunches", "This is a cable crunches exercise", "Abdominals", "Cables"),
    Exercise("Pallof Press", "This is a pallof press exercise", "Abdominals", "Cables or Resistance Bands"),
    Exercise("Side Planks", "This is a side planks exercise", "Abdominals", "No equipment"),
    Exercise("Woodchoppers", "This is a woodchoppers exercise", "Abdominals", "Medicine Ball or Dumbbells"),
    Exercise("Dragon Flags", "This is a dragon flags exercise", "Abdominals", "No equipment"),
    Exercise("Hollow Body Hold", "This is a hollow body hold exercise", "Abdominals", "No equipment"),
    Exercise("Reverse Crunches", "This is a reverse crunches exercise", "Abdominals", "No equipment"),
    Exercise("Flutter Kicks", "This is a flutter kicks exercise", "Abdominals", "No equipment"),
    Exercise("Plank to Push-Up", "This is a plank to push-up exercise", "Abdominals", "No equipment"),
    Exercise("Medicine Ball Slams", "This is a medicine ball slams exercise", "Abdominals", "Medicine Ball"),
    Exercise("Windshield Wipers", "This is a windshield wipers exercise", "Abdominals", "No equipment"),
    Exercise("Toe Touches", "This is a toe touches exercise", "Abdominals", "No equipment"),
    Exercise("V-Ups", "This is a v-ups exercise", "Abdominals", "No equipment"),
    Exercise("L-Sit Hold", "This is a l-sit hold exercise", "Abdominals", "No equipment"),
    Exercise("Decline Bench Sit-Ups", "This is a decline bench sit-ups exercise", "Abdominals", "Bench")
)