package data.userinfo.domain

import data.models.UserInfoData
import data.models.UserInfoResponseData
import util.Result
interface UserInfoRepository   {

    suspend fun addUserInfo(userInfo: UserInfoData): Result<UserInfoResponseData>

    suspend fun getUserInfo(userId: String): Result<UserInfoResponseData>

    suspend fun updateUserInfo(userInfo: UserInfoData) : Result<UserInfoResponseData>
}