package di

import domain.usecases.auth.SignInUseCase
import domain.usecases.auth.SignUpUseCase
import domain.usecases.userinfo.GetUserInfoUseCase
import domain.usecases.userinfo.SubmitUserInfoUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { SignInUseCase() }
    factory { SignUpUseCase() }
    factory { SubmitUserInfoUseCase() }
    factory { GetUserInfoUseCase() }

}