package di

import AppContainerViewModel
import org.koin.dsl.module
import presentation.screens.HomeScreen.HomeScreenViewModel
import presentation.screens.Plans.SharedWorkoutViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel
import presentation.screens.auth_onboard.userInfoForm.UserInfoFormViewModel

val viewModelModule = module {
    // Provide ViewModels as factories
    factory { AppContainerViewModel(get()) }
    factory { LoginScreenViewModel(get() ,get() )}
    factory { SignupViewModel(get()) }
    factory { UserInfoFormViewModel(get()) }
    factory { HomeScreenViewModel() }
    factory { SharedWorkoutViewModel() }
}