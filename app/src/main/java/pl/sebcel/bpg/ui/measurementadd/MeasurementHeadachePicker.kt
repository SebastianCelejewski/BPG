package pl.sebcel.bpg.ui.measurementadd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.sebcel.bpg.data.local.database.PainDescriptions

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