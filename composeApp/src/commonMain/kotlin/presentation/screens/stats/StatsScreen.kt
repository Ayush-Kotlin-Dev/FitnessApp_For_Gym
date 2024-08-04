
package presentation.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.datetime.DayOfWeek
import presentation.screens.plans.AccentColor
import presentation.screens.plans.BackgroundColor
import presentation.screens.plans.CardBackgroundColor
import presentation.screens.plans.PrimaryTextColor
import presentation.screens.plans.SecondaryTextColor

class StatsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<StatsScreenViewModel>()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(16.dp)
        ) {
            item { OverallStats(viewModel) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { WeeklyWorkoutSplit(viewModel) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { AttendanceStreak(viewModel) }
        }
    }
}

@Composable
fun OverallStats(viewModel: StatsScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Overall Stats",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                viewModel.getOverallStats().forEach { stat ->
                    StatItem(stat.first, stat.second)
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = PrimaryTextColor, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = SecondaryTextColor)
    }
}

@Composable
fun WeeklyWorkoutSplit(viewModel: StatsScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Weekly Workout Split",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            viewModel.getWeeklyWorkoutSplit().forEach { (day, exercises) ->
                DayWorkout(day, exercises)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun DayWorkout(day: DayOfWeek, exercises: List<ExerciseStats>) {
    Column {
        Text(text = day.name, style = MaterialTheme.typography.titleMedium, color = PrimaryTextColor, fontWeight = FontWeight.Bold)
        exercises.forEach { exercise ->
            Text(
                text = "${exercise.name}: ${exercise.weight}kg - ${exercise.sets}x${exercise.reps}",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
        }
    }
}
@Composable
fun AttendanceStreak(viewModel: StatsScreenViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Attendance Streak",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            val streak = viewModel.getAttendanceStreak()
            val daysOfWeek = listOf("Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

            Box(modifier = Modifier.height(200.dp).horizontalScroll(rememberScrollState()),
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(daysOfWeek) { dayIndex, day ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            if (dayIndex in listOf(1, 3, 5)) {
                                Text(day, style = MaterialTheme.typography.bodySmall, color = SecondaryTextColor, modifier = Modifier.width(32.dp))
                            } else {
                                Spacer(modifier = Modifier.width(32.dp))
                            }

                            streak.chunked(7).forEach { week ->
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(
                                            if (week.getOrNull(dayIndex)?.attended == true) AccentColor else Color.Gray,
                                            shape = MaterialTheme.shapes.small
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

