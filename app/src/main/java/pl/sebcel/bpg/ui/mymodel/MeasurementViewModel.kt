package pl.sebcel.bpg.ui.mymodel

import android.icu.util.Measure
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.sebcel.bpg.data.MeasurementRepository
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.ui.mymodel.MeasurementUiState.Success
import pl.sebcel.bpg.ui.mymodel.MeasurementUiState.Error
import javax.inject.Inject

@HiltViewModel
class MeasurementViewModel @Inject constructor(
    private val measurementRepository: MeasurementRepository
) : ViewModel() {

    val uiState: StateFlow<MeasurementUiState> = measurementRepository
        .measurements.map<List<Measurement>, MeasurementUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MeasurementUiState.Loading)

    fun addMeasurement(measurement: Measurement) {
        viewModelScope.launch {
            measurementRepository.add(measurement)
        }
    }
}

sealed interface MeasurementUiState {
    object Loading : MeasurementUiState
    data class Error(val throwable: Throwable) : MeasurementUiState
    data class Success(val data: List<Measurement>) : MeasurementUiState
}