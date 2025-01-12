package pl.sebcel.bpg.services.dataupdate

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.services.notifications.NotificationsService

object DataUpdateServiceScheduler {
    private const val CHECK_INTERVAL : Long = 3 * 60 * 1000

    fun scheduleJob(context: Context) {
        Log.d("BPG", "Scheduling DataUpdateService. Check interval: ${CHECK_INTERVAL/1000/60} minutes")

        NotificationsService.configureNotifications()

        val serviceComponent = ComponentName(context, DataUpdateService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency(CHECK_INTERVAL)
        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())
    }
}
