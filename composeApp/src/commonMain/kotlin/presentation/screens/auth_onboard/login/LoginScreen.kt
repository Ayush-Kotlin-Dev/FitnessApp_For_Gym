package presentation.screens.auth_onboard.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import presentation.components.CustomTextFields
import presentation.screens.auth_onboard.signup.SignUpScreen

class LoginScreen : Screen {
    // Login screen implementation
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = rememberScreenModel { LoginScreenViewModel()}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomTextFields(
                value = viewModel.uiState.value.emailOrUsername,
                onValueChange = { viewModel.onEmailOrUsernameChange(it) },
                hint = "Email or Username",
                leadingIcon = null
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextFields(
                value = viewModel.uiState.value.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                hint = "Password",
                isPasswordTextField = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.login()
            }) {
                Text("Login")
            }

            TextButton(onClick = { navigator?.push(SignUpScreen())}) {
                Text(text = "Don't have an account? Sign up")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Forgot password? Reset here")
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