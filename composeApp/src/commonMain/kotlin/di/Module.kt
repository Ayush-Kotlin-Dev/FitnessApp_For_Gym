package di

import getPlatform
import org.koin.dsl.module
import presentation.screens.auth_onboard.UserInfoForm.UserInfoDataViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel

val appModule = module {
    single { getPlatform() }
    factory { LoginScreenViewModel()}
    factory { SignupViewModel() }
    factory { UserInfoDataViewModel() }
}