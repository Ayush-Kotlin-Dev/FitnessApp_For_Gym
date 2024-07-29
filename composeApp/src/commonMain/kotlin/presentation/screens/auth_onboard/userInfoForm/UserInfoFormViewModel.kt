package presentation.screens.auth_onboard.userInfoForm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.local.getUserSettingsFlow
import data.models.UserInfoData
import domain.usecases.userinfo.SubmitUserInfoUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import util.Result

class UserInfoFormViewModel(
    private val userInfoUseCase: SubmitUserInfoUseCase,
    private val dataStore: DataStore<Preferences>
) : ScreenModel {

    private val _uiState = mutableStateOf(UserInfoDataUiState())
    val uiState: State<UserInfoDataUiState> = _uiState

    // Basic Info
    fun onFullNameChange(newFullName: String) {
        updateUiState { it.copy(fullName = newFullName) }
    }

    fun onAgeChange(newAge: String) {
        val age = newAge.toIntOrNull() ?: return
        updateUiState { it.copy(age = age) }
    }

    fun setGender(gender: String) {
        updateUiState { it.copy(gender = gender) }
    }

    // Physical Measurements
    fun onHeightChange(height: String) {
        val heightFloat = height.toFloatOrNull() ?: return
        updateUiState { it.copy(height = heightFloat) }
    }

    fun onWeightChange(weight: String) {
        val weightFloat = weight.toFloatOrNull() ?: return
        updateUiState { it.copy(weight = weightFloat) }
    }

    // Fitness Goals and Activity Level
    fun setFitnessGoals(fitnessGoals: String) {
        updateUiState { it.copy(fitnessGoals = fitnessGoals) }
    }

    fun setActivityLevel(activityLevel: String) {
        updateUiState { it.copy(activityLevel = activityLevel) }
    }

    // Dietary and Workout Preferences
    fun setDietaryPreferences(dietaryPreferences: String) {
        updateUiState { it.copy(dietaryPreferences = dietaryPreferences) }
    }

    fun setWorkoutPreferences(workoutPreferences: String) {
        updateUiState { it.copy(workoutPreferences = workoutPreferences) }
    }

    private fun updateUiState(update: (UserInfoDataUiState) -> UserInfoDataUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun clearUserData() {
        _uiState.value = UserInfoDataUiState()
    }
    private var userId: Long = -1

    private fun isDataValid(): Boolean {
        return _uiState.value.run {
            fullName.isNotEmpty() &&
                    age != null &&
                    gender.isNotEmpty() &&
                    height != null &&
                    weight != null &&
                    fitnessGoals.isNotEmpty() &&
                    activityLevel.isNotEmpty() &&
                    dietaryPreferences.isNotEmpty() &&
                    workoutPreferences.isNotEmpty()
        }
    }

    fun submitUserData() {
        if (!isDataValid()) {
            updateUiState { it.copy(
                errorMessage =  "Please fill all the fields",
                submitSuccess = false
            ) }
            return
        }

        screenModelScope.launch {
            updateUiState { it.copy(isLoading = true) }
            getUserSettingsFlow(dataStore).collectLatest { settings ->
                userId = settings.userId
            }
            val userInfoData = UserInfoData(
                userId =  userId,
                fullName = _uiState.value.fullName,
                age = _uiState.value.age ?: 0,
                gender = _uiState.value.gender,
                height = _uiState.value.height ?: 0f,
                weight = _uiState.value.weight ?: 0f,
                fitnessGoals = _uiState.value.fitnessGoals,
                activityLevel = _uiState.value.activityLevel,
                dietaryPreferences = _uiState.value.dietaryPreferences,
                workoutPreferences = _uiState.value.workoutPreferences
            )

            when (val result = userInfoUseCase(userInfoData)) {
                is Result.Error -> {
                    updateUiState { it.copy(
                        isLoading = false,
                        submitSuccess = false,
                        errorMessage = result.message ?: "An error occurred"
                    ) }
                }
                is Result.Success -> {
                    updateUiState { it.copy(
                        isLoading = false,
                        submitSuccess = true
                    ) }
                }
                is Result.Loading -> {
                    updateUiState { it.copy(isLoading = true) }
                }
            }
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
    val submitSuccess: Boolean = false,
    val errorMessage: String = ""
)
