package data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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
