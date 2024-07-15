package di

import data.KtorApi
import org.koin.dsl.module

val apiModule = module {
    single<KtorApi>{AuthService() }
}