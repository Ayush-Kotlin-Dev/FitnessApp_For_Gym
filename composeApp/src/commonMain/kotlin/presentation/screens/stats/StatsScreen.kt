package presentation.screens.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import presentation.screens.homescreen.HomeScreenViewModel

class StatsScreen: Screen {
    @Composable
    override fun Content() {
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()
        val navigator = LocalNavigator.current
        Column {
            Text("Stats Screen")
        }
    }
}