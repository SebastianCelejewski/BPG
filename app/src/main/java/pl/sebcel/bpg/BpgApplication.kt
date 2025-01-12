package pl.sebcel.bpg

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BpgApplication : Application() {
    companion object {
        lateinit var instance: BpgApplication private set
    }

    override fun onCreate() {
        Log.i("BPG", "Creating application Boli Pocia Główka")
        super.onCreate()
        instance = this
    }

    override fun onTerminate() {
        Log.i("BPG", "Terminating application Boli Pocia Główka")
        super.onTerminate()
    }
}