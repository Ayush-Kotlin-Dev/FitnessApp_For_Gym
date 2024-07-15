package di

import data.KtorApi
import data.auth.data.AuthRepositoryImpl
import data.auth.data.AuthService
import data.auth.domain.AuthRepository
import domain.usecases.auth.SignInUseCase
import domain.usecases.auth.SignUpUseCase
import getPlatform
import org.koin.dsl.module
import presentation.screens.auth_onboard.UserInfoForm.UserInfoDataViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel

val appModule = module {
    // Provide AuthService as a singleton
    single<KtorApi> { AuthService() }
    single<AuthService> { AuthService() }

    // Provide AuthRepositoryImpl as a singleton
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // Provide use cases as factories
    factory { SignInUseCase() }
    factory { SignUpUseCase() }

    // Provide ViewModels as factories
    factory { LoginScreenViewModel(get()) }
    factory { SignupViewModel(get()) }
    factory { UserInfoDataViewModel() }
}
