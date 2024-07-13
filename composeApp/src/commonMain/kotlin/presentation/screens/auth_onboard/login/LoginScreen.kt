package presentation.screens.auth_onboard.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.img
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import presentation.components.PasswordEyeIcon

class LoginScreen : Screen {
    // Login screen implementation
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<LoginScreenViewModel>()
        var isPasswordVisible by remember { mutableStateOf(false) }
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

            TextField(
                value = viewModel.uiState.value.emailOrUsername,
                onValueChange = { viewModel.onEmailOrUsernameChange(it) },
                label = { Text("Email or Username") },
                colors = textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = viewModel.uiState.value.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                colors = textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                ),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    PasswordEyeIcon(
                        isPasswordVisible = isPasswordVisible,
                        onPasswordToggleClick = { isPasswordVisible = !isPasswordVisible }
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                enabled = !viewModel.uiState.value.isAuthenticating,
                onClick = { viewModel.login() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                ),
                border = BorderStroke(2.dp, Color.Gray)

            ) {
                Text(
                    text = "Login",
                    color = Color.Red.copy(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Forgot password?")
            }
            if (viewModel.uiState.value.isAuthenticating) {
                CircularProgressIndicator()
            }
            viewModel.uiState.value.authErrorMessage?.let {
                Text(text = it)
            }
        }
    }
}
