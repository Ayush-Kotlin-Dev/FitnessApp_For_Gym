package presentation.screens.HomeScreen

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import presentation.screens.Plans.PlansTab
import presentation.screens.Plans.SharedWorkoutViewModel
import presentation.screens.profile.ProfileTab
import presentation.screens.stats.Stats

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<HomeScreenViewModel>()

        TabNavigator(HomeTab) {
            Scaffold(
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    BottomNavigationBar()
                }
            )
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
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

    @Composable
    private fun BottomNavigationBar() {
        NavigationBar(
            containerColor = Color.Black,
            tonalElevation = 8.dp,
        ) {
            TabNavigationItem(HomeTab)
            TabNavigationItem(Stats)
            TabNavigationItem(PlansTab)
            TabNavigationItem(ProfileTab)
        }
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
        val selectedExercises by sharedViewModel.selectedExercises.collectAsState()
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
            ExerciseSection(exercises = selectedExercises)
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
        exercises.forEachIndexed { index, exercise ->
            ExerciseItem(
                number = index + 1,
                title = exercise,
                description = "Custom exercise from your plan"
            )
        }
        if (exercises.isEmpty()) {
            Text("No exercises selected. Go to Plans to choose your exercises.", color = Color.Gray)
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

