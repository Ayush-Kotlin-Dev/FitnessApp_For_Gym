package data.auth.data


import data.KtorApi
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import data.models.AuthResponse
import data.models.SignInRequest
import data.models.SignUpRequest

class AuthService  : KtorApi(){

    suspend fun signUp(
        request: SignUpRequest
    ): AuthResponse = client.post {
        endPoint(path = "signup")
        setBody(request)
    }.body()

    suspend fun signIn(
        request: SignInRequest
    ): AuthResponse = client.post {
        endPoint(path = "login")
        setBody(request)
    }.body()



}