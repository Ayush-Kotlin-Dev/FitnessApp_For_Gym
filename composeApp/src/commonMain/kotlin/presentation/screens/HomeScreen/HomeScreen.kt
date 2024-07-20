package presentation.screens.HomeScreen


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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
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
import avikfitness.composeapp.generated.resources.hide_eye_icon_filled
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.screens.profile.ProfileTab

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<HomeScreenViewModel>()

        TabNavigator(HomeTab) {
            Scaffold(
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    BottomNavigation(
                        backgroundColor = Color.Black,
                        elevation = 8.dp
                    ) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(SearchTab)
                        TabNavigationItem(CartTab)
                        TabNavigationItem(ProfileTab)
                    }
                }
            )
        }

    }
    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        BottomNavigationItem(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } },
            selectedContentColor = Color.Red.copy(alpha = 0.7f),
            unselectedContentColor = Color.White.copy(alpha = 0.7f),
            label = { Text(text = tab.options.title) },
            alwaysShowLabel = false

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(16.dp))
            WorkoutSection()
            Spacer(modifier = Modifier.height(16.dp))
            ExerciseSection()
        }
    }
}

object SearchTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Search"
            val icon = rememberVectorPainter(Icons.Default.Search)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Text("Search")
    }
}

object CartTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Cart"
            val icon = rememberVectorPainter(Icons.Default.ShoppingCart)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Text("Cart")
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
    Button(
        onClick = { /* TODO */ },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
//            modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
        Text(text = "SAT", color = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Time to workout", color = Color.Black)
    }
}

@Composable
fun WorkoutSection() {
    Column {
        Text(
            text = "GUIDED TRAINING", color = Color.Gray, fontSize = 14.sp
        )
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
fun ExerciseSection() {
    Column {
        Text(text = "NEXT EXERCISE", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        ExerciseItem(
            number = 1,
            title = "Incline Dumbbell Flyes",
            description = "It works your upper and outer pecs to build a broader chest."
        )
        ExerciseItem(
            number = 2,
            title = "Cable Rope Overhead Extensions",
            description = "This move works your triceps through a full range of motion, and the cable forces your muscles to work harder."
        )
        ExerciseItem(
            number = 3,
            title = "Barbell Curl",
            description = "This move works your biceps through a full range of motion, and the cable forces your muscles to work harder."
        )
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