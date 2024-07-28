package util

import com.soywiz.klock.DateTime
import com.soywiz.klock.DayOfWeek

fun getCurrentDay(): String {
    val today = DateTime.now().dayOfWeek
    return when (today) {
        DayOfWeek.Monday -> "Day 1"
        DayOfWeek.Tuesday -> "Day 2"
        DayOfWeek.Wednesday -> "Day 3"
        DayOfWeek.Thursday -> "Day 4"
        DayOfWeek.Friday -> "Day 5"
        DayOfWeek.Saturday -> "Day 6"
        DayOfWeek.Sunday -> "Day 2"
    }
}