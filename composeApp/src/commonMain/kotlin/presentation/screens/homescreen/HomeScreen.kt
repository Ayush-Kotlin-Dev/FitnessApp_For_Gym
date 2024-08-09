package presentation.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.chest_home
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import presentation.components.DraggableLazyColumn
import presentation.screens.tabs.SharedWorkoutViewModel
import util.getCurrentDay


class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val sharedViewModel = koinScreenModel<SharedWorkoutViewModel>()
        val homeScreenViewModel = koinScreenModel<HomeScreenViewModel>()

        val homeScreenUiState by homeScreenViewModel.homeScreenUiStateFlow.collectAsState()
        val currentPlanName by sharedViewModel.getSelectedRoutineFlow()
            .collectAsState(initial = null)

        val currentDay = getCurrentDay()
        val Navigator = LocalNavigator.current

        LaunchedEffect(currentPlanName) {
            currentPlanName?.let {
                homeScreenViewModel.getWorkoutDayForDate(it, currentDay)
            }
        }

        LaunchedEffect(Unit) {
            homeScreenViewModel.getFullName()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            HeaderSection(fullName = homeScreenUiState.fullName)
            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = !homeScreenUiState.isLoading,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                when {
                    homeScreenUiState.currentWorkoutDay != null -> {
                        Column {
                            WorkoutSection(focus = homeScreenUiState.currentWorkoutDay!!.focus)
                            Spacer(modifier = Modifier.height(16.dp))
                            ExerciseSection(
                                exercises = homeScreenUiState.currentWorkoutDay!!.exerciseDbs.map { it.name },
                                onReorder = { from, to ->
                                    homeScreenViewModel.reorderExercises(from, to)
                                }
                            )
                        }
                    }

                    !homeScreenUiState.isLoading -> {
                        Text(
                            text = "No workout planned for today. Go to Plans to choose your exercises.",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            if (homeScreenUiState.isLoading) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}


@Composable
fun HeaderSection(
    currentDay: String = getCurrentDay(),
    fullName: String = ""
) {
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
            Text(
                text = "Welcome ${fullName.substringBefore(" ")}",
                color = Color.White,
                fontSize = 18.sp
            )
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
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(50),

        ) {
        Text(
            text = " $currentDay ",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color(0xFFD4FC79), RoundedCornerShape(50))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Time to workout", color = Color.White)
    }
}

@Composable
fun WorkoutSection(focus: String) {
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
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
            )
            Column {
                Text(
                    text = focus,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = " Recommended ",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(Color.Yellow, RoundedCornerShape(4.dp))
                )
                Text(
                    text = "Dynamic Warmup | 22 Minutes",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ExerciseSection(
    exercises: List<String>,
    onReorder: (Int, Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(
            text = "NEXT EXERCISES",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(modifier = Modifier.height(500.dp)) { //TODO: Fix height
            DraggableLazyColumn(
                items = exercises,
                lazyListState = lazyListState,
                onMove = onReorder,
                onDragEnd = { }
            ) { exercise, isDragging ->
                DraggableExerciseItem(
                    number = exercises.indexOf(exercise) + 1,
                    title = exercise,
                    description = "3 sets, 10 reps each set | 30 sec rest between sets | 1 min rest between exercises",
                    isLast = exercise == exercises.last(),
                    isDragging = isDragging
                )
            }
        }
    }
}

@Composable
fun DraggableExerciseItem(
    number: Int,
    title: String,
    description: String,
    isLast: Boolean,
    isDragging: Boolean
) {
    val elevation by animateDpAsState(if (isDragging) 8.dp else 2.dp)
    val scale by animateFloatAsState(if (isDragging) 1.05f else 1f)
    val Navigator = LocalNavigator.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .zIndex(if (isDragging) 1f else 0f),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = if (isDragging) Color(0xFF2A2A2A) else Color.Transparent)
    ) {
        ExerciseItem(
            number = number,
            title = title,
            description = description,
            isLast = isLast,
            onItemClick = { exerciseName ->
                Navigator?.push(ExerciseDetailScreen( exerciseName, onBackClick = { Navigator.pop() }))
            }
        )
    }
}

@Composable
fun ExerciseItem(
    number: Int,
    title: String,
    description: String,
    isLast: Boolean,
    onItemClick: (String) -> Unit
) { //TODO navigates to detailed screen of that exercise (having stats of that exercise (last week weight reps ))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(title)} //TODO navigate to detailed screen of that exercise (having stats of that exercise (last week weight reps ))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFFD4FC79), CircleShape)
            ) {
                Text(
                    text = number.toString(),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (!isLast) {
                VerticalDivider(
                    modifier = Modifier
                        .height(150.dp)
                        .padding(vertical = 10.dp),
                    thickness = 1.dp,
                    color = Color(0xFF3A3A3A)
                )
            }
        }
        Column(
            modifier = Modifier
                .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
                .weight(1f) // Allow Column to take available space
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = description,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis

            )
        }
    }
}