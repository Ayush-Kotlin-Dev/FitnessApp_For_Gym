package presentation.screens.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import presentation.screens.plans.SelectedRoutineScreen

class PlansTab(
    private val onNavigator: (Boolean) -> Unit
) : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Plans"
            val icon = rememberVectorPainter(Icons.Default.ShoppingCart)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }


    @Composable
    override fun Content() {
        val sharedViewModel = koinScreenModel<SharedWorkoutViewModel>()
        Navigator(SelectedRoutineScreen()){ navigator ->
            LaunchedEffect(navigator.lastItem){
                println("qsddqsdsqdsqdsqdsq"+navigator.lastItem)
                onNavigator(navigator.lastItem is SelectedRoutineScreen)
            }
            SlideTransition( navigator)
        }
    }
}