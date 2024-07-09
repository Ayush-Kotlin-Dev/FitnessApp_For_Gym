import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import presentation.screens.auth_onboard.AuthScreen
import presentation.screens.auth_onboard.login.LoginScreen
import ui.GymAppTheme

@Composable
fun App() {
    GymAppTheme{
        Navigator(AuthScreen()){ navigator ->
            FadeTransition(navigator)
        }
    }
}