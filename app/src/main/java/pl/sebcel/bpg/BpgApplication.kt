package pl.sebcel.bpg

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BpgApplication : Application() {
    companion object {
        lateinit var instance: BpgApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}