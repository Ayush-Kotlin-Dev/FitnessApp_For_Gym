package presentation.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow

class ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<ProfileScreenViewModel>()
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profile Screen")

            Button(onClick = {
                navigator.push(DetailsScreen(id = 1))
                //to change the visibility of the tab navigation
            }) {
                Text(text = "Go to example details")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = viewModel.userSettingsState.isLoading) {
                Text("Loading...")
            }

            AnimatedVisibility(visible = !viewModel.userSettingsState.isLoading) {
                Column {
                    Text("Full Name: ${viewModel.userSettingsState.fullName}")
                    Text("Age: ${viewModel.userSettingsState.age}")
                    Text("Gender: ${viewModel.userSettingsState.gender}")
                    Text("Height: ${viewModel.userSettingsState.height}")
                    Text("Weight: ${viewModel.userSettingsState.weight}")
                    Text("Fitness Goals: ${viewModel.userSettingsState.fitnessGoals}")
                    Text("Activity Level: ${viewModel.userSettingsState.activityLevel}")
                    Text("Dietary Preferences: ${viewModel.userSettingsState.dietaryPreferences}")
                    Text("Workout Preferences: ${viewModel.userSettingsState.workoutPreferences}")
                }
            }
        }
    }
}
