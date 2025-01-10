package pl.sebcel.bpg.ui.measurementlist

import android.util.Log
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
import pl.sebcel.bpg.ui.measurementlist.MeasurementListUiState.Error
import pl.sebcel.bpg.ui.measurementlist.MeasurementListUiState.Success
import javax.inject.Inject

@HiltViewModel
class MeasurementListViewModel @Inject constructor(
    private val measurementRepository: MeasurementRepository
) : ViewModel() {

    val uiState: StateFlow<MeasurementListUiState> = measurementRepository
        .measurements.map<List<Measurement>, MeasurementListUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MeasurementListUiState.Loading)

    fun deleteMeasurement(measurement: Measurement) {
        Log.d("BPG", "Deleting measurement using Measurement Repository")
        viewModelScope.launch {
            measurementRepository.delete(measurement)
        }
        Log.d("BPG", "Deleted measurement using Measurement Repository")
    }
}

sealed interface MeasurementListUiState {
    object Loading : MeasurementListUiState
    data class Error(val throwable: Throwable) : MeasurementListUiState
    data class Success(val data: List<Measurement>) : MeasurementListUiState
}