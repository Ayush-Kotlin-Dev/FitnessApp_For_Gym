package presentation.screens.HomeScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.chest_home
import avikfitness.composeapp.generated.resources.hide_eye_icon_filled
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<HomeScreenViewModel>()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(16.dp))
            WorkoutSection()
            Spacer(modifier = Modifier.height(16.dp))
            ExerciseSection()
            Spacer(modifier = Modifier.weight(1f))
            BottomNavigationBar()
        }
    }

    @Composable
    fun HeaderSection() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Res.drawable.hide_eye_icon_filled),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(40.dp).clip(CircleShape)
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

    @Composable
    fun BottomNavigationBar() {
        BottomNavigation(
            backgroundColor = Color.Black,
            contentColor = Color.White
        ) {
            BottomNavigationItem(
                selected = false,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.Build, contentDescription = "Statistics") },
                label = { Text("Statistics") }
            )
            BottomNavigationItem(
                selected = false,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search") }
            )
            BottomNavigationItem(
                selected = true,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )
            BottomNavigationItem(
                selected = false,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                label = { Text("Cart") }
            )
            BottomNavigationItem(
                selected = false,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") }
            )
        }
    }
}
