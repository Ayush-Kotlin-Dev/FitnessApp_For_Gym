package presentation.screens.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import presentation.screens.homescreen.HomeScreenViewModel

class TabsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val sharedWorkoutViewModel = koinScreenModel<SharedWorkoutViewModel>()
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()
        var isVisible by remember { mutableStateOf(true) }
        val plansTab = remember {
            PlansTab(
                onNavigator = { isVisible = it }
            )
        }


        TabNavigator(HomeTab) {
            Scaffold(
                content = {
                        CurrentTab()

                },
                bottomBar = {
                    AnimatedVisibility(visible = isVisible , enter = slideInVertically { height -> height } , exit = slideOutVertically { height -> height }) {
                        BottomNavigationBar( plansTab)
                    }
                }
            )
        }
    }


    @Composable
    private fun BottomNavigationBar(
        plansTab : Tab
    ) {
        NavigationBar(
            containerColor = Color.Black,
            tonalElevation = 8.dp,
        ) {
            TabNavigationItem(HomeTab )
            TabNavigationItem(StatsTab)
            TabNavigationItem(plansTab)
            TabNavigationItem(ProfileTab)
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(
        tab: Tab,
    ) {
        val tabNavigator: TabNavigator = LocalTabNavigator.current
        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = {
                tabNavigator.current = tab
            },
            icon = {
                tab.options.icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = tab.options.title
                    )
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red.copy(alpha = 0.7f),
                unselectedIconColor = Color.White.copy(alpha = 0.7f)
            ),
            label = { Text(text = tab.options.title) },
            alwaysShowLabel = false,
        )
    }
}