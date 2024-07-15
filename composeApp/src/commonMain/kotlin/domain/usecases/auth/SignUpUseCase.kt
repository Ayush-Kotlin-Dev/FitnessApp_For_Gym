package domain.usecases.auth

import data.auth.domain.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.models.AuthResultData
import util.Result

class SignUpUseCase : KoinComponent {

    private val repository: AuthRepository by inject()

    suspend operator fun invoke(
        userName: String,
        email: String,
        password: String
    ): Result<AuthResultData> {
        //all checks for username and password
        if(userName.isBlank() ) {
            return Result.Error("Invalid username")
        }
        if(email.isBlank() ) {
            return Result.Error("Invalid email")
        }
        if(password.isBlank() || password.length < 6) {
            return Result.Error("Password should be atleast 6 characters long")
        }

        return repository.signUp(userName, email, password)
    }
}

