import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.navigator.Navigator
import data.auth.domain.AuthRepository
import di.apiModule
import di.appModule
import di.repositoryModule
import di.useCaseModule
import di.viewModelModule
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screens.auth_onboard.login.LoginScreen
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import ui.GymAppTheme

@Composable
fun App(
    prefs: DataStore<Preferences>
) {
    initKoin(prefs)
    GymAppTheme {
        Surface {
            Navigator(AppContainerScreen())
        }
    }
}


fun initKoin(
    prefs: DataStore<Preferences>
) {
    startKoin {
        modules(
            appModule,
            module {
                single { prefs }
            },
            apiModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }
}