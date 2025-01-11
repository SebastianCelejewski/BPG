package pl.sebcel.bpg.services.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import pl.sebcel.bpg.ui.measurementadd.MeasurementAddActivity

object NotificationsService {

    private val notificationChannelId : String = BpgApplication.instance.getString(R.string.notification_channel_id)

    fun configureNotifications() {
        Log.i("BPG", "Configuring notifications")

        Thread {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is not in the Support Library.
            Log.d("BPG", "Creating a notification channel")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = BpgApplication.instance.getString(R.string.notification_channel_name)
                val descriptionText =
                    BpgApplication.instance.getString(R.string.notification_channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(notificationChannelId, name, importance).apply {
                    description = descriptionText
                }

                // Register the channel with the system.
                Log.d("BPG", "Registering the notification channel")
                val notificationManager: NotificationManager =
                    BpgApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

            }
            Log.d("BPG", "Creation of a notification channel completed")
        }.start()
    }

    fun sendNotification() {
        Log.i("BPG", "Sending a notification")

        Thread {
            // Create an explicit intent for an Activity in your app.

            Log.d("BPG", "Creating a pending intent")
            val intent = Intent(BpgApplication.instance, MeasurementAddActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                BpgApplication.instance,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )


            Log.d("BPG", "Creating a builder")
            val builder = NotificationCompat.Builder(BpgApplication.instance, notificationChannelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(BpgApplication.instance.getString(R.string.data_update_notification_title))
                .setContentText(BpgApplication.instance.getString(R.string.data_update_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            Log.d("BPG", "Checking permissions")
            with(NotificationManagerCompat.from(BpgApplication.instance)) {
                if (ActivityCompat.checkSelfPermission(
                        BpgApplication.instance,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.e("BPG", "No permission to send notification")
                    // TODO: Consider calling
                    // ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                    //                                        grantResults: IntArray)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return@with
                }

                // notificationId is a unique int for each notification that you must define.
                Log.d("BPG", "Sending notification")
                val notificationId = 1
                notify(notificationId, builder.build())

                Log.d("BPG", "Sending notification complete")
            }
        }.start()
    }
}