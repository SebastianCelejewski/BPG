package pl.sebcel.bpg.ui.measurementadd

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MeasurementComment(modifier : Modifier = Modifier, onSelect: (String) -> Unit) {
    var comment by remember { mutableStateOf("") }

    OutlinedTextField(
        value = comment,
        onValueChange = {
            comment = it
            onSelect(it)
        },
        label = { Text("Okoliczności") },
        modifier = modifier.fillMaxWidth()
    )
}