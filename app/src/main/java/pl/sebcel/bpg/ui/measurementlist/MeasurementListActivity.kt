package pl.sebcel.bpg.ui.measurementlist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.di.FakeMeasurementRepository
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.export.ExcelExporter
import pl.sebcel.bpg.services.dataupdate.DataUpdateServiceScheduler
import pl.sebcel.bpg.ui.measurementadd.MeasurementAddActivity
import pl.sebcel.bpg.ui.theme.BPGTheme

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
                    FloatingActionButton(onClick = { AddMeasurement() }) {
                        Icon(Icons.Default.Add, contentDescription = getString(R.string.add_button_label))
                    }
                    if (items is MeasurementListUiState.Success) {
                        val measurements = (items as MeasurementListUiState.Success).data
                        FloatingActionButton(onClick = { exportToExcel(measurements, snackbarHostState, scope) }) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = getString(R.string.export_button_label)
                            )
                        }
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

    private fun askForPermissions(permissions: Array<String>, requestCode: Int) {
        Log.d("BPG", "Checking permissions")
        val permissionsToRequest : MutableList<String> = mutableListOf<String>()
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this@MeasurementListActivity, it) == PackageManager.PERMISSION_GRANTED) {
                Log.d("BPG", "Permission $it is granted")
            } else {
                Log.d("BPG", "Permission $it is not granted")
                permissionsToRequest.add(it)
            }
        }

        Log.d("BPG", "Requesting permissions")
        ActivityCompat.requestPermissions(this@MeasurementListActivity, permissions, requestCode)

        Log.d("BPG", "Checking if permissions were granted")
        val permissionsNotGranted : MutableList<String> = mutableListOf()
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this@MeasurementListActivity, it) == PackageManager.PERMISSION_GRANTED) {
                Log.d("BPG", "Permission $it is granted")
            } else {
                Log.d("BPG", "Permission $it is not granted")
                permissionsNotGranted.add(it)
            }
        }

        if (permissionsNotGranted.isNotEmpty()) {
            throw Exception("Failed to get permissions necessary to perform the export: ${permissionsNotGranted.joinToString(", ")}")
        }
    }

    private fun exportToExcel(measurements: List<Measurement>, snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
        val requestId = 1

        try {
            askForPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), requestId)
            val fileName = ExcelExporter.export(measurements)
            scope.launch {
                val result = snackbarHostState.showSnackbar(message = getString(R.string.export_completed_toast_text) + fileName, duration = SnackbarDuration.Long, actionLabel = "Otwórz")
                when (result) {
                    SnackbarResult.ActionPerformed -> {

                    }
                    SnackbarResult.Dismissed -> {

                    }
                }

            }
        } catch (ex: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar(message = getString(R.string.export_failed_toast_text) + ex.message, duration = SnackbarDuration.Long)
            }
            Log.e("BPG", "Export failed: ${ex.message}", ex)
        }
    }
}