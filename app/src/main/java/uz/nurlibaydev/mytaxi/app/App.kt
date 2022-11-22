package uz.nurlibaydev.mytaxi.app

import android.app.Application
import timber.log.Timber
import uz.nurlibaydev.mytaxi.BuildConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}