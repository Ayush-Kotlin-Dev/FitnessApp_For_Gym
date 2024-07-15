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
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isAuthenticating = true)
            val authResultData = signUpUseCase(
                _uiState.value.emailOrUsername,
                _uiState.value.emailOrUsername,
                _uiState.value.password
            )
            _uiState.value = when (authResultData) {
                is Result.Error -> {
                    _uiState.value.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.message ?: "An error occurred"
                    )
                }
                is Result.Success -> {
                    val isFormFilled = authResultData.data?.isFormFilled
                    _uiState.value.copy(
                        authenticationSucceed = true,
                        isAuthenticating = false,
                        isFormFilled = isFormFilled ?: false
                    )
                }
                is Result.Loading -> {
                    _uiState.value.copy(isAuthenticating = true)
                }
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
