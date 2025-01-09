package presentation.screens.auth_onboard.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.usecases.auth.SignUpUseCase
import util.Result
import kotlinx.coroutines.launch

class SignupViewModel(
    private val signUpUseCase: SignUpUseCase
) : ScreenModel {

    private val _uiState = mutableStateOf(SignUpUiState())
    val uiState: State<SignUpUiState> = _uiState

    private fun validateInput(): Boolean {
        when {
            uiState.value.fullName.length < 3 -> {
                _uiState.value = _uiState.value.copy(
                    authErrorMessage = "Username must be at least 3 characters"
                )
                return false
            }
            !uiState.value.emailOrUsername.contains("@") -> {
                _uiState.value = _uiState.value.copy(
                    authErrorMessage = "Please enter a valid email address"
                )
                return false
            }
            uiState.value.password.length < 6 -> {
                _uiState.value = _uiState.value.copy(
                    authErrorMessage = "Password must be at least 6 characters"
                )
                return false
            }
        }
        return true
    }

    fun onFullNameChange(newFullName: String) {
        _uiState.value = _uiState.value.copy(fullName = newFullName)
    }

    fun onEmailOrUsernameChange(newEmailOrUsername: String) {
        _uiState.value = _uiState.value.copy(emailOrUsername = newEmailOrUsername)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun signUp() {
        if (!validateInput()) return

        screenModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isAuthenticating = true,
                    authErrorMessage = null // Clear previous errors
                )

                val authResultData = signUpUseCase(
                    _uiState.value.emailOrUsername,
                    _uiState.value.fullName,
                    _uiState.value.password
                )

                _uiState.value = when (authResultData) {
                    is Result.Error -> _uiState.value.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.message ?: "An error occurred"
                    )
                    is Result.Success -> _uiState.value.copy(
                        authenticationSucceed = true,
                        isAuthenticating = false,
                        isFormFilled = authResultData.data?.isFormFilled ?: false,
                        authErrorMessage = null
                    )
                    is Result.Loading -> _uiState.value.copy(isAuthenticating = true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isAuthenticating = false,
                    authErrorMessage = "An unexpected error occurred"
                )
            }
        }
    }
}

data class SignUpUiState(
    val fullName: String = "",
    val emailOrUsername: String = "",
    val password: String = "",
    val isAuthenticating: Boolean = false,
    val authErrorMessage: String? = null,
    val authenticationSucceed: Boolean = false,
    val isFormFilled: Boolean = false
)