package com.konopko.stocktest.app

import android.app.Application
import com.konopko.stocktest.BuildConfig
import com.konopko.stocktest.koin.myModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application(), AppHelper {

    companion object {
        lateinit var instance: App
           private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(myModule)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

    }

    private val currencySign = mapOf("USD" to "$")

    override fun getCurrencySign(currency: String): String
            = currencySign[currency] ?: currency

}