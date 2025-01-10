package pl.sebcel.bpg.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DataUpdateServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BPG", "Received event " + intent.action)
        DataUpdateServiceScheduler.scheduleJob(context)
    }
}
