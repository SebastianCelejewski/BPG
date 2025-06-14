package pl.sebcel.bpg.ui.measurementadd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.extensions.hour
import pl.sebcel.bpg.extensions.minute
import pl.sebcel.bpg.extensions.stripTime
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

        var measurementDate by remember { mutableStateOf(Date().stripTime()) }
        var measurementTime = rememberTimePickerState(Date().hour(), Date().minute(), true)
        var pain by remember { mutableIntStateOf(0) }
        var weatherDescription by remember { mutableStateOf("") }
        var periodStateDescription by remember { mutableStateOf("") }
        var locationDescription by remember { mutableStateOf("") }
        var durationDescription by remember { mutableStateOf("") }
        var comment by remember { mutableStateOf("") }

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
            }
        ){
            innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            )
            {
                Image(
                    painter = painterResource(R.drawable.ornament),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                Surface(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxHeight()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        var selectedDateAndTime = measurementDate.time
                        selectedDateAndTime += (measurementTime.hour * 3600 + measurementTime.minute * 60) * 1000;

                        MeasurementDatePicker(initialDateAndTime = measurementDate, onSelect = {measurementDate = it})
                        MeasurementTimePicker(initialTimePickerState = measurementTime, onSelect = {measurementTime = it})
                        MeasurementHeadachePicker(onSelect = { pain = it })
                        MeasurementStringMetadata(modifier = Modifier, getString(R.string.measurement_weather_label), onSelect = { weatherDescription = it })
                        MeasurementStringMetadata(modifier = Modifier, getString(R.string.measurement_period_state_label), onSelect = { periodStateDescription = it })
                        MeasurementStringMetadata(modifier = Modifier, getString(R.string.measurement_location_label), onSelect = { locationDescription = it })
                        MeasurementStringMetadata(modifier = Modifier, getString(R.string.measurement_duration_label), onSelect = { durationDescription = it })
                        MeasurementStringMetadata(modifier = Modifier, getString(R.string.measurement_comment_label), onSelect = { comment = it })
                        Button(onClick = {
                            viewModel.addMeasurement(
                                Measurement(
                                    date = Date(selectedDateAndTime),
                                    pain = pain,
                                    weatherDescription = weatherDescription,
                                    periodStateDescription = periodStateDescription,
                                    locationDescription = locationDescription,
                                    durationDescription = durationDescription,
                                    comment = comment
                                ))
                            finish()
                        }) {
                            Icon(Icons.Default.Check, contentDescription = stringResource(R.string.add_measurement_button_label))
                        }
                        Spacer(modifier = Modifier.weight(1.0f))
                    }
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