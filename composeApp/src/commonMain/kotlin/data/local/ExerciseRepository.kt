package data.local

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import avikfitness.composeapp.generated.resources.Res
import data.models.ExerciseList
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

class ExerciseRepository {
    @OptIn(ExperimentalResourceApi::class)
    suspend fun getExercisesFromJson(): ExerciseList {
        return try {
            val bytes = Res.readBytes("drawable/exercises.json")
            val jsonString = bytes.decodeToString()
            println("JSON content: $jsonString") // Add this line for debugging
            Json.decodeFromString(ExerciseList.serializer(), jsonString)
        } catch (e: Exception) {
            println("Error reading JSON: ${e.message}")
            throw e
        }
    }
}