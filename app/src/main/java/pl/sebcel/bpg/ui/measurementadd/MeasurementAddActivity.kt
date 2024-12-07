package pl.sebcel.bpg.ui.measurementadd

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.sebcel.bpg.ui.measurementlist.MeasurementListActivity
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.PainDescriptions
import pl.sebcel.bpg.ui.mymodel.MeasurementViewModel
import pl.sebcel.bpg.ui.theme.BPGTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MeasurementAddActivity : ComponentActivity() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val painDescriptions = PainDescriptions()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BPGTheme {
                AddNewMeasurement()
            }
            Column {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.app_name))
                    }
                )
            }
        }
    }

    @Composable
    @Preview
    fun AddNewMeasurementPreview() {
        BPGTheme {
            AddNewMeasurement()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddNewMeasurement(viewModel: MeasurementViewModel = hiltViewModel()) {

        var measurementDate by remember { mutableStateOf(Date())}
        var pain by remember { mutableStateOf(0)}
        var comment by remember { mutableStateOf("")}

        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.app_name))
                    }
                )
            }
        ){
            innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(innerPadding)
            ) {
                MeasurementDatePicker(modifier = Modifier.height(36.dp))
                HeadachePicker(onSelect = {
                    pain = it
                })
                Button(onClick = {
                    viewModel.addMeasurement(Measurement(date = measurementDate, pain = pain, comment = comment))
                    val intent = Intent(Intent(baseContext, MeasurementListActivity::class.java))
                        startActivity(intent)
                    }) {
                    Icon(Icons.Default.Check, contentDescription = stringResource(R.string.label_button_measurement_add))
                }
            }
        }
    }

    @Composable
    fun MeasurementDatePicker(modifier : Modifier = Modifier) {
        var showModal by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf<Long?>(null) }

        DateField()

        if (showModal) {
            DatePickerModal(
                onDateSelected = {
                    selectedDate = it
                    showModal = false
                },
                onDismiss = { showModal = false }
            )
        }
    }

    @Composable
    fun HeadachePicker(onSelect: (Int) -> Unit) {
        val radioOptions = listOf(0, 1, 2, 3)
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }
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
    fun DateField(modifier: Modifier = Modifier) {
        var selectedDate by remember { mutableStateOf<Long?>(null) }
        var showModal by remember { mutableStateOf(false) }

        if (selectedDate == null) {
            selectedDate = Date().time
        }

        OutlinedTextField(
            value = selectedDate?.let { convertMillisToDate(it) } ?: formatter.format(Date()),
            onValueChange = { },
            label = { Text("Data pomiaru") },
            placeholder = { Text("MM/DD/YYYY") },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(selectedDate) {
                    awaitEachGesture {
                        // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                        // in the Initial pass to observe events before the text field consumes them
                        // in the Main pass.
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
                onDateSelected = { selectedDate = it },
                onDismiss = { showModal = false }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePickerModal(
        onDateSelected: (Long?) -> Unit,
        onDismiss: () -> Unit
    ) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    fun convertMillisToDate(millis: Long): String {
        return formatter.format(Date(millis))
    }
}