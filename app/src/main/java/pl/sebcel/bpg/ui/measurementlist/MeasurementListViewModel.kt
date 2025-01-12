package pl.sebcel.bpg.ui.measurementlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.sebcel.bpg.data.local.repositories.MeasurementRepository
import pl.sebcel.bpg.data.local.database.model.Measurement
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
        viewModelScope.launch {
            measurementRepository.delete(measurement)
        }
    }
}

sealed interface MeasurementListUiState {
    data object Loading : MeasurementListUiState
    data class Error(val throwable: Throwable) : MeasurementListUiState
    data class Success(val data: List<Measurement>) : MeasurementListUiState
}