package presentation.models

data class UserData(
    var fullName: String = "",
    var age: Int = 0,
    var gender: String = "",
    var height: Float = 0f,
    var weight: Float = 0f,
    var fitnessGoals: String = "",
    var activityLevel: String = "",
    var dietaryPreferences: String = "",
    var workoutPreferences: String = ""
)
