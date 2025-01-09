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

    // Validation helpers
    private fun validateFullName(name: String): String? {
        return when {
            name.isBlank() -> "Name cannot be empty"
            name.length < 2 -> "Name must be at least 2 characters"
            !name.matches("[a-zA-Z ]+".toRegex()) -> "Name can only contain letters"
            else -> null
        }
    }

    private fun validateAge(age: Int?): String? {
        return when {
            age == null -> "Age is required"
            age < 13 -> "Must be at least 13 years old"
            age > 100 -> "Please enter a valid age"
            else -> null
        }
    }

    private fun validateMeasurement(value: Float?, type: String): String? {
        return when {
            value == null -> "$type is required"
            value <= 0f -> "Invalid $type"
            when (type) {
                "height" -> value > 250f
                "weight" -> value > 250f
                else -> false
            } -> "Please enter a valid $type"
            else -> null
        }
    }

    // Updated input handlers with validation
    fun onFullNameChange(newFullName: String) {
        updateUiState { 
            it.copy(
                fullName = newFullName,
                fullNameError = validateFullName(newFullName)
            )
        }
    }

    fun onAgeChange(newAge: String) {
        val age = newAge.toIntOrNull()
        updateUiState { 
            it.copy(
                age = age,
                ageError = validateAge(age)
            )
        }
    }

    fun onHeightChange(height: String) {
        val heightFloat = height.toFloatOrNull()
        updateUiState { 
            it.copy(
                height = heightFloat,
                heightError = validateMeasurement(heightFloat, "height")
            )
        }
    }

    fun onWeightChange(weight: String) {
        val weightFloat = weight.toFloatOrNull()
        updateUiState { 
            it.copy(
                weight = weightFloat,
                weightError = validateMeasurement(weightFloat, "weight")
            )
        }
    }

    fun setGender(gender: String) {
        updateUiState { it.copy(gender = gender) }
    }

    fun setFitnessGoals(fitnessGoals: String) {
        updateUiState { it.copy(fitnessGoals = fitnessGoals) }
    }

    fun setActivityLevel(activityLevel: String) {
        updateUiState { it.copy(activityLevel = activityLevel) }
    }

    fun setDietaryPreferences(dietaryPreferences: String) {
        updateUiState { it.copy(dietaryPreferences = dietaryPreferences) }
    }

    fun setWorkoutPreferences(workoutPreferences: String) {
        updateUiState { it.copy(workoutPreferences = workoutPreferences) }
    }

    // Add validation state to UI state
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
        val fullNameError: String? = null,
        val ageError: String? = null,
        val heightError: String? = null,
        val weightError: String? = null,
        val currentStep: Int = 0,
        val canProceedToNextStep: Boolean = false,
        val isLoading: Boolean = false,
        val submitSuccess: Boolean = false,
        val errorMessage: String = ""
    )

    private fun updateUiState(update: (UserInfoDataUiState) -> UserInfoDataUiState) {
        _uiState.value = update(_uiState.value)
    }

    private fun isBasicInfoValid() = validateFullName(_uiState.value.fullName) == null &&
            validateAge(_uiState.value.age) == null &&
            _uiState.value.gender.isNotEmpty()

    private fun isPhysicalMeasurementsValid() = validateMeasurement(_uiState.value.height, "height") == null &&
            validateMeasurement(_uiState.value.weight, "weight") == null

    private fun isFitnessGoalsValid() = _uiState.value.fitnessGoals.isNotEmpty() &&
            _uiState.value.activityLevel.isNotEmpty()

    private fun isDietaryPreferencesValid() = _uiState.value.dietaryPreferences.isNotEmpty() &&
            _uiState.value.workoutPreferences.isNotEmpty()

    fun canProceedToNextStep(currentStep: Int): Boolean {
        return when (currentStep) {
            0 -> isBasicInfoValid()
            1 -> isPhysicalMeasurementsValid()
            2 -> isFitnessGoalsValid()
            3 -> isDietaryPreferencesValid()
            else -> false
        }
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

    suspend fun getUserSettings(dataStore: DataStore<Preferences>) {
        getUserSettingsFlow(dataStore).collectLatest { userSettings ->
            userId = userSettings.userId
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