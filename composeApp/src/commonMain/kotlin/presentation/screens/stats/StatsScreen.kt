package presentation.screens.stats

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import presentation.screens.plans.AccentColor
import presentation.screens.plans.BackgroundColor
import presentation.screens.plans.CardBackgroundColor
import presentation.screens.plans.PrimaryTextColor
import presentation.screens.plans.SecondaryTextColor
import presentation.screens.profile.formatBmi

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
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { ProgressGraph(viewModel) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { PersonalRecords(viewModel) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { WorkoutConsistency(viewModel) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { BodyMeasurements(viewModel) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { NutritionSummary(viewModel) }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = PrimaryTextColor,
            fontWeight = FontWeight.Bold
        )
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
        Text(
            text = day.name,
            style = MaterialTheme.typography.titleMedium,
            color = PrimaryTextColor,
            fontWeight = FontWeight.Bold
        )
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
    val currentMonth =
        remember { mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()).month) }
    val currentYear =
        remember { mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()).year) }

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (currentMonth.value == kotlinx.datetime.Month.JANUARY) {
                        currentMonth.value = kotlinx.datetime.Month.DECEMBER
                        currentYear.value -= 1
                    } else {
                        currentMonth.value =
                            kotlinx.datetime.Month.values()[currentMonth.value.ordinal - 1]
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Month"
                    )
                }
                Text(
                    text = "${
                        currentMonth.value.name.lowercase().replaceFirstChar { it.uppercase() }
                    } ${currentYear.value}",
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryTextColor
                )
                IconButton(onClick = {
                    if (currentMonth.value == kotlinx.datetime.Month.DECEMBER) {
                        currentMonth.value = kotlinx.datetime.Month.JANUARY
                        currentYear.value += 1
                    } else {
                        currentMonth.value =
                            kotlinx.datetime.Month.values()[currentMonth.value.ordinal + 1]
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Month"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val streak = viewModel.getAttendanceStreak().filter {
                it.date.month == currentMonth.value && it.date.year == currentYear.value
            }
            val daysOfWeek = listOf("Mon", "Wed", "Fri")

            Box(modifier = Modifier.height(200.dp)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(streak.chunked(5)) { weekIndex, week ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            if (weekIndex == 1 || weekIndex == 3 || weekIndex == 5) {
                                val dayIndex = weekIndex / 2
                                if (dayIndex < daysOfWeek.size) {
                                    Text(
                                        daysOfWeek[dayIndex],
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SecondaryTextColor,
                                        modifier = Modifier.width(32.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(32.dp))
                            }

                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(
                                            if (day.attended) AccentColor else Color.Gray,
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

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun ProgressGraph(viewModel: StatsScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Progress Distribution",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            val data = viewModel.getProgressData()
            val values = data.map { it.value }
            val total = values.sum()
            val formatString = formatBmi(total).split(".")

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PieChart(
                    values,
                    label = { i ->
                        Text("${data[i].label}: ${data[i].value}")
                    },
                    holeSize = 0.75F,
                    holeContent = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Total", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    formatString[0],
                                    style = MaterialTheme.typography.displaySmall
                                )
                            }
                        }
                    }
                )
                data.forEachIndexed { index, dataPoint ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color.hsl(360f * index / data.size, 0.5f, 0.5f))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${dataPoint.label}: ${dataPoint.value}")
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalRecords(viewModel: StatsScreenViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var editingRecord by remember { mutableStateOf<PersonalRecord?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Personal Records",
                    style = MaterialTheme.typography.titleLarge,
                    color = AccentColor,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    editingRecord = null
                    showDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Record")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn {
                    items(viewModel.fetchPersonalRecords()) { record ->
                        PersonalRecordItem(
                            record = record,
                            onEditClick = {
                                editingRecord = record
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    val scope = rememberCoroutineScope()

    if (showDialog) {
        PersonalRecordDialog(
            record = editingRecord,
            onDismiss = { showDialog = false },
            onSave = { newRecord ->
                scope.launch {
                    viewModel.savePersonalRecord(newRecord)
                    showDialog = false
                }

            }
        )
    }
}

@Composable
fun PersonalRecordItem(record: PersonalRecord, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEditClick()
            }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column {
            Text(
                text = record.exercise,
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryTextColor
            )
            Text(
                text = "${record.weight}kg x ${record.reps} reps",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
        }

    }
}

@Composable
fun PersonalRecordDialog(
    record: PersonalRecord?,
    onDismiss: () -> Unit,
    onSave: (PersonalRecord) -> Unit
) {
    var exercise by remember { mutableStateOf(record?.exercise ?: "") }
    var weight by remember { mutableStateOf(record?.weight?.toString() ?: "") }
    var reps by remember { mutableStateOf(record?.reps?.toString() ?: "") }

    AlertDialog(
        containerColor = CardBackgroundColor,
        onDismissRequest = onDismiss,
        title = { Text(if (record == null) "Add Personal Record" else "Edit Personal Record") },
        text = {
            Column {
                OutlinedTextField(
                    value = exercise,
                    onValueChange = { exercise = it },
                    label = { Text("Exercise") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = { Text("Reps") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newRecord = PersonalRecord(
                    exercise = exercise,
                    weight = weight.toFloatOrNull() ?: 0f,
                    reps = reps.toIntOrNull() ?: 0
                )
                onSave(newRecord)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun WorkoutConsistency(viewModel: StatsScreenViewModel) {
    val consistency = viewModel.getWorkoutConsistency()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Workout Consistency",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${consistency.percentage}%",
                style = MaterialTheme.typography.displayMedium,
                color = PrimaryTextColor
            )
            Text(
                text = "${consistency.daysWorkedOut} out of ${consistency.totalDays} days",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
        }
    }
}

@Composable
fun BodyMeasurements(viewModel: StatsScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Body Measurements",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            viewModel.getLatestBodyMeasurements().forEach { (measurement, value) ->
                Text(
                    text = "$measurement: $value",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryTextColor
                )
            }
        }
    }
}

@Composable
fun NutritionSummary(viewModel: StatsScreenViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Nutrition Summary",
                style = MaterialTheme.typography.titleLarge,
                color = AccentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            val nutritionData = viewModel.getNutritionSummary()
            Text(
                text = "Average Daily Intake:",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
            Text(
                text = "Calories: ${nutritionData.calories}",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryTextColor
            )
            Text(
                text = "Protein: ${nutritionData.protein}g",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryTextColor
            )
            Text(
                text = "Carbs: ${nutritionData.carbs}g",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryTextColor
            )
            Text(
                text = "Fat: ${nutritionData.fat}g",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryTextColor
            )
        }
    }
}