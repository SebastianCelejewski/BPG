package pl.sebcel.bpg.data

import kotlinx.coroutines.flow.Flow
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.MeasurementDao
import javax.inject.Inject

interface MeasurementRepository {
    val measurements: Flow<List<Measurement>>

    suspend fun add(measurement: Measurement)

    suspend fun delete(measurement: Measurement)
}

class DefaultMeasurementRepository @Inject constructor (private val measurementDao: MeasurementDao) : MeasurementRepository {
    override val measurements: Flow<List<Measurement>> = measurementDao.getMeasurements();

    override suspend fun add(measurement: Measurement) {
        measurementDao.insertMeasurement(measurement)
    }

    override suspend fun delete(measurement: Measurement) {
        measurementDao.deleteMeasurement(measurement)
    }

}