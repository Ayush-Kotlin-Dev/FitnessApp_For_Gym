package di

import domain.RealmManager
import org.koin.dsl.module

val appModule = module {
    single { RealmManager() }
}