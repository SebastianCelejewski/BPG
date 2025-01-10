package pl.sebcel.bpg.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log

object DataUpdateServiceScheduler {
    fun scheduleJob(context: Context) {
        Log.d("BPG", "Scheduling DataUpdateService")
        val serviceComponent = ComponentName(context, DataUpdateService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((5 * 1000).toLong()) // wait at least 1 minute
        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())
    }
}
