package presentation.screens.auth_onboard.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator


 class SignUpScreen : Screen {
    // Login screen implementation
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("SignUp Screen")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator?.pop()
                        })
                        {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },

            ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("SignUp Screen")
                Button(onClick = {
                    // Navigate to SignUpScreen
                }) {
                    Text("Name :")
                }
            }
        }


    }
}