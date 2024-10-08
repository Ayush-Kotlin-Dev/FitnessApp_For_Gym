package presentation.screens.auth_onboard.login

import ContentWithMessageBar
import UserInfoFormScreen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.img
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import presentation.components.CustomTextField
import presentation.screens.homescreen.HomeScreen
import presentation.screens.tabs.HomeTab
import presentation.screens.tabs.TabsScreen
import rememberMessageBarState

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<LoginScreenViewModel>()
        val isPasswordVisible = remember { mutableStateOf(false) }
        val uiState = viewModel.uiState.value

        LoginContent(
            uiState = uiState,
            isPasswordVisible = isPasswordVisible.value,
            onPasswordVisibilityChanged = { isPasswordVisible.value = it },
            onEmailOrUsernameChange = viewModel::onEmailOrUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = { viewModel.login() },
            onForgotPasswordClick = { /* TODO */ }
        ) {
            if (uiState.authenticationSucceed) {
                if (uiState.isFormFilled) {
                    navigator?.replaceAll(TabsScreen())
                } else {
                    navigator?.replaceAll(UserInfoFormScreen())
                }
            }
        }
    }
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChanged: (Boolean) -> Unit,
    onEmailOrUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    val state = rememberMessageBarState()

    ContentWithMessageBar(errorMaxLines = 2, messageBarState = state) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .size(320.dp)
                    .padding(30.dp)
            )

            CustomTextField(
                value = uiState.emailOrUsername,
                onValueChange = onEmailOrUsernameChange,
                label = "Email or Username",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = "Password",
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isPasswordTextField = true,
                keyboardType = KeyboardType.Password,
                isPasswordVisible = isPasswordVisible,
                onPasswordVisibilityToggle = { onPasswordVisibilityChanged(!isPasswordVisible) }
            )

            Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isAuthenticating -> {
                CircularProgressIndicator()
            }
            uiState.authenticationSucceed -> {
                onAuthSuccess()
            }
            else -> {
                OutlinedButton(
                    onClick = onLoginClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(2.dp, Color.Gray)
                ) {
                    Text(
                        text = if (uiState.authErrorMessage != null) "Retry" else "Login",
                        color = Color.Red.copy(0.9f)
                    )
                }
            }
        }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onForgotPasswordClick) {
                Text(text = "Forgot password?")
            }
        }
    }


    LaunchedEffect(uiState.authErrorMessage) {
        uiState.authErrorMessage?.let {
            state.addError(exception = Exception(it))
        }
    }
}