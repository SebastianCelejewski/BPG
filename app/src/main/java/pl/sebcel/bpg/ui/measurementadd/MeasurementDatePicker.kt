package pl.sebcel.bpg.ui.measurementadd

import android.util.Log
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

@Composable
fun MeasurementDatePicker(initialDateAndTime : Date, onSelect: (Date) -> Unit) {
    var showModal by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Date>(initialDateAndTime) }

    Log.d("BPG", "Initial date setting for date picker: $selectedDate")

    DateField(initialDateAndTime)

    if (showModal) {
        DatePickerModal(
            initialDate = selectedDate,
            onDateSelected = {
                selectedDate = it
                onSelect(it)
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun DateField(initialDate : Date, modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Date>(initialDate) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = formatter.format(selectedDate),
        onValueChange = { },
        label = { Text(BpgApplication.instance.getString(R.string.measurement_date_field_label)) },
        placeholder = { Text(formatter.toPattern()) },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = BpgApplication.instance.getString(R.string.select_measurement_date_button_label))
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            initialDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate : Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDate.time)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                Log.d("BPG", "Date picker state: $datePickerState")
                if (datePickerState.selectedDateMillis != null) {
                    onDateSelected(Date(datePickerState.selectedDateMillis!!))
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(BpgApplication.instance.getString(R.string.cancel_button_label))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
