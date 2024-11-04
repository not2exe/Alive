package ru.notexe.alive.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.notexe.alive.app.di.appModules

internal class AliveApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AliveApp)
            modules(appModules())
        }
    }
}