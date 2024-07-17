package di

import data.auth.data.AuthRepositoryImpl
import data.auth.domain.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

}