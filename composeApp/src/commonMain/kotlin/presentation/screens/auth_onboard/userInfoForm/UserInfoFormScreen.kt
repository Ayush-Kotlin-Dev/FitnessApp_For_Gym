import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import avikfitness.composeapp.generated.resources.Res
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import presentation.components.CustomTextField
import presentation.screens.auth_onboard.userInfoForm.UserInfoFormViewModel
import presentation.screens.tabs.TabsScreen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
class UserInfoFormScreen : Screen {
    @Composable
    override fun Content() {
        val pagerState = rememberPagerState(pageCount = { 4 })
        val coroutineScope = rememberCoroutineScope()
        val viewModel = koinScreenModel<UserInfoFormViewModel>()
        val navigator = LocalNavigator.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (pagerState.currentPage) {
                                0 -> "Step 1 of 4"
                                1 -> "Step 2 of 4"
                                2 -> "Step 3 of 4"
                                3 -> "Step 4 of 4"
                                else -> "Step 1 of 4"
                            },
                            color = Color.White
                        )

                    },
                    navigationIcon = {
                        if (pagerState.currentPage > 0) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = when (pagerState.currentPage) {
                        0 -> "Basic Info"
                        1 -> "Physical Measurements"
                        2 -> "Fitness Goals & Activity Level"
                        3 -> "Dietary & Workout Preferences"
                        else -> "User Info"
                    },
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                        Button(onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }) {
                            Text("Next")
                        }
                    } else {
                        Button(onClick = {
                            viewModel.submitUserData()
                        }, enabled = !viewModel.uiState.value.isLoading) {
                            Text(if (viewModel.uiState.value.isLoading) "Submitting..." else "Submit")
                        }
                    }
                }

                if (viewModel.uiState.value.errorMessage.isNotEmpty()) {
                    Text(
                        text = viewModel.uiState.value.errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        LaunchedEffect(viewModel.uiState) {
            if (viewModel.uiState.value.submitSuccess) {
                navigator?.replace(TabsScreen())
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun BasicInfoStep(viewModel: UserInfoFormViewModel) {
        var genderExpanded by remember { mutableStateOf(false) }
        var ageInput by remember { mutableStateOf(viewModel.uiState.value.age?.toString() ?: "") }
        var ageError by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition {
                LottieCompositionSpec.JsonString(
                    Res.readBytes("drawable/anim.json").decodeToString()
                )
            }
            val progress by animateLottieCompositionAsState(composition)

            Image(
                painter = rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
                contentDescription = "Lottie animation"
            )

            CustomTextField(
                value = viewModel.uiState.value.fullName,
                onValueChange = { viewModel.onFullNameChange(it) },
                label = "Full Name",
                keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value = ageInput,
                onValueChange = {
                    ageInput = it
                    ageError = it.toIntOrNull() == null || it.toInt() > 100
                    if (!ageError) viewModel.onAgeChange(it)
                },
                label = "Age",
                keyboardType = KeyboardType.Number,
                isError = ageError,
                errorMessage = "Please enter a valid age"
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                val genders = listOf("Male", "Female", "Transgender")

                Text("Gender", modifier = Modifier.padding(start = 8.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(onClick = { genderExpanded = true }) {
                        Text(viewModel.uiState.value.gender.ifEmpty { "Select Gender" })
                    }
                    DropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        genders.forEach { gender ->
                            DropdownMenuItem(
                                text = { Text(gender) },
                                onClick = {
                                    viewModel.setGender(gender)
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PhysicalMeasurementsStep(viewModel: UserInfoFormViewModel) {
        var heightInput by remember {
            mutableStateOf(
                viewModel.uiState.value.height?.toString() ?: ""
            )
        }
        var weightInput by remember {
            mutableStateOf(
                viewModel.uiState.value.weight?.toString() ?: ""
            )
        }
        var heightError by remember { mutableStateOf(false) }
        var weightError by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            CustomTextField(
                value = heightInput,
                onValueChange = {
                    heightInput = it
                    heightError = it.toFloatOrNull() == null || it.toFloat() > 200.0
                    if (!heightError) viewModel.onHeightChange(it)
                },
                label = "Height (cm)",
                keyboardType = KeyboardType.Decimal,
                isError = heightError,
                errorMessage = "Please enter a valid height"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = weightInput,
                onValueChange = {
                    weightInput = it
                    weightError = it.toFloatOrNull() == null || it.toFloat() > 200.0
                    if (!weightError) viewModel.onWeightChange(it)
                },
                label = "Weight (kg)",
                keyboardType = KeyboardType.Decimal,
                isError = weightError,
                errorMessage = "Please enter a valid weight"
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FitnessGoalsActivityLevelStep(viewModel: UserInfoFormViewModel) {
        var selectedGoal by remember { mutableStateOf(viewModel.uiState.value.fitnessGoals) }
        var selectedActivityLevel by remember { mutableStateOf(viewModel.uiState.value.activityLevel) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            PreferenceSection(
                title = "Fitness Goal",
                options = listOf("Lose Weight", "Gain Muscle", "Maintain Weight"),
                selectedOption = selectedGoal,
                onOptionSelected = { goal ->
                    selectedGoal = goal
                    viewModel.setFitnessGoals(goal)
                }
            )

            PreferenceSection(
                title = "Activity Level",
                options = listOf("Sedentary", "Lightly Active", "Active", "Very Active"),
                selectedOption = selectedActivityLevel,
                onOptionSelected = { activityLevel ->
                    selectedActivityLevel = activityLevel
                    viewModel.setActivityLevel(activityLevel)
                }
            )
        }
    }


    @Composable
    fun DietaryWorkoutPreferencesStep(viewModel: UserInfoFormViewModel) {
        var dietaryPreferences by remember { mutableStateOf(viewModel.uiState.value.dietaryPreferences) }
        var workoutFrequency by remember { mutableStateOf(viewModel.uiState.value.workoutPreferences) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            PreferenceSection(
                title = "Dietary Preferences",
                options = listOf("Vegetarian", "Non-Vegetarian", "Vegan", "Keto"),
                selectedOption = dietaryPreferences,
                onOptionSelected = { preference ->
                    dietaryPreferences = preference
                    viewModel.setDietaryPreferences(preference)
                }
            )

            PreferenceSection(
                title = "Workout Frequency",
                options = listOf(
                    "1-2 times a week",
                    "3-4 times a week",
                    "5-6 times a week",
                    "Every day"
                ),
                selectedOption = workoutFrequency,
                onOptionSelected = { frequency ->
                    workoutFrequency = frequency
                    viewModel.setWorkoutPreferences(frequency)
                }
            )
        }
    }

    @Composable
    fun PreferenceSection(
        title: String,
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(option) }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
