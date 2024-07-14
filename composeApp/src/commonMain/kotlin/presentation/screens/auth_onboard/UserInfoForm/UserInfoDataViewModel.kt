package presentation.screens.auth_onboard.UserInfoForm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserInfoDataViewModel : ScreenModel {

    private val _uiState = mutableStateOf(UserInfoDataUiState())
    val uiState: State<UserInfoDataUiState> = _uiState

    // Basic Info
    fun onFullNameChange(newFullName: String) {
        _uiState.value = _uiState.value.copy(fullName = newFullName)
    }

    fun onAgeChange(newAge: String) {
        _uiState.value = _uiState.value.copy(age = newAge.toInt())
    }

    fun setGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    // Physical Measurements
    fun onHeightChange(height: String) {
        _uiState.value = _uiState.value.copy(height = height.toFloatOrNull())
    }

    fun onWeightChange(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight.toFloatOrNull())
    }


    // Fitness Goals and Activity Level
    fun setFitnessGoals(fitnessGoals: String) {
        _uiState.value = _uiState.value.copy(fitnessGoals = fitnessGoals)
    }

    fun setActivityLevel(activityLevel: String) {
        _uiState.value = _uiState.value.copy(activityLevel = activityLevel)
    }


    // Dietary and Workout Preferences
    fun setDietaryPreferences(dietaryPreferences: String) {
        _uiState.value = _uiState.value.copy(dietaryPreferences = dietaryPreferences)
    }

    fun setWorkoutPreferences(workoutPreferences: String) {
        _uiState.value = _uiState.value.copy(workoutPreferences = workoutPreferences)
    }

    fun clearUserData() {
        //TODO
    }

    fun submitUserData() {
        screenModelScope.launch {
            // Set isAuthenticating to true
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Perform login logic here
            delay(2000)

            // Set isAuthenticating to false
            _uiState.value = _uiState.value.copy(isLoading = false)
            _uiState.value = _uiState.value.copy(submitSuccess = true)

        }
    }
}

data class UserInfoDataUiState(
    val fullName: String = "",
    val age: Int? = null,
    val gender: String = "",
    val height: Float? = null,
    val weight: Float? = null,
    val fitnessGoals: String = "",
    val activityLevel: String = "",
    val dietaryPreferences: String = "",
    val workoutPreferences: String = "",
    val isLoading: Boolean = false,
    val submitSuccess: Boolean = false
)
