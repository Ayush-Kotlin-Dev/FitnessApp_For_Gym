package presentation.screens.homescreen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import presentation.screens.plans.PlanDetailScreen
import presentation.screens.plans.PlansTab
import presentation.screens.profile.ProfileTab
import presentation.screens.stats.StatsDetailedScreen
import presentation.screens.stats.StatsTab

class HomeScreenViewModel : ScreenModel {

    private val _currentScreen = MutableStateFlow<Screen>(HomeTab)
    val currentScreen = _currentScreen.asStateFlow()

    fun updateCurrentScreen(screen: Screen) {
        println("HomeScreenViewModel: updating current screen to $screen")
        _currentScreen.value = screen
    }

    fun shouldShowBars(screen: Screen): Boolean {
        return when (screen) {
            is HomeTab, is StatsTab, is PlansTab, is ProfileTab -> true
            is PlanDetailScreen -> false
            is StatsDetailedScreen -> false
            else -> false
        }
    }
}