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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import pl.sebcel.bpg.R
import pl.sebcel.bpg.ui.measurementadd.MeasurementAddActivity
import pl.sebcel.bpg.ui.mymodel.MeasurementScreen
import pl.sebcel.bpg.ui.mymodel.MyModelScreen
import pl.sebcel.bpg.ui.theme.BPGTheme

@AndroidEntryPoint
class MeasurementListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BPGTheme {
                MeasurementList()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MeasurementList() {
        var presses by remember { mutableIntStateOf(0) }
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
            Column {
                Text(
                    text = "elemele-dutki",
                    modifier = Modifier.padding(innerPadding)
                )
                MyModelScreen()
                MeasurementScreen()
            }
        }
    }

    @Preview
    @Composable
    fun MeasurementListPreview() {
        BPGTheme {
            MeasurementList()
        }
    }

    fun AddMeasurement() {
        val intent = Intent(Intent(baseContext, MeasurementAddActivity::class.java))
        startActivity(intent)
    }
}