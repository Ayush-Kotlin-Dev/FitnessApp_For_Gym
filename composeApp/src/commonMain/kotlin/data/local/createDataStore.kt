package data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import data.models.UserInfoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

fun createDataStore(producePath :() -> String ) : DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() },
    )
}

internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"


suspend fun saveUserSettings(dataStore: DataStore<Preferences>, userSettings: UserSettings) {
    dataStore.edit { preferences ->
        preferences[PreferencesKeys.USER_ID] = userSettings.userId
        preferences[PreferencesKeys.USER_NAME] = userSettings.userName
        preferences[PreferencesKeys.EMAIL] = userSettings.email
        preferences[PreferencesKeys.BIO] = userSettings.bio
        preferences[PreferencesKeys.TOKEN] = userSettings.token
        preferences[PreferencesKeys.IS_FORM_FILLED] = userSettings.isFormFilled
    }
}

fun getUserSettingsFlow(dataStore: DataStore<Preferences>): Flow<UserSettings> {
    return dataStore.data.map { preferences ->
        UserSettings(
            userId = preferences[PreferencesKeys.USER_ID] ?: -1,
            userName = preferences[PreferencesKeys.USER_NAME] ?: "",
            email = preferences[PreferencesKeys.EMAIL] ?: "",
            bio = preferences[PreferencesKeys.BIO] ?: "",
            token = preferences[PreferencesKeys.TOKEN] ?: "",
            isFormFilled = preferences[PreferencesKeys.IS_FORM_FILLED] ?: false
        )
    }
}
suspend fun saveUserInfo(dataStore: DataStore<Preferences>, userInfo: UserInfoData) {
    dataStore.edit { preferences ->
        preferences[PreferencesKeys.USER_ID] = userInfo.userId
        preferences[PreferencesKeys.FULL_NAME] = userInfo.fullName
        preferences[PreferencesKeys.AGE] = userInfo.age
        preferences[PreferencesKeys.GENDER] = userInfo.gender
        preferences[PreferencesKeys.HEIGHT] = userInfo.height
        preferences[PreferencesKeys.WEIGHT] = userInfo.weight
        preferences[PreferencesKeys.FITNESS_GOALS] = userInfo.fitnessGoals
        preferences[PreferencesKeys.ACTIVITY_LEVEL] = userInfo.activityLevel
        preferences[PreferencesKeys.DIETARY_PREFERENCES] = userInfo.dietaryPreferences
        preferences[PreferencesKeys.WORKOUT_PREFERENCES] = userInfo.workoutPreferences
        preferences[PreferencesKeys.IS_FORM_FILLED] = true
    }
}
fun getUserInfoFlow(dataStore: DataStore<Preferences>): Flow<UserInfoData> {
    return dataStore.data.map { preferences ->
        UserInfoData(
            userId = preferences[PreferencesKeys.USER_ID] ?: -1,
            fullName = preferences[PreferencesKeys.FULL_NAME] ?: "",
            age = preferences[PreferencesKeys.AGE] ?: 0,
            gender = preferences[PreferencesKeys.GENDER] ?: "",
            height = preferences[PreferencesKeys.HEIGHT] ?: 0f,
            weight = preferences[PreferencesKeys.WEIGHT] ?: 0f,
            fitnessGoals = preferences[PreferencesKeys.FITNESS_GOALS] ?: "",
            activityLevel = preferences[PreferencesKeys.ACTIVITY_LEVEL] ?: "",
            dietaryPreferences = preferences[PreferencesKeys.DIETARY_PREFERENCES] ?: "",
            workoutPreferences = preferences[PreferencesKeys.WORKOUT_PREFERENCES] ?: ""
        )
    }
}


