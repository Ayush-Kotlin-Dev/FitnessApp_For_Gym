import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import kotlinx.coroutines.launch
import presentation.models.UserData
import presentation.screens.auth_onboard.UserInfoForm.UserInfoDataViewModel

@OptIn(ExperimentalFoundationApi::class)
class UserInfoFormScreen : Screen {
    @Composable
    override fun Content() {
        val pagerState = rememberPagerState(pageCount = { 4 })
        val coroutineScope = rememberCoroutineScope()
        val viewModel = koinScreenModel<UserInfoDataViewModel>()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> BasicInfoStep(viewModel)
                    1 -> PhysicalMeasurementsStep(viewModel)
                    2 -> FitnessGoalsActivityLevelStep(viewModel)
                    3 -> DietaryWorkoutPreferencesStep(viewModel)
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (pagerState.currentPage > 0) {
                    Button(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }) {
                        Text("Previous")
                    }
                }

                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    Button(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                        Text("Next")
                    }
                } else {
                    Button(onClick = { viewModel.submitUserData() }) {
                        Text("Submit")
                    }
                }
            }
        }
    }

    @Composable
    fun BasicInfoStep(viewModel: UserInfoDataViewModel) {
        var expanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = viewModel.uiState.value.fullName,
                onValueChange = { viewModel.onFullNameChange(it) },
                label = { Text("Full Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.uiState.value.age.toString(),
                onValueChange = { viewModel.onAgeChange(it.toInt()) },
                label = { Text("Age") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            val genders = listOf("Male", "Female", "Transgender")

            Text("Gender")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { expanded = true }) {
                    Text(viewModel.uiState.value.gender.ifEmpty { "Select Gender" })
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genders.forEach { gender ->
                        DropdownMenuItem(onClick = {
                            viewModel.onGenderChange(gender)
                        }) {
                            Text(gender)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PhysicalMeasurementsStep(viewModel: UserInfoDataViewModel) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = viewModel.uiState.value.height.toString(),
                onValueChange = { viewModel.onHeightChange(it.toFloat()) },
                label = { Text("Height (cm)") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.uiState.value.weight.toString(),
                onValueChange = { viewModel.onWeightChange(it.toFloat()) },
                label = { Text("Weight (kg)") }
            )
        }
    }

    @Composable
    fun FitnessGoalsActivityLevelStep(viewModel: UserInfoDataViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val fitnessGoals = listOf("Weight Loss", "Weight Gain", "Muscle Gain", "General Fitness")

        Column(modifier = Modifier.fillMaxSize()) {
            Text("Fitness Goals")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { expanded = true }) {
                    Text(viewModel.uiState.value.fitnessGoals.ifEmpty { "Select Fitness Goal" })
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    fitnessGoals.forEach { goal ->
                        DropdownMenuItem(onClick = {
                            viewModel.setFitnessGoals(goal)
                            expanded = false
                        }) {
                            Text(goal)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Activity Level")
            val activityLevels = listOf(
                "Sedentary",
                "Lightly active",
                "Moderately active",
                "Very active",
                "Super active"
            )
            var selectedActivityLevel by  remember { mutableStateOf("") }

            activityLevels.forEach { level ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedActivityLevel == level,
                        onClick = { selectedActivityLevel = level }
                    )
                    Text(level)
                }
            }
            viewModel.setActivityLevel(selectedActivityLevel)
        }
    }

    @Composable
    fun DietaryWorkoutPreferencesStep(viewModel: UserInfoDataViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val dietaryPreferences = listOf("Vegetarian", "Vegan", "Non-Vegetarian")

        Column(modifier = Modifier.fillMaxSize()) {
            Text("Dietary Preferences")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { expanded = true }) {
                    Text(viewModel.uiState.value.dietaryPreferences.ifEmpty { "Select Dietary Preference" })
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    dietaryPreferences.forEach { preference ->
                        DropdownMenuItem(onClick = {
                            viewModel.setDietaryPreferences(preference)
                            expanded = false
                        }) {
                            Text(preference)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = viewModel.uiState.value.workoutPreferences,
                onValueChange = { viewModel.setWorkoutPreferences(it) },
                label = { Text("Workout Preferences") }
            )
        }
    }
}
