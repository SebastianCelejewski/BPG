package pl.sebcel.bpg.services.dataupdate

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.sebcel.bpg.data.local.MeasurementRepository
import pl.sebcel.bpg.services.notifications.NotificationsService
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class DataUpdateService : JobService() {

    private val measurementTimeout : Long = 3 * 60 * 60 * 1000L
    private val notificationTimeout : Long = 1 * 60 * 60 * 1000L
    private var lastNotificationDate : Date = Date(0L)

    @Inject
    lateinit var measurementRepository: MeasurementRepository

    @SuppressLint("NewApi")
    override fun onStartJob(params: JobParameters): Boolean {
        Log.d("BPG", "Running DataUpdateService")

        runBlocking{
            launch {
                val lastMeasurementDate = measurementRepository.getLastMeasurementDate().first()
                Log.d("BPG", "Last measurement date: $lastMeasurementDate")

                val currentDate = Date()
                Log.d("BPG", "Current date: $currentDate")

                val timeSinceLastMeasurement = currentDate.time - lastMeasurementDate.time
                val timeSinceLastNotification = currentDate.time - lastNotificationDate.time
                Log.d("BPG", "Last measurement date: $lastMeasurementDate")
                Log.d("BPG", "Last notification date: $lastNotificationDate")
                Log.d("BPG", "Time since last measurement: ${timeSinceLastMeasurement/1000} s, timeout: ${measurementTimeout/1000} s")
                Log.d("BPG", "Time since last notification: ${timeSinceLastNotification/1000} s, timeout: ${notificationTimeout/1000} s")

                if (timeSinceLastMeasurement > measurementTimeout) {
                    Log.d("BPG", "It is time to enter a new measurement!")

                    if (timeSinceLastNotification > notificationTimeout) {
                        Log.d("BPG", "It is time to send a new notification")
                        NotificationsService.sendNotification()
                        lastNotificationDate = currentDate
                    } else {
                        Log.d("BPG", "It is not time to send a new notification yet")
                    }

                    Log.d("BPG", "Done")
                } else {
                    Log.d("BPG", "It is not time to enter a new measurement yet")
                }
            }
        }

        DataUpdateServiceScheduler.scheduleJob(applicationContext) // reschedule the job

        return true
    }
    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }
}
