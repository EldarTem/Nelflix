package com.kpu.shared.di

import com.kpu.shared.utils.DatabaseDriverFactory
import com.kpu.shared.utils.MultiplatformSettingsWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory(context = get()) }
    single { MultiplatformSettingsWrapper().createSettings() }
}
