package presentation.screens.homescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.chest_home
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import presentation.screens.plans.PlanDetailScreen
import presentation.screens.plans.PlansTab
import presentation.screens.plans.SharedWorkoutViewModel
import presentation.screens.profile.ProfileTab
import presentation.screens.stats.StatsTab

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val sharedWorkoutViewModel = koinScreenModel<SharedWorkoutViewModel>()
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()

        val currentScreen by homeScreenViewModel.currentScreen.collectAsState()
        val showBars = homeScreenViewModel.shouldShowBars(currentScreen)



        TabNavigator(HomeTab) { tabNavigator ->
            Scaffold(
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        when (val screen = currentScreen) {
                            is Tab -> tabNavigator.current.Content()
                            is PlanDetailScreen -> screen.Content()
                            else -> HomeTab.Content() // Default to HomeTab if unknown screen
                        }
                    }
                },
                bottomBar = {
                    if (showBars) {
                        BottomNavigationBar(
                            tabNavigator = tabNavigator,
                            updateCurrentScreen = homeScreenViewModel::updateCurrentScreen
                        )
                    }
                },
                topBar = {
                    if (showBars) {
                        TopAppBar(
                            title = { Text(currentScreen.toString()) },
                            // Add other TopAppBar properties as needed
                        )
                    }
                }
            )
        }
    }


    @Composable
    private fun BottomNavigationBar(
        tabNavigator: TabNavigator,
        updateCurrentScreen: (Screen) -> Unit
    ) {
        NavigationBar(
            containerColor = Color.Black,
            tonalElevation = 8.dp,
        ) {
            TabNavigationItem(HomeTab, tabNavigator, updateCurrentScreen)
            TabNavigationItem(StatsTab, tabNavigator, updateCurrentScreen)
            TabNavigationItem(PlansTab, tabNavigator, updateCurrentScreen)
            TabNavigationItem(ProfileTab, tabNavigator, updateCurrentScreen)
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(
        tab: Tab,
        tabNavigator: TabNavigator,
        updateCurrentScreen: (Screen) -> Unit
    ) {
        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = {
                tabNavigator.current = tab
                updateCurrentScreen(tab)
            },
            icon = {
                tab.options.icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = tab.options.title
                    )
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red.copy(alpha = 0.7f),
                unselectedIconColor = Color.White.copy(alpha = 0.7f)
            ),
            label = { Text(text = tab.options.title) },
            alwaysShowLabel = false,
        )
    }
}

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val sharedViewModel = koinScreenModel<SharedWorkoutViewModel>()
        val allSelectedExercises by sharedViewModel.selectedExercises.collectAsState()

        // For demonstration, let's assume the current day is "Day 1"
        val currentDay = "Day 1"
        val currentDayExercises = allSelectedExercises[currentDay] ?: emptyList()

        println("HomeTab - All exercises: $allSelectedExercises")
        println("HomeTab - Current day exercises: $currentDayExercises")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(16.dp))
            WorkoutSection()
            Spacer(modifier = Modifier.height(16.dp))
            ExerciseSection(exercises = currentDayExercises)
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Image",
            modifier = Modifier.size(50.dp).clip(CircleShape),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "Welcome Ayush", color = Color.White, fontSize = 18.sp)
            Text(
                text = "Way to go! You're on a hot 3-week workout streak",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedButton(
        onClick = { /* TODO */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,

            ),
        border = BorderStroke(2.dp, Color.Gray)
    ) {
        Text(
            text = " SAT ",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Yellow)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Time to workout", color = Color.White)
    }
}

@Composable
fun WorkoutSection() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "GUIDED TRAINING ", color = Color.Gray, fontSize = 14.sp
            )
            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 8.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(Res.drawable.chest_home),
                contentDescription = "Workout Image",
                modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp))
            )
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "CHEST & ARMS",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Recommended",
                    color = Color.Yellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Dynamic Warmup | 22 Minutes",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ExerciseSection(exercises: List<String>) {
    Column {
        Text(text = "NEXT EXERCISES", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        if (exercises.isNotEmpty()) {
            exercises.forEachIndexed { index, exercise ->
                ExerciseItem(
                    number = index + 1,
                    title = exercise,
                    description = "Custom exercise from your plan"
                )
            }
        } else {
            Text("No exercises selected for today. Go to Plans to choose your exercises.", color = Color.Gray)
        }
    }
}

@Composable
fun ExerciseItem(number: Int, title: String, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Text(
            text = number.toString(),
            color = Color.Yellow,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = description, color = Color.Gray, fontSize = 14.sp)
        }

    }
}

