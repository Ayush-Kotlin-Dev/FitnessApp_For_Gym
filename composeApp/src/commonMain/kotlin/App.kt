import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import di.apiModule
import di.appModule
import di.repositoryModule
import di.useCaseModule
import di.viewModelModule
import org.koin.core.context.startKoin
import ui.GymAppTheme

@Composable
fun App() {
    initKoin()
    GymAppTheme {
        Surface {
            Navigator(UserInfoFormScreen()) { navigator ->
                FadeTransition(navigator)
            }
        }
    }
}

fun initKoin() {
    startKoin {
        modules(
            appModule,
            apiModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }
}