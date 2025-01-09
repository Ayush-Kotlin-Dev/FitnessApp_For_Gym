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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF2A2A2A), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Image",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Welcome ${fullName.substringBefore(" ")}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = "🔥 3-week streak",
                        color = Color(0xFFD4FC79),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A2A2A)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentDay,
                    color = Color(0xFFD4FC79),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Time to workout",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun WorkoutSection(focus: String) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "TODAY'S FOCUS",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.Gray.copy(alpha = 0.5f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(Res.drawable.chest_home),
                    contentDescription = "Workout Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = focus.uppercase(),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFD4FC79)
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "RECOMMENDED",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "22 Minutes",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseSection(
    exercises: List<String>,
    onReorder: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "EXERCISES",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.Gray.copy(alpha = 0.5f)
            )
            Text(
                text = "${exercises.size}",
                color = Color(0xFFD4FC79),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        ) {
            DraggableLazyColumn(
                items = exercises,
                lazyListState = rememberLazyListState(),
                onMove = onReorder,
                onDragEnd = { }
            ) { exercise, isDragging ->
                DraggableExerciseItem(
                    number = exercises.indexOf(exercise) + 1,
                    title = exercise,
                    description = "3 sets × 10 reps\n30s rest • 1m between exercises",
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