package presentation.screens.plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import presentation.screens.homescreen.HomeScreenViewModel

class PlanSelectionScreen : Screen {
    @Composable
    override fun Content() {

        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()
        val plans = listOf(
            "5-Day Split",
            "6-Day Push/Pull/Legs",
            "4-Day Upper/Lower",
            "5-Day Upper/Lower/Full",
            "4-Day Push/Pull",
            "6-Day Body Part Split"
        )


        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Select Workout Plan", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            val navigator = LocalNavigator.currentOrThrow
            plans.forEach { plan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navigator.push(PlanDetailScreen(plan , homeScreenViewModel::updateCurrentScreen))
                        }
                ) {
                    Text(plan, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
