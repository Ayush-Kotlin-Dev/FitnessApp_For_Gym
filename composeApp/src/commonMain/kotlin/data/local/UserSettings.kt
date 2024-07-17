package data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.serialization.Serializable
import presentation.models.AuthResultData

@Serializable
data class UserSettings(
    val userId : Long = -1,
    val userName : String = "",
    val email : String = "",
    val bio : String = "",
    val token : String = "",
    val isFormFilled : Boolean = false,
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


object PreferencesKeys {
    val USER_ID = longPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
    val EMAIL = stringPreferencesKey("email")
    val BIO = stringPreferencesKey("bio")
    val TOKEN = stringPreferencesKey("token")
    val IS_FORM_FILLED = booleanPreferencesKey("is_form_filled")
}
