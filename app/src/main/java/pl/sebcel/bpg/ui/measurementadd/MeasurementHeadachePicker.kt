package pl.sebcel.bpg.ui.measurementadd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.sebcel.bpg.data.local.database.model.PainDescriptions
import pl.sebcel.bpg.ui.theme.BPGTheme

private val painDescriptions = PainDescriptions()

@Composable
fun MeasurementHeadachePicker(onSelect: (Int) -> Unit) {
    val radioOptions = listOf(0, 1, 2, 3)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0] ) }
    Column {
        radioOptions.forEach { value ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (value == selectedOption),
                        onClick = {
                            onOptionSelected(value)
                            onSelect(value)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (value == selectedOption),
                    onClick = {
                        onOptionSelected(value)
                        onSelect(value)
                    }
                )
                Text(
                    text = painDescriptions.getPainDescription(value)
                )
            }
        }
    }
}

@Composable
@Preview
fun MeasurementHeadachePickerPreview() {
    BPGTheme {
        MeasurementHeadachePicker(
            onSelect = {  }
        )
    }
}