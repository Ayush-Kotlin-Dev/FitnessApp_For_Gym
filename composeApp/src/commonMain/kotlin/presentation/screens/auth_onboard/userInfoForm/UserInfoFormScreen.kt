import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import avikfitness.composeapp.generated.resources.Res
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import io.github.alexzhirkevich.compottie.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.components.CustomTextField
import presentation.screens.HomeScreen.HomeScreen
import presentation.screens.auth_onboard.userInfoForm.UserInfoFormViewModel

@OptIn(ExperimentalFoundationApi::class)
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
                    navigationIcon = if (pagerState.currentPage > 0) {
                        {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back" , tint = Color.Red)
                            }
                        }
                    } else null,
                    backgroundColor = Color.Transparent,
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
                    style = MaterialTheme.typography.h5
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
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
                        }) {
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
                navigator?.replace(HomeScreen())
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
                            DropdownMenuItem(onClick = {
                                viewModel.setGender(gender)
                                genderExpanded = false
                            }) {
                                Text(gender)
                            }
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

    @Composable
    fun FitnessGoalsActivityLevelStep(viewModel: UserInfoFormViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val fitnessGoals = listOf("Weight Loss", "Weight Gain", "Muscle Gain", "General Fitness")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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

            activityLevels.forEach { level ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.setActivityLevel(level) }
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = viewModel.uiState.value.activityLevel == level,
                        onClick = { viewModel.setActivityLevel(level) },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Green)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(level)
                }
            }
        }
    }

    @Composable
    fun DietaryWorkoutPreferencesStep(viewModel: UserInfoFormViewModel) {
        var dietaryPreferencesExpanded by remember { mutableStateOf(false) }
        var workoutPreferencesExpanded by remember { mutableStateOf(false) }
        val dietaryOptions = listOf("Vegan", "Vegetarian", "Non-Vegetarian")
        val workoutOptions = listOf("Strength Training", "Cardio", "Mixed")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text("Dietary Preferences")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { dietaryPreferencesExpanded = true }) {
                    Text(viewModel.uiState.value.dietaryPreferences.ifEmpty { "Select Dietary Preference" })
                }
                DropdownMenu(
                    expanded = dietaryPreferencesExpanded,
                    onDismissRequest = { dietaryPreferencesExpanded = false }
                ) {
                    dietaryOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            viewModel.setDietaryPreferences(option)
                            dietaryPreferencesExpanded = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Workout Preferences")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { workoutPreferencesExpanded = true }) {
                    Text(viewModel.uiState.value.workoutPreferences.ifEmpty { "Select Workout Preference" })
                }
                DropdownMenu(
                    expanded = workoutPreferencesExpanded,
                    onDismissRequest = { workoutPreferencesExpanded = false }
                ) {
                    workoutOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            viewModel.setWorkoutPreferences(option)
                            workoutPreferencesExpanded = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }
        }
    }
}
