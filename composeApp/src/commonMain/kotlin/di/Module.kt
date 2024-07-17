package di

import AppContainerViewModel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import data.KtorApi
import data.auth.data.AuthRepositoryImpl
import data.auth.data.AuthService
import data.auth.domain.AuthRepository
import data.local.createDataStore
import domain.usecases.auth.SignInUseCase
import domain.usecases.auth.SignUpUseCase
import org.koin.dsl.module
import presentation.screens.auth_onboard.UserInfoForm.UserInfoDataViewModel
import presentation.screens.auth_onboard.login.LoginScreenViewModel
import presentation.screens.auth_onboard.signup.SignupViewModel

val appModule = module {

}