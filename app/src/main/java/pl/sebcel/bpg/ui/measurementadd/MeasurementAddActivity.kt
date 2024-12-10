package pl.sebcel.bpg.ui.measurementadd

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    title = {
                        Text(stringResource(R.string.app_name))
                    }
                )
            }
        ){
            innerPadding ->
            Surface(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
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
                    Spacer(modifier = Modifier.weight(1.0f))
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