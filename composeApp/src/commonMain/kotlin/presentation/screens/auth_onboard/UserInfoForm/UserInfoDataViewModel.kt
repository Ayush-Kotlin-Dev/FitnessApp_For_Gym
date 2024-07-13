package presentation.screens.auth_onboard.UserInfoForm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel

class UserInfoDataViewModel : ScreenModel {

    private val _uiState = mutableStateOf(UserInfoDataUiState())
    val uiState: State<UserInfoDataUiState> = _uiState

    // Basic Info
    fun onFullNameChange(newFullName: String) {
        _uiState.value = _uiState.value.copy(fullName = newFullName)
    }

    fun onAgeChange(newAge: Int) {
        _uiState.value = _uiState.value.copy(age = newAge)
    }

    fun onGenderChange(newGender: String) {
        _uiState.value = _uiState.value

    }

    // Physical Measurements
    fun onHeightChange(newHeight: Float) {
        _uiState.value = _uiState.value.copy(height = newHeight)
    }

    fun onWeightChange(newWeight: Float) {
        _uiState.value = _uiState.value.copy(weight = newWeight)
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
        //TODO
    }


}

data class UserInfoDataUiState(
    val fullName: String = "",
    val age: Int = 0,
    val gender: String = "",
    val height: Float = 0f,
    val weight: Float = 0f,
    val fitnessGoals: String = "",
    val activityLevel: String = "",
    val dietaryPreferences: String = "",
    val workoutPreferences: String = ""
)