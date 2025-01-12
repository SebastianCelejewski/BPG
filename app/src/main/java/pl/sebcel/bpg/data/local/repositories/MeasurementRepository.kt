package pl.sebcel.bpg.data.local.repositories

import kotlinx.coroutines.flow.Flow
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.data.local.database.model.MeasurementDao
import java.util.Date
import javax.inject.Inject

interface MeasurementRepository {
    val measurements: Flow<List<Measurement>>

    suspend fun add(measurement: Measurement)

    suspend fun delete(measurement: Measurement)

    suspend fun getLastMeasurementDate() : Flow<Date>
}

class DefaultMeasurementRepository @Inject constructor (private val measurementDao: MeasurementDao) :
    MeasurementRepository {
    override val measurements: Flow<List<Measurement>> = measurementDao.getMeasurements();

    override suspend fun add(measurement: Measurement) {
        measurementDao.insertMeasurement(measurement)
    }

    override suspend fun delete(measurement: Measurement) {
        measurementDao.deleteMeasurement(measurement)
    }

    override suspend fun getLastMeasurementDate() : Flow<Date> {
        val result = measurementDao.getLastMeasurementDate()
        return result
    }
}