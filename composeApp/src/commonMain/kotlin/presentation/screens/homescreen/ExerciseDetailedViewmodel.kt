package presentation.screens.homescreen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.RealmManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(private val realmManager: RealmManager) : ScreenModel {

    private val _exerciseDetailsFlow = MutableStateFlow(ExerciseDetails())
    val exerciseDetailsFlow: StateFlow<ExerciseDetails> = _exerciseDetailsFlow.asStateFlow()

    fun loadExerciseDetails(exerciseName: String) {
        screenModelScope.launch {
            realmManager.getExerciseDetails(exerciseName).collect { exercise ->
                if (exercise != null) {
                    _exerciseDetailsFlow.value = ExerciseDetails(
                        name = exercise.name,
                        description = exercise.description,
                        muscleGroup = exercise.muscleGroup,
                        equipment = exercise.equipment,
                        lastWeekWeight = exercise.lastWeekWeight ?: 0.0,
                        lastWeekReps = exercise.lastWeekReps ?: 0,
                        lastWeekSets = exercise.lastWeekSets ?: 0
                    )
                }
            }
        }
    }

    fun updateExerciseDetails(exerciseDetails: ExerciseDetails) {
        screenModelScope.launch {
            realmManager.createOrUpdateExercise(exerciseDetails)
            _exerciseDetailsFlow.value = exerciseDetails
        }
    }

    fun updateWeight(exerciseName: String, newWeight: Double) {
        screenModelScope.launch {
            val updatedDetails = _exerciseDetailsFlow.value.copy(lastWeekWeight = newWeight)
            realmManager.createOrUpdateExercise(updatedDetails)
            _exerciseDetailsFlow.value = updatedDetails
        }
    }

    fun updateReps(exerciseName: String, newReps: Int) {
        screenModelScope.launch {
            val updatedDetails = _exerciseDetailsFlow.value.copy(lastWeekReps = newReps)
            realmManager.createOrUpdateExercise(updatedDetails)
            _exerciseDetailsFlow.value = updatedDetails
        }
    }

    fun updateSets(exerciseName: String, newSets: Int) {
        screenModelScope.launch {
            val updatedDetails = _exerciseDetailsFlow.value.copy(lastWeekSets = newSets)
            realmManager.createOrUpdateExercise(updatedDetails)
            _exerciseDetailsFlow.value = updatedDetails
        }
    }

    fun resetExerciseStats(exerciseName: String) {
        screenModelScope.launch {
            realmManager.resetExerciseStats(exerciseName)
            _exerciseDetailsFlow.value = _exerciseDetailsFlow.value.copy(
                lastWeekWeight = 0.0,
                lastWeekReps = 0,
                lastWeekSets = 0
            )
        }
    }
}

data class ExerciseDetails(
    val name: String = "",
    val description: String = "",
    val muscleGroup: String = "",
    val equipment: String = "",
    val lastWeekWeight: Double = 0.0,
    val lastWeekReps: Int = 0,
    val lastWeekSets: Int = 0
)