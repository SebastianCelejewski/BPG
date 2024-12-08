package pl.sebcel.bpg.ui.measurementadd

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.ui.measurementlist.MeasurementListActivity
import pl.sebcel.bpg.ui.theme.BPGTheme
import java.util.Date

@AndroidEntryPoint
class MeasurementAddActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BPGTheme {
                AddNewMeasurement()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddNewMeasurement(viewModel: MeasurementAddViewModel = hiltViewModel()) {

        var measurementDate by remember { mutableStateOf(Date())}
        var pain by remember { mutableStateOf(0)}
        var comment by remember { mutableStateOf("")}

        Scaffold (
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(stringResource(R.string.app_name))
                    }
                )
            }
        ){
            innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(innerPadding)
            ) {
                MeasurementDatePicker(modifier = Modifier.height(36.dp))
                MeasurementHeadachePicker(onSelect = {
                    pain = it
                })
                MeasurementComment(onSelect = {
                    comment = it
                })
                Button(onClick = {
                    viewModel.addMeasurement(Measurement(date = measurementDate, pain = pain, comment = comment))
                    val intent = Intent(Intent(baseContext, MeasurementListActivity::class.java))
                        startActivity(intent)
                    }) {
                    Icon(Icons.Default.Check, contentDescription = stringResource(R.string.label_button_measurement_add))
                }
            }
        }
    }

    @Composable
    @Preview
    fun AddNewMeasurementPreview() {
        BPGTheme {
            AddNewMeasurement()
        }
    }
}