package presentation.screens.auth_onboard.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import presentation.screens.auth_onboard.signup.SignUpScreen

class LoginScreen : Screen {
    // Login screen implementation
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),

        ) {
            Button(onClick = {
                navigator?.push(SignUpScreen("Ayush"))
            }) {
                Text("Navigate to SignUp")
            }
        }

    }
}