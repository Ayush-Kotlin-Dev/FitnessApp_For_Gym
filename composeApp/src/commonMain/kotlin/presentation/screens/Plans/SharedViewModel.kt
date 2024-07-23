package presentation.screens.Plans

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedWorkoutViewModel : ScreenModel {
    private val _selectedExercises = MutableStateFlow<List<String>>(emptyList())
    val selectedExercises: StateFlow<List<String>> = _selectedExercises.asStateFlow()

    fun updateSelectedExercises(exercises: List<String>) {
        _selectedExercises.value = exercises
    }
}