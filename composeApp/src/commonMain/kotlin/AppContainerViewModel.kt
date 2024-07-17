import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.local.UserSettings
import data.local.getUserSettingsFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppContainerViewModel(
    private val dataStore: DataStore<Preferences>
) : ScreenModel {
    private val _userSettingsState = mutableStateOf<UserSettings?>(null)
    val userSettingsState: State<UserSettings?> = _userSettingsState

    private val _isLoading = mutableStateOf(true)
    val isLoading: Boolean by _isLoading

    init {
        screenModelScope.launch {
            getUserSettingsFlow(dataStore).collectLatest { settings ->
                _userSettingsState.value = settings
                _isLoading.value = false
            }
        }
    }
}
