package di

import AppContainerViewModel
import org.koin.dsl.module
import presentation.screens.HomeScreen.HomeScreenViewModel
import presentation.screens.auth_onboard.UserInfoForm.UserInfoDataViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel

val viewModelModule = module {
    // Provide ViewModels as factories
    factory { LoginScreenViewModel(get() )}
    factory { SignupViewModel(get()) }
    factory { UserInfoDataViewModel() }
    factory { AppContainerViewModel(get()) }
    factory { HomeScreenViewModel() }
}