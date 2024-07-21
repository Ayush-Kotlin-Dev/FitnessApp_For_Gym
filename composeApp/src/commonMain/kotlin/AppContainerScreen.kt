import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import presentation.screens.HomeScreen.HomeScreen
import presentation.screens.auth_onboard.AuthScreen
import presentation.screens.auth_onboard.login.LoginScreen

class AppContainerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<AppContainerViewModel>()
        val userSettings = viewModel.userSettingsState.value
        val isLoading = viewModel.isLoading

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LaunchedEffect(userSettings?.token) {
                if (userSettings?.token.isNullOrEmpty()) {
                    navigator?.replace(AuthScreen())
                } else {
                    if(userSettings?.isFormFilled == true) {
                        navigator?.replace(HomeScreen())
                    } else {
                        navigator?.replace(UserInfoFormScreen())
                    }
                }
            }
        }
    }
}
