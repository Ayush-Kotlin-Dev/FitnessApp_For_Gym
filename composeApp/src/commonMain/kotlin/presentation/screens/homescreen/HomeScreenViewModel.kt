package presentation.screens.homescreen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import presentation.screens.plans.PlanDetailScreen
import presentation.screens.plans.PlansTab
import presentation.screens.profile.ProfileTab
import presentation.screens.stats.StatsDetailedScreen
import presentation.screens.stats.StatsTab

class HomeScreenViewModel : ScreenModel {

        private val _currentScreen = MutableStateFlow<Screen>(HomeTab)
        val currentScreen: StateFlow<Screen> = _currentScreen

        fun setCurrentScreen(screen: Screen) {
                _currentScreen.value = screen
        }

}