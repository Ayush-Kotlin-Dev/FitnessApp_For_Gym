package presentation.screens.auth_onboard.signup

// Login screen implementation
import UserInfoFormScreen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import presentation.components.CustomTextField

class SignUpScreen : Screen {
    // Login screen implementation
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<SignupViewModel>()
        val isPasswordVisible = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.img),
                contentDescription = null,
                modifier = Modifier.size(320.dp).padding(30.dp)
            )

            //full name
            CustomTextField(
                value = viewModel.uiState.value.fullName,
                onValueChange = { viewModel.onFullNameChange(it) },
                label = "Username",
                keyboardType = KeyboardType.Text,
                )
            CustomTextField(
                value = viewModel.uiState.value.emailOrUsername,
                onValueChange = { viewModel.onEmailOrUsernameChange(it) },
                label = "Email",
                keyboardType = KeyboardType.Email,
                )
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(
                value = viewModel.uiState.value.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Password",
                visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                isPasswordTextField = true,
                keyboardType = KeyboardType.Password,
                isPasswordVisible = isPasswordVisible.value,
                onPasswordVisibilityToggle = { isPasswordVisible.value = !isPasswordVisible.value }
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                viewModel.uiState.value.isAuthenticating -> {
                    CircularProgressIndicator()
                }
                viewModel.uiState.value.authenticationSucceed -> {
                    if (viewModel.uiState.value.isFormFilled) {
//                        navigator?.navigateToHome()
                        navigator?.replaceAll(UserInfoFormScreen())
                    } else {
                        navigator?.replaceAll(UserInfoFormScreen())
                    }
                }
            }
            OutlinedButton(
                enabled = !viewModel.uiState.value.isAuthenticating,
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.signUp()
                        if (viewModel.uiState.value.authenticationSucceed) {
                            navigator?.push(UserInfoFormScreen())
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                ),
                border = BorderStroke(2.dp, Color.Gray)

            ) {
                Text(
                    text = if(viewModel.uiState.value.isAuthenticating) "Signing up..." else if (viewModel.uiState.value.authErrorMessage!=null) "Retry" else "Sign up",
                    color = Color.Red.copy(0.9f)
                )
            }
            viewModel.uiState.value.authErrorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))



        }
    }
}
