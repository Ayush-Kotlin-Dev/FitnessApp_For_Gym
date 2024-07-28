package presentation.screens.plans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
        ) {
            Text("Select Workout Plan", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            val navigator = LocalNavigator.currentOrThrow
            plans.forEach { plan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navigator.push(PlanDetailScreen(plan))
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f),
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                plan,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "View Details",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Red.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Normal
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "Navigate to details",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
