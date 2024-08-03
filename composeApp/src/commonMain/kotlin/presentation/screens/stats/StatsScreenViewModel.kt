package presentation.screens.stats

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.datetime.*

class StatsScreenViewModel : ScreenModel {

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
        return (0 until 28).map { daysAgo ->
            val date = today.minus(DatePeriod(days = daysAgo))
            AttendanceDay(date, attended = daysAgo % 3 != 0)
        }.reversed()
    }
}

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