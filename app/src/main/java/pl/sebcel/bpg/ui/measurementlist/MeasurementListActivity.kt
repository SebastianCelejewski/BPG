package pl.sebcel.bpg.ui.measurementlist

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.sebcel.bpg.R
import pl.sebcel.bpg.ui.measurementadd.MeasurementAddActivity
import pl.sebcel.bpg.ui.theme.BPGTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.sebcel.bpg.data.di.FakeMeasurementRepository

@AndroidEntryPoint
class MeasurementListActivity : ComponentActivity() {

    val notificationChannelId : String = "BPG Channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BPGTheme {
                ListMeasurements()
            }
        }
        ConfigureNotifications()

        //DataUpdateServiceScheduler.scheduleJob(baseContext)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ListMeasurements(viewModel: MeasurementListViewModel = hiltViewModel()) {
        val items by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    title = {
                        Image(
                            painter = painterResource(R.drawable.header),
                            contentDescription = stringResource(R.string.app_name)
                        )
                    }
                )
            },
            floatingActionButton = {
                Row {
                    FloatingActionButton(onClick = { AddMeasurement() }) {
                        Icon(Icons.Default.Add, contentDescription = getString(R.string.add_button_label))
                    }
                    FloatingActionButton(onClick = { SendNotification() }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Wyślij powiadomienie")
                    }
                }
            }
        ) {
            innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Image(
                    painter = painterResource(R.drawable.ornament),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Column {
                        if (items is MeasurementListUiState.Success) {
                            MeasurementListTable(
                                measurements = (items as MeasurementListUiState.Success).data,
                                onDelete = {  viewModel.deleteMeasurement(it) }
                            )
                        }
                    }
                }

            }
        }
    }

    private fun ConfigureNotifications() {
        Log.i("BPG", "Configuring notifications")

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        Log.d("BPG", "Creating a notification channel")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BPG Notification Channel Name"
            val descriptionText = "BPG Notification Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notificationChannelId, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system.
            Log.d("BPG", "Registering the notification channel")
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
        Log.d("BPG", "Creation of a notification channel completed")
    }

    private fun SendNotification() {
        Log.i("BPG", "Sending a notification")

        // Create an explicit intent for an Activity in your app.

        Log.d("BPG", "Creating a pending intent")
        val intent = Intent(this, MeasurementAddActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        Log.d("BPG", "Creating a builder")
        var builder = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification title")
            .setContentText("Notification text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        Log.d("BPG", "Checking permissions")
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MeasurementListActivity,
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
    }

    @Preview
    @Composable
    fun ListMeasurementsPreview() {
        BPGTheme {
            ListMeasurements(viewModel = MeasurementListViewModel(FakeMeasurementRepository()))
        }
    }

    fun AddMeasurement() {
        val intent = Intent(Intent(baseContext, MeasurementAddActivity::class.java))
        startActivity(intent)
    }
}