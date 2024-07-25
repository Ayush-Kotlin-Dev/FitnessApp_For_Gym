package presentation.screens.tabs

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import presentation.screens.plans.WorkoutDay


class SharedWorkoutViewModel : ScreenModel {
    private val _selectedExercises = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val selectedExercises: StateFlow<Map<String, List<String>>> = _selectedExercises.asStateFlow()

    fun getWorkoutDaysForPlan(planName: String): MutableList<WorkoutDay> {
        return workoutPlans[planName] ?: mutableStateListOf()
    }
    private val workoutPlans = mapOf(
        "5-Day Split" to mutableStateListOf(
            WorkoutDay("Day 1", "Chest and Triceps", mutableListOf("Bench Press", "Incline Dumbbell Press", "Cable Flyes", "Tricep Pushdowns", "Skull Crushers")),
            WorkoutDay("Day 2", "Back and Biceps", mutableListOf("Deadlifts", "Pull-Ups", "Bent-Over Rows", "Barbell Curls", "Hammer Curls")),
            WorkoutDay("Day 3", "Legs", mutableListOf("Squats", "Leg Press", "Romanian Deadlifts", "Leg Extensions", "Calf Raises")),
            WorkoutDay("Day 4", "Shoulders and Forearms", mutableListOf("Overhead Press", "Lateral Raises", "Face Pulls", "Wrist Curls", "Farmer's Walks")),
            WorkoutDay("Day 5", "Arms (Biceps and Triceps)", mutableListOf("EZ-Bar Curls", "Dips", "Preacher Curls", "Overhead Tricep Extensions", "Concentration Curls"))
        ),
        "6-Day Push/Pull/Legs" to mutableStateListOf(
            WorkoutDay("Day 1", "Chest, Shoulders, and Triceps", mutableListOf("Bench Press", "Overhead Press", "Tricep Pushdowns", "Lateral Raises")),
            WorkoutDay("Day 2", "Back and Biceps", mutableListOf("Deadlifts", "Pull-Ups", "Bent-Over Rows", "Barbell Curls")),
            WorkoutDay("Day 3", "Legs", mutableListOf("Squats", "Leg Press", "Romanian Deadlifts", "Leg Extensions")),
            WorkoutDay("Day 4", "Chest, Shoulders, and Triceps", mutableListOf("Incline Bench Press", "Face Pulls", "Tricep Dips", "Lateral Raises")),
            WorkoutDay("Day 5", "Back and Biceps", mutableListOf("Rows", "Hammer Curls", "Face Pulls")),
            WorkoutDay("Day 6", "Legs", mutableListOf("Front Squats", "Leg Curls", "Standing Calf Raises"))

        ),
        "4-Day Upper/Lower" to mutableStateListOf(
            WorkoutDay("Day 1", "Chest and Back", mutableListOf("Bench Press", "Rows", "Incline Dumbbell Press", "Pull-Ups")),
            WorkoutDay("Day 2", "Legs", mutableListOf("Squats", "Deadlifts", "Leg Press", "Calf Raises")),
            WorkoutDay("Day 3", "Shoulders and Arms", mutableListOf("Overhead Press", "Lateral Raises", "Barbell Curls", "Tricep Pushdowns")),
            WorkoutDay("Day 4", "Legs", mutableListOf("Front Squats", "Romanian Deadlifts", "Leg Curls", "Seated Calf Raises"))
        ),
        "5-Day Upper/Lower/Full" to mutableStateListOf(
            WorkoutDay("Day 1", "Chest and Back", mutableListOf("Bench Press", "Rows", "Incline Dumbbell Press", "Pull-Ups")),
            WorkoutDay("Day 2", "Legs", mutableListOf("Squats", "Deadlifts", "Leg Press", "Calf Raises")),
            WorkoutDay("Day 3", "Shoulders and Arms", mutableListOf("Overhead Press", "Lateral Raises", "Barbell Curls", "Tricep Pushdowns")),
            WorkoutDay("Day 4", "Legs", mutableListOf("Front Squats", "Romanian Deadlifts", "Leg Curls", "Seated Calf Raises")),
            WorkoutDay("Day 5", "Full Body", mutableListOf("Deadlifts", "Overhead Press", "Pull-Ups", "Barbell Curls", "Tricep Pushdowns"))

        ),
        "4-Day Push/Pull" to mutableStateListOf(
            WorkoutDay("Day 1", "Chest, Shoulders, and Triceps", mutableListOf("Bench Press", "Overhead Press", "Tricep Pushdowns", "Lateral Raises")),
            WorkoutDay("Day 2", "Back and Biceps", mutableListOf("Deadlifts", "Pull-Ups", "Bent-Over Rows", "Barbell Curls")),
            WorkoutDay("Day 3", "Legs", mutableListOf("Squats", "Leg Press", "Romanian Deadlifts", "Leg Extensions")),
            WorkoutDay("Day 4", "Chest, Shoulders, and Triceps", mutableListOf("Incline Bench Press", "Face Pulls", "Tricep Dips", "Lateral Raises"))
        ),
        "6-Day Body Part Split" to mutableStateListOf(
            WorkoutDay("Day 1", "Chest", mutableListOf("Bench Press", "Incline Dumbbell Press", "Cable Flyes")),
            WorkoutDay("Day 2", "Back", mutableListOf("Deadlifts", "Pull-Ups", "Bent-Over Rows")),
            WorkoutDay("Day 3", "Legs", mutableListOf("Squats", "Leg Press", "Romanian Deadlifts")),
            WorkoutDay("Day 4", "Shoulders", mutableListOf("Overhead Press", "Lateral Raises", "Face Pulls")),
            WorkoutDay("Day 5", "Arms", mutableListOf("Barbell Curls", "Tricep Pushdowns", "Hammer Curls")),
            WorkoutDay("Day 6", "Abs", mutableListOf("Planks", "Russian Twists", "Leg Raises"))


        )
    )
    fun updateSelectedExercises(planName: String, day: String, exercises: List<String>) {
        workoutPlans[planName]?.first { it.day == day }?.exercises?.apply {
            clear()
            addAll(exercises)
        }
    }
}
