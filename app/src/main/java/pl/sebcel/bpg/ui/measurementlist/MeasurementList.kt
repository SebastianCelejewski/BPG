package pl.sebcel.bpg.ui.measurementlist

import pl.sebcel.bpg.ui.theme.BPGTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.PainDescriptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
private val painDescriptions = PainDescriptions()

@Composable
fun MeasurementList(modifier: Modifier = Modifier, viewModel: MeasurementListViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is MeasurementListUiState.Success) {
        MeasurementScreen(
            items = (items as MeasurementListUiState.Success).data,
            modifier = modifier
        )
    }
}

@Composable
internal fun MeasurementScreen(
    items: List<Measurement>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        var measurementComment by remember { mutableStateOf("Compose") }
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
        MeasurementScreen(listOf(Measurement(date = Date(), pain = 1, comment = "Sample comment")))
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    BPGTheme {
        MeasurementScreen(listOf(Measurement(date = Date(), pain = 1, comment = "Sample comment")))
    }
}