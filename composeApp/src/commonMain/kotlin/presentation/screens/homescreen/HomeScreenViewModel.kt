package presentation.screens.homescreen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.models.WorkoutDayDb
import domain.RealmManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val realmManager: RealmManager
) : ScreenModel {
    private val _currentWorkoutDay = MutableStateFlow<WorkoutDayDb?>(null)
    val currentWorkoutDay: StateFlow<WorkoutDayDb?> = _currentWorkoutDay

    init {
        println("HomeScreenViewModel init")
    }

    fun getWorkoutDayForDate(planName: String, dayName: String) {
        screenModelScope.launch {
            realmManager.getWorkoutDayForDate(planName, dayName).collect { day ->
                _currentWorkoutDay.value = day
            }
        }
    }
}
