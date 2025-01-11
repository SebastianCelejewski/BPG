package pl.sebcel.bpg.ui.measurementlist

import android.content.Intent
import androidx.compose.foundation.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.sebcel.bpg.services.notifications.NotificationsService
import pl.sebcel.bpg.data.di.FakeMeasurementRepository
import pl.sebcel.bpg.services.dataupdate.DataUpdateServiceScheduler
import javax.inject.Inject

@AndroidEntryPoint
class MeasurementListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BPGTheme {
                ListMeasurements()
            }
        }

        DataUpdateServiceScheduler.scheduleJob(baseContext)
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
                    FloatingActionButton(onClick = { NotificationsService.sendNotification() }) {
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