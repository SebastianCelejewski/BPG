package pl.sebcel.bpg.ui.measurementadd

import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R

@Composable
fun MeasurementComment(modifier : Modifier = Modifier, onSelect: (String) -> Unit) {
    var comment by remember { mutableStateOf("") }

    OutlinedTextField(
        value = comment,
        onValueChange = {
            comment = it
            onSelect(it)
        },
        label = { Text(BpgApplication.instance.getString(R.string.measurement_comment_label)) },
        modifier = modifier.fillMaxWidth()
    )
}