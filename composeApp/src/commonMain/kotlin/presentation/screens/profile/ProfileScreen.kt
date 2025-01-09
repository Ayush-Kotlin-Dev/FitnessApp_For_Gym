package presentation.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    QuickStats(viewModel)
                }
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
fun QuickStats(viewModel: ProfileScreenViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            value = "22",
            label = "Workouts",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = "3",
            label = "Week Streak",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = "12",
            label = "Goals Met",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryTextColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryTextColor
            )
        }
    }
}

@Composable
fun ProfileHeader(viewModel: ProfileScreenViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AccentColor,
                            AccentColor.copy(alpha = 0.8f)
                        )
                    )
                )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileBadge(text = "${viewModel.userSettingsState.age} years")
            ProfileBadge(text = viewModel.userSettingsState.gender)
            ProfileBadge(text = viewModel.userSettingsState.activityLevel)
        }
    }
}

@Composable
fun ProfileBadge(text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = SecondaryTextColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun StatsSection(viewModel: ProfileScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Body Metrics",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    title = "Height",
                    value = "${viewModel.userSettingsState.height} cm",
                )
                StatItem(
                    title = "Weight",
                    value = "${viewModel.userSettingsState.weight} kg",
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            BMIStatItem(viewModel.userSettingsState.height, viewModel.userSettingsState.weight)
        }
    }
}

@Composable
fun StatItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryTextColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = SecondaryTextColor
        )
    }
}

@Composable
fun PreferencesSection(viewModel: ProfileScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            PreferenceItem(
                title = "Fitness Goals",
                value = viewModel.userSettingsState.fitnessGoals
            )
            PreferenceItem(
                title = "Activity Level",
                value = viewModel.userSettingsState.activityLevel
            )
            PreferenceItem(
                title = "Dietary",
                value = viewModel.userSettingsState.dietaryPreferences
            )
            PreferenceItem(
                title = "Workout Frequency",
                value = viewModel.userSettingsState.workoutPreferences
            )
        }
    }
}

@Composable
fun PreferenceItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryTextColor
            )
        }
    }
}

@Composable
fun ActionsSection(viewModel: ProfileScreenViewModel, navigator: Navigator) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { /* TODO: Implement edit profile functionality */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.clearRealmDb() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clear Database", color = Color.White)
            }
        }
    }
}

@Composable
fun BMIStatItem(height: Float, weight: Float) {
    val bmi = calculateBMI(height, weight)
    val bmiValue = formatBmi(bmi)
    val bmiCategory = getBMICategory(bmi)
    val bmiColor = getBMIColor(bmiCategory)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BMI",
                    style = MaterialTheme.typography.titleMedium,
                    color = SecondaryTextColor
                )
            }

            Text(
                text = bmiValue,
                style = MaterialTheme.typography.headlineMedium,
                color = PrimaryTextColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Keep the original progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFF2A2A2A), RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(getBMIProgress(bmi))
                        .height(8.dp)
                        .background(bmiColor, RoundedCornerShape(4.dp))
                )
            }

            Text(
                text = bmiCategory,
                style = MaterialTheme.typography.bodyLarge,
                color = bmiColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Keep the existing helper functions
private fun getBMIProgress(bmi: Float): Float {
    return when {
        bmi < 18.5 -> 0.25f
        bmi < 24.9 -> 0.5f
        bmi < 29.9 -> 0.75f
        else -> 1f
    }
}

private fun getBMIColor(category: String): Color {
    return when (category) {
        "Underweight" -> Color(0xFFFFA500) // Orange
        "Normal" -> Color(0xFF4CAF50) // Green
        "Overweight" -> Color(0xFFFFA500) // Orange
        else -> Color(0xFFFF0000) // Red
    }
}

private fun getBMICategory(bmi: Float): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 24.9 -> "Normal"
        bmi < 29.9 -> "Overweight"
        else -> "Obese"
    }
}

private fun calculateBMI(height: Float, weight: Float): Float {
    val heightInMeters = height / 100
    return weight / (heightInMeters * heightInMeters)
}