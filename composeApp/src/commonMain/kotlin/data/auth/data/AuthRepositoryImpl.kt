package data.auth.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import data.auth.domain.AuthRepository
import data.local.saveUserSettings
import data.local.toUserSettings
import data.toAuthResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import data.models.AuthResultData
import data.models.SignInRequest
import data.models.SignUpRequest
import util.Result

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val dataStore: DataStore<Preferences>  // Add DataStore as a dependency
) : AuthRepository {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData> = withContext(Dispatchers.IO) {
        try {
            val request = SignUpRequest(name, email, password)
            val authResponse = authService.signUp(request)
            if (authResponse.data == null) {
                Result.Error(
                    message =authResponse.errorMessage ?: "Sign up failed"
                )
            } else {
                val authResultData = authResponse.data.toAuthResultData()
                // Save user info
                saveUserSettings(dataStore, authResultData.toUserSettings())
                Result.Success(authResultData)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthResultData> = withContext(Dispatchers.IO) {
        try {
            val request = SignInRequest(email, password)
            val authResponse = authService.signIn(request)
            if (authResponse.data == null) {
                Result.Error(
                    message = authResponse.errorMessage ?: "Sign in failed"
                )
            } else {
                val authResultData = authResponse.data.toAuthResultData()
                // Save user info
                saveUserSettings(dataStore, authResultData.toUserSettings())
                Result.Success(authResultData)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }
    }
}
