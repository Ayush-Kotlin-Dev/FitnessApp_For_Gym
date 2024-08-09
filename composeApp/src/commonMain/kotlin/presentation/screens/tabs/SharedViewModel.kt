package presentation.screens.tabs

import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.local.getSelectedRoutineFlowFromPreferences
import data.local.saveSelectedRoutineToPreferences
import data.models.Exercise
import data.models.WorkoutDayDb
import data.models.WorkoutPlan
import data.models.WorkoutPlanDb
import domain.RealmManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.screens.plans.WorkoutDay
import presentation.screens.plans.abdominalExercises
import presentation.screens.plans.backExercises
import presentation.screens.plans.bicepsExercises
import presentation.screens.plans.chestExercises
import presentation.screens.plans.forearmExercises
import presentation.screens.plans.legExercises
import presentation.screens.plans.shoulderExercises
import presentation.screens.plans.tricepsExercises

class SharedWorkoutViewModel(
    private val realmManager: RealmManager,
    private val dataStore: DataStore<Preferences>

) : ScreenModel {
    private val _selectedExercises =
        MutableStateFlow<Map<String, Map<String, List<Exercise>>>>(emptyMap())
    private val _currentWorkoutPlan = MutableStateFlow<WorkoutPlan?>(null)
    val currentWorkoutPlan: StateFlow<WorkoutPlan?> = _currentWorkoutPlan.asStateFlow()

    // Helper function to get exercises by name
    private fun getExercisesByName(names: List<String>): MutableList<Exercise> {
        return names.mapNotNull { allExercises[it] }.toMutableList()
    }

    // Mapping exercises by name for easy lookup
    private val allExercises = (chestExercises + tricepsExercises + backExercises + bicepsExercises+ forearmExercises + shoulderExercises + abdominalExercises + legExercises)
        .associateBy { it.name }

    // Creating the workout plans
    private val workoutPlans = mapOf(
        "5-Day Split" to mutableStateListOf(
            WorkoutDay(
                "Day 1",
                "Chest and Triceps",
                getExercisesByName(listOf("Barbell Bench Press", "Incline Dumbbell Bench Press", "Cable Flyes", "Tricep Pushdowns", "Skull Crushers", "Dips"))),
            WorkoutDay(
                "Day 2",
                "Back and Biceps",
                getExercisesByName(
                    listOf(
                        "Deadlifts", "Pull-Ups", "Lat Pulldown",
                        "Bent-Over Rows",
                        "Barbell Curls",
                        "Hammer Curls"
                    )
                )
            ),
            WorkoutDay(
                "Day 3",
                "Legs",
                getExercisesByName(
                    listOf(
                        "Squats",
                        "Leg Press",
                        "Romanian Deadlifts",
                        "Leg Extensions",
                        "Calf Raises"
                    )
                )
            ),
            WorkoutDay(
                "Day 4",
                "Shoulders and Forearms",
                getExercisesByName(
                    listOf(
                        "Overhead Press",
                        "Lateral Raises",
                        "Face Pulls",
                        "Wrist Curls",
                        "Farmer's Walks"
                    )
                )
            ),
            WorkoutDay(
                "Day 5",
                "Arms (Biceps and Triceps)",
                getExercisesByName(
                    listOf(
                        "EZ-Bar Curls",
                        "Dips",
                        "Preacher Curls",
                        "Overhead Tricep Extensions",
                        "Concentration Curls"
                    )
                )
            )
        ),
        "6-Day Push/Pull/Legs" to mutableStateListOf(
            WorkoutDay(
                "Day 1",
                "Chest, Shoulders, and Triceps",
                getExercisesByName(
                    listOf(
                        "Barbell Bench Press",
                        "Overhead Press",
                        "Tricep Pushdowns",
                        "Lateral Raises"
                    )
                )
            ),
            WorkoutDay(
                "Day 2",
                "Back and Biceps",
                getExercisesByName(
                    listOf(
                        "Deadlifts",
                        "Pull-Ups",
                        "Bent-Over Rows",
                        "Barbell Curls"
                    )
                )
            ),
            WorkoutDay(
                "Day 3",
                "Legs",
                getExercisesByName(
                    listOf(
                        "Squats",
                        "Leg Press",
                        "Romanian Deadlifts",
                        "Leg Extensions"
                    )
                )
            ),
            WorkoutDay(
                "Day 4",
                "Chest, Shoulders, and Triceps",
                getExercisesByName(
                    listOf(
                        "Incline Bench Press",
                        "Face Pulls",
                        "Dips",
                        "Lateral Raises"
                    )
                )
            ),
            WorkoutDay(
                "Day 5",
                "Back and Biceps",
                getExercisesByName(listOf("Bent-Over Rows", "Hammer Curls", "Face Pulls"))
            ),
            WorkoutDay(
                "Day 6",
                "Legs",
                getExercisesByName(listOf("Front Squats", "Leg Curls", "Standing Calf Raises"))
            )
        ),
        "4-Day Upper/Lower" to mutableStateListOf(
            WorkoutDay(
                "Day 1",
                "Chest and Back",
                getExercisesByName(
                    listOf(
                        "Barbell Bench Press",
                        "Rows",
                        "Incline Dumbbell Bench Press",
                        "Pull-Ups"
                    )
                )
            ),
            WorkoutDay(
                "Day 2",
                "Legs",
                getExercisesByName(listOf("Squats", "Deadlifts", "Leg Press", "Calf Raises"))
            ),
            WorkoutDay(
                "Day 3",
                "Shoulders and Arms",
                getExercisesByName(
                    listOf(
                        "Overhead Press",
                        "Lateral Raises",
                        "Barbell Curls",
                        "Tricep Pushdowns"
                    )
                )
            ),
            WorkoutDay(
                "Day 4",
                "Legs",
                getExercisesByName(
                    listOf(
                        "Front Squats",
                        "Romanian Deadlifts",
                        "Leg Curls",
                        "Seated Calf Raises"
                    )
                )
            ),
        ),
        "5-Day Upper/Lower/Full" to mutableStateListOf(
            WorkoutDay(
                "Day 1",
                "Chest and Back",
                getExercisesByName(
                    listOf(
                        "Barbell Bench Press",
                        "Rows",
                        "Incline Dumbbell Bench Press",
                        "Pull-Ups"
                    )
                )
            ),
            WorkoutDay(
                "Day 2",
                "Legs",
                getExercisesByName(listOf("Squats", "Deadlifts", "Leg Press", "Calf Raises"))
            ),
            WorkoutDay(
                "Day 3",
                "Shoulders and Arms",
                getExercisesByName(
                    listOf(
                        "Overhead Press",
                        "Lateral Raises",
                        "Barbell Curls",
                        "Tricep Pushdowns"
                    )
                )
            ),
            WorkoutDay(
                "Day 4",
                "Legs",
                getExercisesByName(
                    listOf(
                        "Front Squats",
                        "Romanian Deadlifts",
                        "Leg Curls",
                        "Seated Calf Raises"
                    )
                )
            ),
            WorkoutDay(
                "Day 5",
                "Full Body",
                getExercisesByName(
                    listOf(
                        "Deadlifts",
                        "Overhead Press",
                        "Pull-Ups",
                        "Barbell Curls",
                        "Tricep Pushdowns"
                    )
                )
            ),
        ),
        "4-Day Push/Pull" to mutableStateListOf(
            WorkoutDay(
                "Day 1",
                "Chest, Shoulders, and Triceps",
                getExercisesByName(
                    listOf(
                        "Barbell Bench Press",
                        "Overhead Press",
                        "Tricep Pushdowns",
                        "Lateral Raises"
                    )
                )
            ),
            WorkoutDay(
                "Day 2",
                "Back and Biceps",
                getExercisesByName(
                    listOf(
                        "Deadlifts",
                        "Pull-Ups",
                        "Bent-Over Rows",
                        "Barbell Curls"
                    )
                )
            ),
            WorkoutDay(
                "Day 3",
                "Legs",
                getExercisesByName(
                    listOf(
                        "Squats",
                        "Leg Press",
                        "Romanian Deadlifts",
                        "Leg Extensions"
                    )
                )
            ),
            WorkoutDay(
                "Day 4",
                "Chest, Shoulders, and Triceps",
                getExercisesByName(
                    listOf(
                        "Incline Bench Press",
                        "Face Pulls",
                        "Dips",
                        "Lateral Raises"
                    )
                )
            )
        ),
        "6-Day Body Part Split" to mutableStateListOf(
            WorkoutDay(
                "Day 1",
                "Chest",
                getExercisesByName(
                    listOf(
                        "Push-Ups",
                        "Bench Press",
                        "Incline Dumbbell Bench Press",
                        "Cable Flyes",
                        "Pec fly",
                        "Dips"
                    )
                )
            ),
            WorkoutDay(
                "Day 2",
                "Back",
                getExercisesByName(
                    listOf(
                        "Deadlifts",
                        "Pull-Ups",
                        "Bent-Over Rows",
                        "Lat-PullDown",
                        "Face Pulls"
                    )
                )
            ),
            WorkoutDay(
                "Day 3",
                "Legs",
                getExercisesByName(listOf("Squats", "Leg Press", "Romanian Deadlifts"))
            ),
            WorkoutDay(
                "Day 4",
                "Shoulders",
                getExercisesByName(listOf("Overhead Press", "Lateral Raises", "Face Pulls"))
            ),
            WorkoutDay(
                "Day 5",
                "Arms",
                getExercisesByName(listOf("Barbell Curls", "Tricep Pushdowns", "Hammer Curls"))
            ),
            WorkoutDay(
                "Day 6",
                "Abs",
                getExercisesByName(listOf("Planks", "Russian Twists", "Leg Raises"))
            )
        )
    )

    init {
        // Initialize _selectedExercises with the workout plans
        _selectedExercises.value = workoutPlans.mapValues { (_, days) ->
            days.associate { it.day to it.exercises }
        }
        println("SharedWorkoutViewModel init")
    }

    private fun updateSelectedExercises(planName: String, day: String, exercises: List<Exercise>) {
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
                        this.exerciseDbs.addAll(exercises.map { exercise ->
                            data.models.ExerciseDb().apply {
                                name = exercise.name
                                description = exercise.description
                                muscleGroup = exercise.muscleGroup
                                equipment = exercise.equipment
                                lastWeekWeight = exercise.lastWeekWeight
                                lastWeekReps = exercise.lastWeekReps
                                lastWeekSets = exercise.lastWeekSets
                            }
                        })
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
    fun saveExercisesToDb(planName: String, day: String, exercises: List<Exercise>) {
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