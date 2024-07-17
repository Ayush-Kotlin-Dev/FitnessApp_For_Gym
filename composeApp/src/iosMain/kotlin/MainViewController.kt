import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import data.local.createDataStore

fun MainViewController() = ComposeUIViewController {
    App(
        prefs = remember {
            createDataStore()
        }
    )
}