package pl.sebcel.bpg.ui.measurementlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import pl.sebcel.bpg.ui.theme.BPGTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.PainDescriptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
private val painDescriptions = PainDescriptions()

@Composable
fun MeasurementListTable(items: List<Measurement>, modifier: Modifier = Modifier) {
    val shape = CircleShape
    Column(modifier) {
        items.forEach {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, shape)
                    .background(MaterialTheme.colorScheme.primary, shape)
                    .padding(16.dp)
                    .fillMaxWidth()
            ){
                Text(
                    formatter.format(it.date),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text("\t")
                Text(
                    painDescriptions.getPainDescription(it.pain),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementListTableRowPreview() {
    BPGTheme {
        MeasurementListTable(
            listOf(
                Measurement(date = Date(), pain = 1, comment = "Sample comment 1"),
                Measurement(date = Date(), pain = 2, comment = "Sample comment 2"),
                Measurement(date = Date(), pain = 0, comment = "Sample comment 3")
            )
        )
    }
}