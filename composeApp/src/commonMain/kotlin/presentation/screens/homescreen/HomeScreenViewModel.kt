package presentation.screens.homescreen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import presentation.screens.tabs.HomeTab

class HomeScreenViewModel : ScreenModel {
    init {
        println("HomeScreenViewModel init")
    }
    val  testMessage = MutableStateFlow("Hello from HomeScreenViewModel")

    override fun onDispose() {
        println("HomeScreenViewModel disposed")
    }
}