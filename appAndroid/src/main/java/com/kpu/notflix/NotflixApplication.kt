package com.kpu.nelflix

import android.app.Application
import com.kpu.shared.di.initKoin
import com.kpu.shared.utils.ContextUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class NelflixApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ContextUtils.setContext(context = this)

        initKoin {
            androidLogger(level = Level.NONE)
            androidContext(androidContext = this@NelflixApplication)
        }
    }
}
