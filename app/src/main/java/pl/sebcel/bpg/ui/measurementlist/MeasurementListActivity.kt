package pl.sebcel.bpg.ui.measurementlist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.di.FakeMeasurementRepository
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.export.ExcelExporter
import pl.sebcel.bpg.services.dataupdate.DataUpdateServiceScheduler
import pl.sebcel.bpg.ui.measurementadd.MeasurementAddActivity
import pl.sebcel.bpg.ui.theme.BPGTheme
import pl.sebcel.bpg.ui.trivia.TriviaActivity
import java.io.File

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
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

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
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                Row {
                    FloatingActionButton(
                        onClick = { addMeasurement() },
                        modifier = Modifier.padding(3.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = getString(R.string.add_button_label))
                    }
                    if (items is MeasurementListUiState.Success) {
                        val measurements = (items as MeasurementListUiState.Success).data
                        FloatingActionButton(
                            onClick = { share(measurements, snackbarHostState, scope, this@MeasurementListActivity) }, // { ExcelExporter.exportToExcel(measurements, snackbarHostState, scope, this@MeasurementListActivity) },
                            modifier = Modifier.padding(3.dp)
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = getString(R.string.share_button_label)
                            )
                        }
                    }
                    FloatingActionButton(
                        onClick = { displayTrivia() },
                        modifier = Modifier.padding(3.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = getString(R.string.trivia_button_label))
                    }
                }
            }
        ) {
            innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Image(
                    painter = painterResource(R.drawable.ornament),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
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

    private fun displayTrivia() {
        val intent = Intent(Intent(baseContext, TriviaActivity::class.java))
        startActivity(intent)
    }

    @Preview
    @Composable
    fun ListMeasurementsPreview() {
        BPGTheme {
            ListMeasurements(viewModel = MeasurementListViewModel(FakeMeasurementRepository()))
        }
    }

    private fun addMeasurement() {
        val intent = Intent(Intent(baseContext, MeasurementAddActivity::class.java))
        startActivity(intent)
    }

    private fun share(measurements: List<Measurement>, snackbarHostState: SnackbarHostState, scope: CoroutineScope, activity: Activity) {
        val exportedFileName = ExcelExporter.exportToExcel(measurements, snackbarHostState, scope, this@MeasurementListActivity)

        if (exportedFileName != null) {
            val requestFile = File(exportedFileName)
            val uri = FileProvider.getUriForFile(activity, "pl.sebcel.bpg.fileprovider", requestFile)

            Log.d("BPG", "Exported file name: $exportedFileName")
            Log.d("BPG", "Exported file URI: $uri")

            Log.d("BPG", "Creating intent")
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.data_export_introductory_text))
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "text/csv"
            }

            Log.d("BPG", "Granting permission to read the shared file")
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            Log.d("BPG", "Launching sharing activity")
            startActivity(sendIntent)
            Log.d("BPG", "Sharing completed")
        }
    }
}