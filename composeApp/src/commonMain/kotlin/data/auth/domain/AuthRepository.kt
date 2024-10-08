package data.auth.domain

import data.models.AuthResultData
import util.Result

interface AuthRepository {
    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData>
    suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthResultData>
}