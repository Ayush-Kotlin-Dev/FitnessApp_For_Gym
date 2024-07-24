package presentation.screens.plans

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition

object PlansTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Plans"
            val icon = rememberVectorPainter(Icons.Default.ShoppingCart)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }


    @Composable
    override fun Content() {
        val sharedViewModel = koinScreenModel<SharedWorkoutViewModel>()
        Navigator(PlanSelectionScreen()){navigator ->
            SlideTransition( navigator)
        }
    }
}

data class WorkoutDay(
    val day: String,
    val focus: String,
    val exercises: MutableList<String>
)

@Composable
fun WorkoutDayCard(
    workoutDay: WorkoutDay,
    onEditClick: () -> Unit,
    onExercisesChanged: (List<String>) -> Unit
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
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                workoutDay.exercises.forEach { exercise ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text("•", color = Color.Red, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(exercise, color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditExercisesDialog(
    workoutDay: WorkoutDay,
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit,
    sharedViewModel: SharedWorkoutViewModel
) {

    val relevantExercises = remember {
        when (workoutDay.focus) {
            "Chest and Triceps" -> chestExercises + tricepsExercises
            "Back and Biceps" -> backExercises + bicepsExercises
            "Legs" -> legExercises
            "Shoulders and Forearms" -> shoulderExercises + forearmExercises
            "Arms (Biceps and Triceps)" -> bicepsExercises + tricepsExercises
            "Abs" -> abdominalExercises
            "Chest" -> chestExercises
            "Back" -> backExercises
            "Shoulders" -> shoulderExercises
            "Arms" -> bicepsExercises + tricepsExercises
            "Full Body" -> chestExercises + backExercises + legExercises + shoulderExercises + bicepsExercises + tricepsExercises
            "Chest, Shoulders, and Triceps" -> chestExercises + shoulderExercises + tricepsExercises
            "Chest and Back" -> chestExercises + backExercises
            "Shoulders and Arms" -> shoulderExercises + bicepsExercises + tricepsExercises
            else -> emptyList()
        }
    }

    val selectedExercises = remember { workoutDay.exercises.toMutableStateList() }

    AlertDialog(
        containerColor = Color.Black.copy(alpha = 0.7f),
        onDismissRequest = onDismiss,
        title = { Text("Edit Exercises for ${workoutDay.day}", color = Color.White) },
        text = {
            LazyColumn {
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        relevantExercises.forEach { exercise ->
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
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(selectedExercises)
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
