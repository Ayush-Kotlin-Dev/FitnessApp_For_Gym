package presentation.screens.stats

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.RealmManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.*
import kotlin.random.Random

class StatsScreenViewModel(
    private val realmManager: RealmManager
) : ScreenModel {
    private val _personalRecords = mutableStateListOf<PersonalRecord>()
    val personalRecords: List<PersonalRecord> = _personalRecords

    init {
        // Initialize with some example data
        getPersonalRecords()

    }

    fun getOverallStats(): List<Pair<String, String>> {
        return listOf(
            "Workouts" to "10",
            "Time" to "10h 30m",
            "Calories" to "1000",
            "Distance" to "100km"
        )
    }

    fun getWeeklyWorkoutSplit(): Map<DayOfWeek, List<ExerciseStats>> {
        return mapOf(
            DayOfWeek.MONDAY to listOf(
                ExerciseStats("Bench Press", 80.0, 3, 8),
                ExerciseStats("Squat", 100.0, 3, 6),
                ExerciseStats("Leg Press", 120.0, 3, 10),
            ),
            DayOfWeek.TUESDAY to listOf(
                ExerciseStats("Dumbbell Curl", 20.0, 3, 12),
                ExerciseStats("Tricep Extension", 30.0, 3, 10)
            ),
            DayOfWeek.WEDNESDAY to listOf(
                ExerciseStats("Deadlift", 120.0, 3, 5),
                ExerciseStats("Pull-ups", 0.0, 3, 10)
            ),
            DayOfWeek.THURSDAY to listOf(
                ExerciseStats("Incline Bench Press", 70.0, 3, 8),
                ExerciseStats("Leg Curl", 80.0, 3, 10)
            ),
            DayOfWeek.FRIDAY to listOf(
                ExerciseStats("Overhead Press", 60.0, 3, 8),
                ExerciseStats("Barbell Row", 70.0, 3, 8)
            )
        )
    }

    fun getAttendanceStreak(): List<AttendanceDay> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val daysInMonth = daysInMonth(today.month, today.year)
        return (0 until daysInMonth).map { day ->
            val date = LocalDate(today.year, today.month, day + 1)
            AttendanceDay(date, attended = Random.nextBoolean())
        }
    }

    fun getProgressData(): List<ProgressDataPoint> {
        // This should fetch real data from your database or API
        // For now, let's use some example data
        return listOf(
            ProgressDataPoint("Chest", 20f),
            ProgressDataPoint("Back", 25f),
            ProgressDataPoint("Legs", 30f),
            ProgressDataPoint("Arms", 15f),
            ProgressDataPoint("Shoulders", 10f)
        )
    }


    data class ProgressDataPoint(val label: String, val value: Float)

    fun fetchPersonalRecords(): List<PersonalRecord> = _personalRecords

    suspend fun savePersonalRecord(record: PersonalRecord) {
        realmManager.savePersonalRecord(record)
    }
    private fun getPersonalRecords() {
        screenModelScope.launch {
            realmManager.getPersonalRecords().collect { records ->
                withContext(Dispatchers.Main) {
                    _personalRecords.clear()
                    _personalRecords.addAll(records)
                }
            }
        }
    }

    fun getWorkoutConsistency(): ConsistencyData {
        // Implement logic to calculate workout consistency
        return ConsistencyData(80, 20, 25)
    }

    fun getLatestBodyMeasurements(): Map<String, String> {
        // Implement logic to fetch latest body measurements
        return mapOf(
            "Weight" to "70kg",
            "Height" to "180cm",
            "Body Fat" to "15%"
        )
    }

    fun getNutritionSummary(): NutritionData {
        // Implement logic to calculate average nutrition data
        return NutritionData(2000, 100, 200, 50)
    }



}

data class ConsistencyData(
    val percentage: Int,
    val daysWorkedOut: Int,
    val totalDays: Int
)

data class NutritionData(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

data class PersonalRecord(val exercise: String, val weight: Float, val reps: Int)


data class ExerciseStats(
    val name: String,
    val weight: Double,
    val sets: Int,
    val reps: Int
)

data class AttendanceDay(
    val date: LocalDate,
    val attended: Boolean
)

fun daysInMonth(month: Month, year: Int): Int {
    return when (month) {
        Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY, Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        Month.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> TODO()
    }
}
