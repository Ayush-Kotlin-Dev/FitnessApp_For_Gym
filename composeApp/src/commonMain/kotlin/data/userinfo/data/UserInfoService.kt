package data.userinfo.data

import data.KtorApi
import data.models.UserInfoData
import data.models.UserInfoResponseData
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class UserInfoService : KtorApi() {

    suspend fun addUserInfo(
        userInfo: UserInfoData
    ): UserInfoResponseData = client.post {
        endPoint(path = "addUserInfo")
        setBody(userInfo)
    }.body()


    suspend fun getUserInfo(
        userId: Long
    ): UserInfoResponseData = client.get {
        endPoint(path = "getUserInfo")
        parameter("userId", userId)
    }.body()

    suspend fun updateUserInfo(
        userInfo: UserInfoData
    ): UserInfoResponseData =
        client.post {
            endPoint(path = "updateUserInfo")
            setBody(userInfo)
        }.body()
}
