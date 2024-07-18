package di

import data.KtorApi
import data.auth.data.AuthService
import data.userinfo.data.UserInfoService
import org.koin.dsl.module

val apiModule = module {
    single<KtorApi>{ AuthService() }
    factory { AuthService() }
    factory { UserInfoService() }
}