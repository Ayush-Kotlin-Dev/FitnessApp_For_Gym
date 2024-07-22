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
            ExerciseSection()
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
        val chestExercises = listOf(
            "Barbell Bench Press",
            "Incline Dumbbell Bench Press",
            "Decline Dumbbell Bench Press",
            "Push-Ups",
            "Diamond Push-Ups",
            "Dips (Chest Version)",
            "Cable Flyes",
            "Dumbbell Flyes",
            "Pec Deck Machine",
            "Smith Machine Bench Press",
            "Single-Arm Dumbbell Bench Press",
            "Chest Dips",
            "Hammer Strength Chest Press",
            "Dumbbell Pullover",
            "Cable Crossover",
            "Incline Cable Flyes",
            "Decline Barbell Bench Press"
        )
        val tricepsExercises = listOf(
            "Close-Grip Bench Press",
            "Tricep Pushdowns",
            "Overhead Tricep Extension",
            "Dips",
            "Skull Crushers",
            "Rope Pushdowns",
            "Bench Dips",
            "Single-Arm Tricep Extensions",
            "Tricep Push-Ups",
            "Reverse Grip Tricep Pushdowns",
            "Lying Tricep Extensions",
            "Overhead Cable Tricep Extensions",
            "Close-Grip Push-Ups",
            "Tricep Dumbbell Pullover"
        )
        val backExercises = listOf(
            "Deadlifts",
            "Bent-Over Rows",
            "Lat Pulldowns",
            "T-Bar Rows",
            "Single-Arm Dumbbell Rows",
            "Seated Cable Rows",
            "Face Pulls",
            "Chin-Ups",
            "Hyperextensions",
            "Inverted Rows",
            "Dumbbell Pullovers",
            "Cable Pullovers",
            "Low Rows",
            "Barbell Shrugs",
            "Dumbbell Shrugs",
            "Reverse Flyes",
        )
        val bicepsExercises = listOf(
            "Barbell Curls",
            "Dumbbell Curls",
            "Hammer Curls",
            "Preacher Curls",
            "Incline Dumbbell Curls",
            "Concentration Curls",
            "EZ-Bar Curls",
            "Cable Curls",
            "Cross-Body Hammer Curls",
            "Rope Hammer Curls",
            "Alternating Dumbbell Curls",
            "Seated Incline Curls",
            "Standing Cable Curls",
            "Bicep Curl to Press"
        )
        val legExercises = listOf(
            "Back Squats",
            "Barbell Squats",
            "Leg Press",
            "Lunges",
            "Walking Lunges",
            "Step-Ups",
            "Leg Extensions",
            "Leg Curls",
            "Calf Raises",
            "Seated Calf Raises",
            "Bulgarian Split Squats",
            "Goblet Squats",
            "Hack Squats",
            "Box Jumps",
            "Pistol Squats",
            "Sumo Squats",
            "Glute Bridges",
            "Hip Thrusts",
            "Leg Press Calf Raises",
            "Wall Sits",
            "Sled Pushes",
            "Jump Squats",
            "Farmer's Walks",
            "Barbell Hip Thrusts",
            "Reverse Lunges",
            "Side Lunges",
            "Curtsy Lunges",
            "Smith Machine Squats",
            "Dumbbell Squats",
            "Kettlebell Swings"
        )
        val shoulderExercises = listOf(
            "Overhead Press",
            "Military Press",
            "Dumbbell Shoulder Press",
            "Arnold Press",
            "Lateral Raises",
            "Front Raises",
            "Reverse Flyes",
            "Face Pulls",
            "Upright Rows",
            "Shrugs",
            "Cable Lateral Raises",
            "Machine Shoulder Press",
            "Bent-Over Lateral Raises",
            "Plate Front Raises",
            "Seated Dumbbell Press",
            "Behind-the-Neck Press",
            "Dumbbell Shoulder Circles",
            "Bradford Press",
            "Single-Arm Dumbbell Press",
            "Kettlebell Upright Rows",
            "Dumbbell Y-Raises",
            "Battling Ropes",
            "Cable Face Pulls",
            "Resistance Band Lateral Raises",
        )
        val forearmExercises = listOf(
            "Wrist Curls",
            "Reverse Wrist Curls",
            "Barbell Hold",
            "Dumbbell Hold",
            "Grip Strengthener",
            "Plate Rotations",
            "Dead Hangs",
            "Forearm Roller",
            "Dumbbell Wrist Rotations",
            "Suitcase Carries",
            "Bottoms-Up Kettlebell Hold",
            "Cable Wrist Curls"
        )
        val abdominalExercises = listOf(
            "Crunches",
            "Sit-Ups",
            "Planks",
            "Russian Twists",
            "Leg Raises",
            "Dead Bug",
            "Mountain Climbers",
            "Hanging Leg Raises",
            "Ab Wheel Rollouts",
            "Cable Crunches",
            "Pallof Press",
            "Side Planks",
            "Woodchoppers",
            "Dragon Flags",
            "Hollow Body Hold",
            "Reverse Crunches",
            "Flutter Kicks",
            "Plank to Push-Up",
            "Medicine Ball Slams",
            "Windshield Wipers",
            "Toe Touches",
            "V-Ups",
            "L-Sit Hold",
            "Decline Bench Sit-Ups"
        )
    }
}

