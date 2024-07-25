package presentation.screens.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.FadeTransition
import presentation.screens.homescreen.HomeScreenViewModel


object StatsTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Stats"
            val icon = rememberVectorPainter(Icons.Default.Search)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(StatsScreen()){navigator ->
            FadeTransition(navigator)
        }
    }
}

class StatsScreen: Screen {
    @Composable
    override fun Content() {
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()
        val currentScreen by homeScreenViewModel.currentScreen.collectAsState()

        val navigator = LocalNavigator.current
        Column {
            Text(currentScreen.toString())


            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            Button(onClick = {
                navigator?.push(StatsDetailedScreen())
            }) {
                Text("Go to Stats Detailed Screen ")
            }
        }
    }
}


class StatsDetailedScreen: Screen {
    @Composable
    override fun Content() {
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()
        val currentScreen by homeScreenViewModel.currentScreen.collectAsState()

        LaunchedEffect(Unit) {
            homeScreenViewModel.updateCurrentScreen(StatsDetailedScreen())
        }
        Text(currentScreen.toString())


    }
}