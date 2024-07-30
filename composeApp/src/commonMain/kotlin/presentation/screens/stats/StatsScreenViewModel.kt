package presentation.screens.stats

import cafe.adriel.voyager.core.model.ScreenModel

class StatsScreenViewModel : ScreenModel {

    val stats = listOf(
        "Total Workouts: 10",
        "Total Time: 10h 30m",
        "Total Calories: 1000",
        "Total Distance: 100km"
    )
    init {
        println("StatsScreenViewModel init")
    }

    override fun onDispose() {
        println("StatsScreenViewModel disposed")
    }
}