package di

import data.auth.data.AuthRepositoryImpl
import data.auth.domain.AuthRepository
import data.userinfo.data.UserInfoRepositoryImpl
import data.userinfo.domain.UserInfoRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single <UserInfoRepository>{ UserInfoRepositoryImpl(get(), get()) }

}