package presentation.screens.auth_onboard.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.usecases.auth.SignInUseCase
import domain.usecases.userinfo.GetUserInfoUseCase
import kotlinx.coroutines.launch
import util.Result

class LoginScreenViewModel(
    private val signInUseCase: SignInUseCase,
    private val GetUserInfoUseCase: GetUserInfoUseCase
) : ScreenModel {
    private val _uiState = mutableStateOf(LoginUiState())
    val uiState: State<LoginUiState> = _uiState

    // Add validation
    private fun validateInput(): Boolean {
        if (uiState.value.emailOrUsername.isBlank()) {
            _uiState.value = _uiState.value.copy(
                authErrorMessage = "Email or username cannot be empty"
            )
            return false
        }
        if (uiState.value.password.length < 6) {
            _uiState.value = _uiState.value.copy(
                authErrorMessage = "Password must be at least 6 characters"
            )
            return false
        }
        return true
    }

    fun onEmailOrUsernameChange(newEmailOrUsername: String) {
        _uiState.value = _uiState.value.copy(emailOrUsername = newEmailOrUsername)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun login() {
        if (!validateInput()) return

        screenModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isAuthenticating = true,
                    authErrorMessage = null // Clear previous errors
                )
                
                val authResultData = signInUseCase(uiState.value.emailOrUsername, uiState.value.password)
                
                _uiState.value = when (authResultData) {
                    is Result.Error -> _uiState.value.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.message ?: "An error occurred"
                    )
                    is Result.Success -> {
                        val isFormFilled = authResultData.data?.isFormFilled
                        if(isFormFilled == true) {
                            GetUserInfoUseCase(authResultData.data.userId)
                        }
                        _uiState.value.copy(
                            authenticationSucceed = true,
                            isAuthenticating = false,
                            isFormFilled = isFormFilled ?: false,
                            authErrorMessage = null
                        )
                    }
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

data class LoginUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val isAuthenticating: Boolean = false,
    val authErrorMessage: String? = null,
    val authenticationSucceed: Boolean = false,
    val isFormFilled: Boolean = false
)