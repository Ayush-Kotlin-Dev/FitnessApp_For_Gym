package di

import data.KtorApi
import data.auth.data.AuthService
import org.koin.dsl.module

val apiModule = module {
    single<KtorApi>{ AuthService() }
}