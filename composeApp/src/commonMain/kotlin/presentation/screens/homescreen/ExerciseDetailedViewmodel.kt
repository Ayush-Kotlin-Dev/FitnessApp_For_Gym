package presentation.screens.homescreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.local.getSelectedRoutineFlowFromPreferences
import data.models.Exercise
import domain.RealmManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(private val realmManager: RealmManager,private val dataStore: DataStore<Preferences>) : ScreenModel {

    private val _exerciseDetailsFlow = MutableStateFlow(Exercise(
        name = "",
        description = "",
        muscleGroup = "",
        equipment = ""
    ))
    val exerciseDetailsFlow: StateFlow<Exercise> = _exerciseDetailsFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadExerciseDetails(exerciseName: String) {
        screenModelScope.launch {
            _isLoading.value = true
            realmManager.getExerciseDetails(exerciseName).collect { exercise ->
                if (exercise != null) {
                    _exerciseDetailsFlow.value = Exercise(
                        name = exercise.name,
                        description = exercise.description,
                        muscleGroup = exercise.muscleGroup,
                        equipment = exercise.equipment,
                        lastWeekWeight = exercise.lastWeekWeight ?: 0.0,
                        lastWeekReps = exercise.lastWeekReps ?: 0,
                        lastWeekSets = exercise.lastWeekSets ?: 0
                    )
                }
                _isLoading.value = false
            }
        }
    }
    fun getSelectedRoutineFlow(): Flow<String?> {
        return getSelectedRoutineFlowFromPreferences(dataStore)
    }


    fun updateWeight(planName: String, dayName: String, exerciseName: String, newWeight: Double) {
        screenModelScope.launch {
            realmManager.updateExerciseWeight(planName, dayName, exerciseName, newWeight)
            val updatedDetails = _exerciseDetailsFlow.value.copy(lastWeekWeight = newWeight)
            _exerciseDetailsFlow.value = updatedDetails
        }
    }

    fun updateReps(planName: String, dayName: String, exerciseName: String, newReps: Int) {
        screenModelScope.launch {
            realmManager.updateExerciseReps(planName, dayName, exerciseName, newReps)
            val updatedDetails = _exerciseDetailsFlow.value.copy(lastWeekReps = newReps)
            _exerciseDetailsFlow.value = updatedDetails
        }
    }

    fun updateSets(planName: String, dayName: String, exerciseName: String, newSets: Int) {
        screenModelScope.launch {
            realmManager.updateExerciseSets(planName, dayName, exerciseName, newSets)
            val updatedDetails = _exerciseDetailsFlow.value.copy(lastWeekSets = newSets)
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

