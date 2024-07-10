package presentation.screens.auth_onboard.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginScreenViewModel : ScreenModel {
    private val _uiState = mutableStateOf(LoginUiState())
    val uiState: State<LoginUiState> = _uiState

    fun onEmailOrUsernameChange(newEmailOrUsername: String) {
        _uiState.value = _uiState.value.copy(emailOrUsername = newEmailOrUsername)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun login() {
        // Implement login logic
        screenModelScope.launch {
            // Set isAuthenticating to true
            _uiState.value = _uiState.value.copy(isAuthenticating = true)

            // Perform login logic here
            delay(2000)

            // Set isAuthenticating to false
            _uiState.value = _uiState.value.copy(isAuthenticating = false)
        }
    }
}


data class LoginUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val isAuthenticating: Boolean = false,
    val authErrorMessage: String? = null,
    val authenticationSucceed: Boolean = false
)
