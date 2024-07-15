package di

import domain.usecases.auth.SignInUseCase
import org.koin.dsl.module
import presentation.screens.auth_onboard.UserInfoForm.UserInfoDataViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel

val useCaseModule = module {
    factory { SignInUseCase() }

}