package com.ivangarzab.talk

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.ivangarzab.data.di.dataModule
import com.ivangarzab.data.network.NetworkRepository
import com.ivangarzab.talk.di.appModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

/**
 * The main [Application] class, responsible for initializing the application and
 * its general dependencies.
 */
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
            modules(listOf(appModule, dataModule))
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.v("Application terminating")
        val networkRepository: NetworkRepository by inject()
        networkRepository.teardown()
    }
}