package di

import AppContainerViewModel
import org.koin.dsl.module
import presentation.screens.homescreen.HomeScreenViewModel
import presentation.screens.tabs.SharedWorkoutViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel
import presentation.screens.auth_onboard.userInfoForm.UserInfoFormViewModel
import presentation.screens.profile.ProfileScreenViewModel
import presentation.screens.stats.StatsScreenViewModel

val viewModelModule = module {
    // Provide ViewModels as factories
    factory { AppContainerViewModel(get()) }
    factory { LoginScreenViewModel(get() ,get() )}
    factory { SignupViewModel(get()) }
    factory { UserInfoFormViewModel(get(),get()) }
    factory { HomeScreenViewModel(get() ,get()) }
    factory { SharedWorkoutViewModel(get(),get()) }
    factory { ProfileScreenViewModel(get() , get()) }
    factory { StatsScreenViewModel() }
}