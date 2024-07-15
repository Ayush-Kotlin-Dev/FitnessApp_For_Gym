package data.auth.data


import data.KtorApi
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import presentation.models.AuthResponse
import presentation.models.SignInRequest
import presentation.models.SignUpRequest

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