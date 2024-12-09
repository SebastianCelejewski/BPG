package pl.sebcel.bpg.ui.measurementadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sebcel.bpg.data.MeasurementRepository
import pl.sebcel.bpg.data.local.database.Measurement
import javax.inject.Inject

@HiltViewModel
class MeasurementAddViewModel @Inject constructor(
    private val measurementRepository: MeasurementRepository
) : ViewModel() {

    fun addMeasurement(measurement: Measurement) {
        viewModelScope.launch {
            measurementRepository.add(measurement)
        }
    }
}