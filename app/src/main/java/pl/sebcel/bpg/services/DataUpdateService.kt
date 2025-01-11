package pl.sebcel.bpg.services

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import pl.sebcel.bpg.data.local.MeasurementRepository
import javax.inject.Inject


@AndroidEntryPoint
class DataUpdateService : JobService() {

   @Inject
   lateinit var measurementRepository: MeasurementRepository

   @SuppressLint("NewApi")
   override fun onStartJob(params: JobParameters): Boolean {
       Log.d("BPG", "Running DataUpdateService")
/*
       runBlocking{
           launch {
               val lastMeasurementDate = measurementRepository.getLastMeasurementDate().first()
               Log.d("BPG", "Last measurement date: " + lastMeasurementDate)

               val currentDate = Date()
               Log.d("BPG", "Current date: " + currentDate)

               val delay = currentDate.time - lastMeasurementDate.time
               Log.d("BPG", "Delay: " + delay)

               if (delay > 3 * 3600 * 1000) {
                   Log.d("BPG", "Time to enter a new measurement!")
/*

                   Log.d("BPG", "Getting notification manager instance")
                   val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                   val id = "my_channel_01"
                   val name: CharSequence = "BPG channel"
                   val description = "BPG channel description"
                   val importance = NotificationManager.IMPORTANCE_DEFAULT

                   Log.d("BPG", "Creating instance of notification channel")
                   val mChannel = NotificationChannel(id, name, importance)

                   Log.d("BPG", "Setting  notification channel properties")
                   mChannel.description = description
                   mChannel.enableLights(true)
                   mChannel.lightColor = Color.RED
                   mChannel.enableVibration(true)
                   mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

                   Log.d("BPG", "Creating notification channel using notification manager")
                   mNotificationManager.createNotificationChannel(mChannel)

                   Log.d("BPG", "Notification channels:")
                   mNotificationManager.notificationChannels.forEach{ channel -> Log.d("BPG", "Channel: " + channel.id)}

                   Log.d("BPG", "Creating notification builder")
                   val builder: NotificationCompat.Builder =
                       NotificationCompat.Builder(baseContext)
                           .setSmallIcon(R.drawable.btn_plus)
                           .setContentTitle("My Notification Title")
                           .setContentText("Something interesting happened")
                           .setChannelId(mChannel.id)
                   val notificationId = 1

                   Log.d("BPG", "Creating target intent")
                   val targetIntent: Intent = Intent(
                       baseContext,
                       MeasurementAddActivity::class.java
                   )

                   Log.d("BPG", "Creating content intent")
                   val contentIntent = PendingIntent.getActivity(
                       baseContext,
                       0,
                       targetIntent,
                       PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
                   )
                   builder.setContentIntent(contentIntent)

                   Log.d("BPG", "Toast?")
                   Toast.makeText(baseContext, "Trele-morele", Toast.LENGTH_LONG).show()

                   Log.d("BPG", "Sending notification")
                   mNotificationManager.notify(notificationId, builder.build())
                   Log.d("BPG", "Notification sent")

 */

                   val notificationId = 1
                   val channelId = "bpg_notification_channel"
                   val channelName = "BPG Notification Channel"
                   val channelDescription = "Channel to notify user that measurement should be entered"

                   Log.d("BPG", "Creating target intent")
                   val targetIntent: Intent = Intent(
                       baseContext,
                       MeasurementAddActivity::class.java
                   )

                   Log.d("BPG", "Creating content intent")
                   val contentIntent = PendingIntent.getActivity(
                       baseContext,
                       0,
                       targetIntent,
                       PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
                   )

                   Log.d("BPG", "Creating intent")
                   var builder = NotificationCompat.Builder(baseContext, channelId)
                       .setSmallIcon(R.drawable.star_on)
                       .setContentTitle("My notification")
                       .setContentText("Much longer text that cannot fit one line...")
                       .setStyle(NotificationCompat.BigTextStyle()
                           .bigText("Much longer text that cannot fit one line..."))
                       .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                       .setContentIntent(contentIntent)

                   Log.d("BPG", "Creating notification channel")
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       Log.d("BPG", "Build version " + Build.VERSION.SDK_INT + " is greater then " + Build.VERSION_CODES.O)
                       val name = channelName
                       val descriptionText = channelDescription
                       val importance = NotificationManager.IMPORTANCE_DEFAULT
                       val channel = NotificationChannel(channelId, name, importance).apply {
                           description = descriptionText
                       }

                       val notificationManager: NotificationManager =
                           getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                       notificationManager.createNotificationChannel(channel)
                   } else {
                       Log.d("BPG", "Build version too low: " + Build.VERSION.SDK_INT)
                   }

                   Log.d("BPG", "Preparing to send notification")
                   with(NotificationManagerCompat.from(this.getcontext)) {
                       if (ActivityCompat.checkSelfPermission(
                               baseContext,
                               Manifest.permission.POST_NOTIFICATIONS
                           ) != PackageManager.PERMISSION_GRANTED
                       ) {
                           Log.d("BPG", "Permission is not granted")

                           // TODO: Consider calling
                           // ActivityCompat#requestPermissions
                           // here to request the missing permissions, and then overriding
                           // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                           //                                        grantResults: IntArray)
                           // to handle the case where the user grants the permission. See the documentation
                           // for ActivityCompat#requestPermissions for more details.

                           return@with
                       } else {
                           Log.d("BPG", "Permission is granted")
                       }

                       Log.d("BPG", "Sending notification")
                       notify(notificationId, builder.build())
                       Log.d("BPG", "Notification sent")
                   }

                   Log.d("BPG", "Done")
               }
           }
       }

       DataUpdateServiceScheduler.scheduleJob(applicationContext) // reschedule the job
       */
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }
}
