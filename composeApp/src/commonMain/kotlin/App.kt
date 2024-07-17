import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.navigator.Navigator
import di.appModule
import org.koin.core.context.startKoin
import ui.GymAppTheme

@Composable
fun App(
    prefs: DataStore<Preferences>
) {
    initKoin()
    GymAppTheme {
        Surface {
            Navigator(UserInfoFormScreen())

        }
    }
}

fun initKoin() {
    startKoin {
        modules(
            appModule,
//            apiModule,
//            repositoryModule,
//            useCaseModule,
//            viewModelModule
        )
    }
}