package presentation.screens.tabs

import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.local.getSelectedRoutineFlowFromPreferences
import data.local.saveSelectedRoutineToPreferences
import data.models.WorkoutDayDb
import data.models.WorkoutPlanDb
import domain.RealmManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.screens.plans.WorkoutDay

class SharedWorkoutViewModel(
    private val realmManager: RealmManager,
    private val dataStore: DataStore<Preferences>

) : ScreenModel {
    private val _selectedExercises =
        MutableStateFlow<Map<String, Map<String, List<String>>>>(emptyMap())

    private val _currentWorkoutPlan = MutableStateFlow<WorkoutPlanDb?>(null)
    val currentWorkoutPlan: StateFlow<WorkoutPlanDb?> = _currentWorkoutPlan.asStateFlow()

    private val workoutPlans = mapOf(
        "5-Day Split" to mutableStateListOf(
            WorkoutDay("Day 1", "Chest and Triceps", mutableListOf("Bench Press", "Incline Dumbbell Press", "Cable Flyes", "Tricep Pushdowns", "Skull Crushers", "Dips")),
            WorkoutDay("Day 2", "Back and Biceps", mutableListOf("Deadlifts", "Pull-Ups", "Lat-PullDown " , "Bent-Over Rows", "Barbell Curls", "Hammer Curls")),
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
            WorkoutDay("Day 1", "Chest", mutableListOf("Push-Ups" , "Bench Press", "Incline Dumbbell Press", "Cable Flyes" , "Pec fly" ,"Dips")),
            WorkoutDay("Day 2", "Back", mutableListOf("Deadlifts", "Pull-Ups", "Bent-Over Rows , " , "Lat-PullDown" , "Face Pulls")),
            WorkoutDay("Day 3", "Legs", mutableListOf("Squats", "Leg Press", "Romanian Deadlifts")),
            WorkoutDay("Day 4", "Shoulders", mutableListOf("Overhead Press", "Lateral Raises", "Face Pulls")),
            WorkoutDay("Day 5", "Arms", mutableListOf("Barbell Curls", "Tricep Pushdowns", "Hammer Curls")),
            WorkoutDay("Day 6", "Abs", mutableListOf("Planks", "Russian Twists", "Leg Raises"))
        )
    )
    init {
        // Initialize _selectedExercises with the workout plans
        _selectedExercises.value = workoutPlans.mapValues { (_, days) ->
            days.associate { it.day to it.exercises }
        }
        println("SharedWorkoutViewModel init")
    }

    fun updateSelectedExercises(planName: String, day: String, exercises: List<String>) {
        _selectedExercises.update { currentPlans ->
            val updatedPlan = currentPlans[planName]?.toMutableMap() ?: mutableMapOf()
            updatedPlan[day] = exercises
            currentPlans + (planName to updatedPlan)
        }
    }

    fun saveWorkoutPlanToDb(planName: String) {
        screenModelScope.launch {
            val plan = WorkoutPlanDb().apply {
                name = planName
                days.addAll((_selectedExercises.value[planName]?.map { (day, exercises) ->
                    WorkoutDayDb().apply {
                        this.day = day
                        this.focus = workoutPlans[planName]?.find { it.day == day }?.focus ?: ""
                        this.exercises.addAll(exercises)
                    }
                } ?: emptyList()))
            }
            realmManager.saveWorkoutPlan(plan)
        }
    }


    fun saveLastSelectedPlan(planName: String) {
        screenModelScope.launch {
            realmManager.saveLastSelectedPlan(planName)
        }
    }

    // reload the exercises lst on the card after clicking the save button on edit dialog
    fun reloadCurrentWorkoutPlan() {
        screenModelScope.launch {
            currentWorkoutPlan.value?.let { plan ->
                loadWorkoutPlanFromDb(plan.name)
            }
        }
    }
    //method to update the exercises for a specific day in a workout plan
    fun loadWorkoutPlanFromDb(planName: String) {
        screenModelScope.launch {
            realmManager.getWorkoutPlan(planName).collect { plan ->
                _currentWorkoutPlan.value = plan
            }
        }
    }

    // Preferences DataStore to get and save the selected routine name
    fun saveSelectedRoutine(routineName: String) {
        screenModelScope.launch {
            saveSelectedRoutineToPreferences(dataStore, routineName)
        }
    }
    fun getSelectedRoutineFlow(): Flow<String?> {
        return getSelectedRoutineFlowFromPreferences(dataStore)
    }

    //method to update the exercises for a specific day in a workout plan
    fun saveExercisesToDb(planName: String, day: String, exercises: List<String>) {
        screenModelScope.launch {
            realmManager.updateWorkoutDayExercises(planName, day, exercises)
            // Update the local state as well
            updateSelectedExercises(planName, day, exercises)
        }
    }

    fun reorderWorkoutDays(fromIndex: Int, toIndex: Int) {
        screenModelScope.launch {
            currentWorkoutPlan.value?.let { plan ->
                realmManager.reorderWorkoutDays(plan.name, fromIndex, toIndex)
                // Reload the workout plan to reflect changes
                loadWorkoutPlanFromDb(plan.name)
            }
        }
    }



    override fun onDispose() {
        println("SharedWorkoutViewModel disposed")
    }
}