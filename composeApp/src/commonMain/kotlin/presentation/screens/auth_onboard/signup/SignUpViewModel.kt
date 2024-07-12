package presentation.screens.auth_onboard.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignupViewModel : ScreenModel {

    private val _uiState = mutableStateOf(SignUpUiState())
    val uiState: State<SignUpUiState> = _uiState

    fun onFullNameChange(newFullName: String) {
        _uiState.value = _uiState.value.copy(fullName = newFullName)
    }

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
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisibility = !_uiState.value.passwordVisibility)
    }
}


data class SignUpUiState(
    val fullName: String = "",
    val emailOrUsername: String = "",
    val password: String = "",
    val isAuthenticating: Boolean = false,
    val authErrorMessage: String? = null,
    val authenticationSucceed: Boolean = false,
    val passwordVisibility: Boolean = true,
)
