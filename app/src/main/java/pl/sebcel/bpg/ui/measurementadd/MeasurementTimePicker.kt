@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package pl.sebcel.bpg.ui.measurementadd

import android.util.Log
import androidx.compose.material3.TimePicker
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import pl.sebcel.bpg.ui.theme.BPGTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun MeasurementTimePicker(initialDateAndTime : Date, onSelect : (Date) -> Unit) {
    var showModal by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<Date>(initialDateAndTime)}

    Log.d("BPG", "Creating measurementTimePicker for date $initialDateAndTime")

    TimeField(initialDateAndTime = initialDateAndTime,
        onSelect = {
            Log.d("BPG1", "MeasurementTimePicker.onTimeSelected, selectedTime: $it")
            selectedTime = it
            Log.d("BPG1", "Output date: ${selectedTime}")
            onSelect(selectedTime)
            showModal = false
         }
    )

    if (showModal) {
        TimePickerModal(
            initialDateAndTime = initialDateAndTime,
            onTimeSelected = {
                Log.d("BPG3", "TimeField.onTimeSelected, selectedTime: $it")
                selectedTime = it
                onSelect(it)
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun TimeField(
    initialDateAndTime : Date,
    modifier: Modifier = Modifier,
    onSelect : (Date) -> Unit) {

    Log.d("BPG", "Creating TimeField with initial date $initialDateAndTime")

    var selectedTime by remember { mutableStateOf<Date>(initialDateAndTime) }
    var showModal by remember { mutableStateOf(false) }
    val df = SimpleDateFormat("HH:mm")

    OutlinedTextField(
        value = df.format(selectedTime),
        onValueChange = { },
        label = { Text(BpgApplication.instance.getString(R.string.measurement_time_field_label)) },
        placeholder = { Text("hh:mm") },
        trailingIcon = {
            Icon(Icons.Default.Notifications, contentDescription = BpgApplication.instance.getString(R.string.select_measurement_time_button_label))
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedTime) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    initialDateAndTime : Date,
    onTimeSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    Log.d("BPG", "Creating TimePickerModal with initial date $initialDateAndTime")

    val currentTime = Calendar.getInstance()
    currentTime.time = initialDateAndTime
    val initialHour = currentTime.get(Calendar.HOUR_OF_DAY)
    val initialMinute = currentTime.get(Calendar.MINUTE)
    val is24Hour = true

    val timePickerState = rememberTimePickerState(initialHour, initialMinute, is24Hour)

    TimePickerDialog(
        onDismiss = {
            Log.d("BPG4", "TimePickerModal.onDismiss")
            onDismiss()
                    },
        onConfirm = {
            Log.d("BPG4", "TimePickerModal.onConfirm, timePickerState: ${timePickerState.hour}:${timePickerState.minute}")
            onTimeSelected(convertHourAndMinuteToDate(timePickerState.hour, timePickerState.minute))
        }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@Composable
@Preview
fun TimePickerDialogPreview() {
    BPGTheme {
        TimePickerDialog({}, {}){ TimePicker(state = TimePickerState(14, 34, true)) }
    }
}

fun convertHourAndMinuteToDate(hour: Int, minute: Int): Date {
    val millis =  (hour * 3600 + minute * 60) * 1000L
    return Date(millis)
}