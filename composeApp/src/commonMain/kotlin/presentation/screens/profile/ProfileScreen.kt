package presentation.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import presentation.screens.plans.AccentColor
import presentation.screens.plans.BackgroundColor
import presentation.screens.plans.CardBackgroundColor
import presentation.screens.plans.PrimaryTextColor
import presentation.screens.plans.SecondaryTextColor

class ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<ProfileScreenViewModel>()
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                ProfileHeader(viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                StatsSection(viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                PreferencesSection(viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                ActionsSection(viewModel, navigator)
            }
        }
    }
}

@Composable
fun ProfileHeader(viewModel: ProfileScreenViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(AccentColor)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                tint = Color.White,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = viewModel.userSettingsState.fullName,
            style = MaterialTheme.typography.headlineMedium,
            color = PrimaryTextColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${viewModel.userSettingsState.age} years old • ${viewModel.userSettingsState.gender}",
            style = MaterialTheme.typography.bodyLarge,
            color = SecondaryTextColor
        )
    }
}

@Composable
fun StatsSection(viewModel: ProfileScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Your Stats",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(
                    title = "Height",
                    value = viewModel.userSettingsState.height.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    title = "Weight",
                    value = viewModel.userSettingsState.weight.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    //calculate BMI
                    title = "BMI",
                    value =  calculateBMI(viewModel.userSettingsState.height, viewModel.userSettingsState.weight) ,
                    modifier = Modifier.weight(1f)
                )

            }
        }
    }
}

private fun calculateBMI(height: Float, weight: Float): String {
    val heightInMeters = height / 100 // Assuming height is in centimeters
    val bmi = weight / (heightInMeters * heightInMeters)
    return formatBmi(bmi)
}

expect fun formatBmi(bmi: Float): String

@Composable
fun StatItem(title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, color = SecondaryTextColor)
        Text(text = value, style = MaterialTheme.typography.titleLarge, color = PrimaryTextColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PreferencesSection(viewModel: ProfileScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Your Preferences",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            PreferenceItem("Fitness Goals", viewModel.userSettingsState.fitnessGoals)
            PreferenceItem("Activity Level", viewModel.userSettingsState.activityLevel)
            PreferenceItem("Dietary Preferences", viewModel.userSettingsState.dietaryPreferences)
            PreferenceItem("Workout Preferences", viewModel.userSettingsState.workoutPreferences)
        }
    }
}

@Composable
fun PreferenceItem(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, color = SecondaryTextColor)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = PrimaryTextColor)
    }
}

@Composable
fun ActionsSection(viewModel: ProfileScreenViewModel, navigator: Navigator) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { /* TODO: Implement edit profile functionality */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
        ) {
            Text("Edit Profile", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.clearRealmDb()
                // TODO: Implement logic to change the visibility of the tab navigation
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
        ) {
            Text("Clear Database", color = Color.White)
        }
    }
}