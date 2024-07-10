package presentation.screens.auth_onboard.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginScreenViewModel : ScreenModel {
    // Login screen view model implementation
    fun login() {
        // Login logic
        //simulate network call
        CoroutineScope(Dispatchers.IO).launch {
            //simulate network call
            delay(2000)

        }

    }
}