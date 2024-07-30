package presentation.screens.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

class StatsScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel =  koinScreenModel<StatsScreenViewModel>()
        Column {
            Text("Stats Screen")
            viewModel.stats.forEach { stat ->
                Text(stat)
            }
        }
    }
}