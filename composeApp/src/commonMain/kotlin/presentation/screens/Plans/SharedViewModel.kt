package presentation.screens.Plans

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedWorkoutViewModel : ScreenModel {
    private val _selectedExercises = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val selectedExercises: StateFlow<Map<String, List<String>>> = _selectedExercises.asStateFlow()

    fun updateSelectedExercises(day: String, exercises: List<String>) {
        _selectedExercises.value = _selectedExercises.value.toMutableMap().apply {
            put(day, exercises)
        }
    }
}