package presentation.screens.Plans
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

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

    private val workoutDays = mutableStateListOf(
        WorkoutDay("Day 1", "Chest and Triceps", mutableStateListOf("Bench Press", "Incline Dumbbell Press", "Cable Flyes", "Tricep Pushdowns", "Skull Crushers")),
        WorkoutDay("Day 2", "Back and Biceps", mutableStateListOf("Deadlifts", "Pull-Ups", "Bent-Over Rows", "Barbell Curls", "Hammer Curls")),
        WorkoutDay("Day 3", "Legs", mutableStateListOf("Squats", "Leg Press", "Romanian Deadlifts", "Leg Extensions", "Calf Raises")),
        WorkoutDay("Day 4", "Shoulders and Forearms", mutableStateListOf("Overhead Press", "Lateral Raises", "Face Pulls", "Wrist Curls", "Farmer's Walks")),
        WorkoutDay("Day 5", "Arms (Biceps and Triceps)", mutableStateListOf("EZ-Bar Curls", "Dips", "Preacher Curls", "Overhead Tricep Extensions", "Concentration Curls"))
    )

    @Composable
    override fun Content() {
        var editingDay by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("5-Day Split Workout Plan", style = MaterialTheme.typography.headlineMedium)
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
                    editingDay = null
                }
            )
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
            containerColor = Color.Black
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workoutDay.day,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
            Text(
                text = workoutDay.focus,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
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
                    border = BorderStroke(2.dp, Color.Gray)
                ) {
                    Text(if (expanded) "Hide" else "View")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red.copy(alpha = 0.9f)
                    ),
                    border = BorderStroke(2.dp, Color.Gray)
                ) {
                    Text("Edit")
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                workoutDay.exercises.forEach { exercise ->
                    Text("• $exercise", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExercisesDialog(
    workoutDay: WorkoutDay,
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit
) {
    val allExercises = remember {
        listOf(
            "Bench Press", "Incline Dumbbell Press", "Cable Flyes", "Tricep Pushdowns", "Skull Crushers",
            "Deadlifts", "Pull-Ups", "Bent-Over Rows", "Barbell Curls", "Hammer Curls",
            "Squats", "Leg Press", "Romanian Deadlifts", "Leg Extensions", "Calf Raises",
            "Overhead Press", "Lateral Raises", "Face Pulls", "Wrist Curls", "Farmer's Walks",
            "EZ-Bar Curls", "Dips", "Preacher Curls", "Overhead Tricep Extensions", "Concentration Curls"
        )
    }

    val selectedExercises = remember { workoutDay.exercises.toMutableStateList() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Exercises for ${workoutDay.day}") },
        text = {
            LazyColumn {
                items(allExercises) { exercise ->
                    FilterChip(
                        selected = exercise in selectedExercises,
                        onClick = {
                            if (exercise in selectedExercises) {
                                selectedExercises.remove(exercise)
                            } else {
                                selectedExercises.add(exercise)
                            }
                        },
                        label = { Text(exercise) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(selectedExercises) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}