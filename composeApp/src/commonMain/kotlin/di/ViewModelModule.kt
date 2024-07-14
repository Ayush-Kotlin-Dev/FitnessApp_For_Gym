package di

import org.koin.dsl.module
import presentation.screens.auth_onboard.UserInfoForm.UserInfoDataViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel

val viewModelModule = module {
    factory { LoginScreenViewModel() }
    factory { SignupViewModel() }
    factory { UserInfoDataViewModel() }
}