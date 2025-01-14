package pl.sebcel.bpg.services.dataupdate

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.sebcel.bpg.data.local.database.model.Notification
import pl.sebcel.bpg.data.local.repositories.MeasurementRepository
import pl.sebcel.bpg.data.local.repositories.NotificationRepository
import pl.sebcel.bpg.services.notifications.NotificationsService
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class DataUpdateService : JobService() {

    private val measurementTimeout : Long = 1 * 60 * 60 * 1000L
    private val notificationTimeout : Long = 30 * 60 * 1000L

    @Inject
    lateinit var measurementRepository: MeasurementRepository

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @SuppressLint("NewApi")
    override fun onStartJob(params: JobParameters): Boolean {
        Log.d("BPG", "Running DataUpdateService")

        scope.launch {
            val lastMeasurementDate = fetchLastMeasurementDate()
            val lastNotificationDate = fetchLastNotificationDate()
            val currentDate = Date()

            Log.d("BPG", "Last measurement date: $lastMeasurementDate")
            Log.d("BPG", "Last notification date: $lastNotificationDate")
            Log.d("BPG", "Current date: $currentDate")

            val timeSinceLastMeasurement = currentDate.time - lastMeasurementDate.time
            val timeSinceLastNotification = currentDate.time - lastNotificationDate.time
            Log.d(
                "BPG",
                "Time since last measurement: ${timeSinceLastMeasurement / 1000} s, timeout: ${measurementTimeout / 1000} s"
            )
            Log.d(
                "BPG",
                "Time since last notification: ${timeSinceLastNotification / 1000} s, timeout: ${notificationTimeout / 1000} s"
            )

            if (timeSinceLastMeasurement > measurementTimeout) {
                Log.d("BPG", "It is time to enter a new measurement!")

                if (timeSinceLastNotification > notificationTimeout) {
                    Log.d("BPG", "It is time to send a new notification - sending")
                    NotificationsService.sendNotification()
                    Log.d("BPG", "Saving new last notification date to the database")
                    notificationRepository.add(Notification(date = currentDate))
                } else {
                    Log.d("BPG", "It is not time to send a new notification yet")
                }

                Log.d("BPG", "Done")
            } else {
                Log.d("BPG", "It is not time to enter a new measurement yet")
            }

            DataUpdateServiceScheduler.scheduleJob(applicationContext) // reschedule the job
        }

        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        job.cancel()
        return true
    }

    private suspend fun fetchLastMeasurementDate() : Date {
        try {
            Log.d("BPG", "Trying to fetch last measurement date from the database")
            val lastMeasurementDateFromDatabase = measurementRepository.getLastMeasurementDate().first()
            Log.d("BPG", "Getting time: ${lastMeasurementDateFromDatabase.time}")
            Log.d("BPG", "Last measurement date fetched from the database")
            return lastMeasurementDateFromDatabase
        } catch (ex : Exception) {
            Log.d("BPG", "Last measurement date was not found in the database")
            return Date()
        }
    }

    private suspend fun fetchLastNotificationDate() : Date {
        try {
            Log.d("BPG", "Trying to fetch last notification date from the database")
            val lastNotificationDateFromDatabase = notificationRepository.getLastNotificationDate().first()
            Log.d("BPG", "Getting time: ${lastNotificationDateFromDatabase.time}")
            Log.d("BPG", "Last notification date fetched from the database")
            return lastNotificationDateFromDatabase
        } catch (ex : Exception) {
            Log.d("BPG", "Last notification date was not found in the database")
            Log.d("BPG", "Inserting the initial notification")
            notificationRepository.add(Notification(date = Date()))
            return Date()
        }
    }
}
