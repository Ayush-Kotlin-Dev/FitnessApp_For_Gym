package data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import data.models.AuthResultData
import data.models.UserInfoData
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val userId: Long = -1,
    val userName: String = "",
    val email: String = "",
    val bio: String = "",
    val token: String = "",
    val isFormFilled: Boolean = false,
)

@Serializable
data class UserForm(
    val fullName: String = "",
    val age: Int = 0,
    val gender: String = "",
    val height: Float = 0f,
    val weight: Float = 0f,
    val fitnessGoals: String = "",
    val activityLevel: String = "",
    val dietaryPreferences: String = "",
    val workoutPreferences: String = "",
)

fun UserSettings.toAuthResultData(): AuthResultData {
    return AuthResultData(
        userId,
        userName,
        email,
        bio,
        token,
        isFormFilled
    )
}

fun AuthResultData.toUserSettings(): UserSettings {
    return UserSettings(
        userId,
        userName,
        email,
        bio,
        token,
        isFormFilled
    )
}

fun UserInfoData.toUserForm(): UserForm {
    return UserForm(
        fullName,
        age,
        gender ,
        height,
        weight,
        fitnessGoals,
        activityLevel,
        dietaryPreferences,
        workoutPreferences
    )
}


object PreferencesKeys {
    val USER_ID = longPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
    val EMAIL = stringPreferencesKey("email")
    val BIO = stringPreferencesKey("bio")
    val TOKEN = stringPreferencesKey("token")
    val IS_FORM_FILLED = booleanPreferencesKey("is_form_filled")
    val FULL_NAME = stringPreferencesKey("full_name")
    val AGE = intPreferencesKey("age")
    val GENDER = stringPreferencesKey("gender")
    val HEIGHT = floatPreferencesKey("height")
    val WEIGHT = floatPreferencesKey("weight")
    val FITNESS_GOALS = stringPreferencesKey("fitness_goals")
    val ACTIVITY_LEVEL = stringPreferencesKey("activity_level")
    val DIETARY_PREFERENCES = stringPreferencesKey("dietary_preferences")
    val WORKOUT_PREFERENCES = stringPreferencesKey("workout_preferences")
}

