package presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.local.getUserInfoFlow
import domain.RealmManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val dataStore: DataStore<Preferences>,
    private val realmManager: RealmManager
) : ScreenModel {

    //get user settings
    private val _userSettingsState = mutableStateOf<ProfileUiState>(ProfileUiState())
    val userSettingsState: ProfileUiState by _userSettingsState



    init {

        println("ProfileScreenViewModel init")
        //get user settings
        screenModelScope.launch {
            getUserInfoFlow(dataStore).collectLatest { settings ->
                _userSettingsState.value = ProfileUiState(isLoading = true)
                _userSettingsState.value = ProfileUiState(
                    fullName = settings.fullName,
                    age = settings.age,
                    gender = settings.gender,
                    height = settings.height,
                    weight = settings.weight,
                    fitnessGoals = settings.fitnessGoals,
                    activityLevel = settings.activityLevel,
                    dietaryPreferences = settings.dietaryPreferences,
                    workoutPreferences = settings.workoutPreferences
                )
                _userSettingsState.value = _userSettingsState.value.copy(isLoading = false)
            }
        }
    }

    fun clearRealmDb() {
        screenModelScope.launch {
            realmManager.clear()
        }
    }


}

data class ProfileUiState(
    val fullName: String = "",
    val age: Int = 0,
    val gender : String = "",
    val height: Float = 0f,
    val weight: Float = 0f,
    val fitnessGoals: String = "",
    val activityLevel: String = "",
    val dietaryPreferences: String = "",
    val workoutPreferences: String = "",
    val isLoading : Boolean = true

)