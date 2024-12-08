package pl.sebcel.bpg.ui.mymodel

import pl.sebcel.bpg.ui.theme.BPGTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.PainDescriptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
private val painDescriptions = PainDescriptions()

@Composable
fun MeasurementScreen(modifier: Modifier = Modifier, viewModel: MeasurementViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is MeasurementUiState.Success) {
        MeasurementScreen(
            items = (items as MeasurementUiState.Success).data,
            onSave = viewModel::addMeasurement,
            modifier = modifier
        )
    }
}

@Composable
internal fun MeasurementScreen(
    items: List<Measurement>,
    onSave: (measurement: Measurement) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        var measurementComment by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = measurementComment,
                onValueChange = { measurementComment = it }
            )

            Button(modifier = Modifier.width(96.dp), onClick = { onSave(Measurement(date = Date(), pain = 1, comment = measurementComment)) }) {
                Text("Save")
            }
        }
        items.forEach {
            Row() {
                Text(formatter.format(it.date))
                Text("\t")
                Text(painDescriptions.getPainDescription(it.pain))
            }
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BPGTheme {
        MeasurementScreen(listOf(Measurement(date = Date(), pain = 1, comment = "Sample comment")), onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    BPGTheme {
        MeasurementScreen(listOf(Measurement(date = Date(), pain = 1, comment = "Sample comment")), onSave = {})
    }
}