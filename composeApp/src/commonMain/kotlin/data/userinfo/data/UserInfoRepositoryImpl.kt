package data.userinfo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import data.local.saveUserInfo
import data.models.UserInfoData
import data.models.UserInfoResponseData
import data.toUserInfoData
import data.userinfo.domain.UserInfoRepository
import util.Result

class UserInfoRepositoryImpl(
    private val userInfoService: UserInfoService,
    private val dataStore: DataStore<Preferences>  // Add DataStore as a dependency

) : UserInfoRepository {
    override suspend fun addUserInfo(userInfo: UserInfoData): Result<UserInfoResponseData> {
        return try {
            val response = userInfoService.addUserInfo(userInfo)
            if(response.data != null) {
                val userSettings = response.data.toUserInfoData()
                // Save user info
                saveUserInfo(dataStore, userSettings)
                Result.Success(response)
            } else {
                Result.Error(
                    message = response.errorMessage ?: "Add user info failed"
                )
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }
    }

    override suspend fun getUserInfo(userId: String): Result<UserInfoResponseData> {
        return try {
            val response = userInfoService.getUserInfo(userId)
            if(response.data != null) {
                val userSettings = response.data.toUserInfoData()
                // Save user info
                saveUserInfo(dataStore, userSettings)
                Result.Success(response)
            } else {
                Result.Error(
                    message = response.errorMessage ?: "Get user info failed"
                )
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }
    }

    override suspend fun updateUserInfo(userInfo: UserInfoData): Result<UserInfoResponseData> {
        return try {
            val response = userInfoService.updateUserInfo(userInfo)
            if(response.data != null) {
                val userSettings = response.data.toUserInfoData()
                // Save user info
                saveUserInfo(dataStore, userSettings)
                Result.Success(response)
            } else {
                Result.Error(
                    message = response.errorMessage ?: "Update user info failed"
                )
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }
    }
}