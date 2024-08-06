package presentation.screens.homescreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.local.getUserInfoFlow
import data.models.WorkoutDayDb
import domain.RealmManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val realmManager: RealmManager,
    private val dataStore: DataStore<Preferences>
) : ScreenModel {
    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiStateFlow: StateFlow<HomeScreenUiState> = _homeScreenUiState

    init {
        println("HomeScreenViewModel init")
    }

    fun getWorkoutDayForDate(planName: String, dayName: String) {
        screenModelScope.launch {
            _homeScreenUiState.update { it.copy(isLoading = true) }
            realmManager.getWorkoutDayForDate(planName, dayName).collect { day ->
                _homeScreenUiState.update {
                    it.copy(currentWorkoutDay = day, isLoading = false, currentPlanName = planName)
                }
            }
        }
    }

    fun getFullName() {
        screenModelScope.launch {
            getUserInfoFlow(dataStore).collectLatest { settings ->
                _homeScreenUiState.update {
                    it.copy(fullName = settings.fullName)
                }
            }
        }
    }
    fun reorderExercises(fromIndex: Int, toIndex: Int) {
        val currentState = _homeScreenUiState.value
        val planName = currentState.currentPlanName
        val dayName = currentState.currentWorkoutDay?.day

        if (planName != null && dayName != null) {
            screenModelScope.launch {
                realmManager.reorderExercises(planName, dayName, fromIndex, toIndex)
                // Refresh the workout day data after reordering
                getWorkoutDayForDate(planName, dayName)
            }
        } else {
            println("Cannot reorder exercises: plan name or day name is null")
        }
    }
}

data class HomeScreenUiState(
    val currentWorkoutDay: WorkoutDayDb? = null,
    val currentPlanName: String? = null,
    val isLoading: Boolean = true,
    val fullName: String = ""
)