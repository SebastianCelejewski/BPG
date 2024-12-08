package pl.sebcel.bpg.ui.measurementlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle


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
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ListMeasurements(viewModel: MeasurementListViewModel = hiltViewModel()) {
        val items by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.app_name))
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { AddMeasurement() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) {
            innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                if (items is MeasurementListUiState.Success) {
                    MeasurementListTable(
                        items = (items as MeasurementListUiState.Success).data
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun ListMeasurementsPreview() {
        BPGTheme {
            ListMeasurements()
        }
    }

    fun AddMeasurement() {
        val intent = Intent(Intent(baseContext, MeasurementAddActivity::class.java))
        startActivity(intent)
    }
}