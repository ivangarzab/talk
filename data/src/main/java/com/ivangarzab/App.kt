package com.ivangarzab

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeLogger()
        initializeDependencyInjection()
    }

    @SuppressLint("LogNotTimber")
    private fun initializeLogger() {
        Log.d("App", "Starting Timber for logging")
        Timber.plant(Timber.DebugTree())
    }

    private fun initializeDependencyInjection() {
        Timber.v("Starting Koin for dependency injection")
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }
}

val appModule = module {

}