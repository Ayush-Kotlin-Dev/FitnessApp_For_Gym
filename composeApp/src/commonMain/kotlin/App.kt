import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.navigator.Navigator
import di.apiModule
import di.appModule
import di.repositoryModule
import di.useCaseModule
import di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.dsl.module
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