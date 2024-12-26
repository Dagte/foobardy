package org.damte.server.di

import org.damte.org.damte.server.StorageManager
import org.koin.dsl.module

val appModule = module {
    single { StorageManager() }
}
