package pl.sebcel.bpg.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.MeasurementDao
import java.util.Date
import javax.inject.Inject

interface MeasurementRepository {
    val measurements: Flow<List<Measurement>>

    suspend fun add(measurement: Measurement)

    suspend fun delete(measurement: Measurement)

    suspend fun getLastMeasurementDate() : Flow<Date>
}

class DefaultMeasurementRepository @Inject constructor (private val measurementDao: MeasurementDao) : MeasurementRepository {
    override val measurements: Flow<List<Measurement>> = measurementDao.getMeasurements();

    override suspend fun add(measurement: Measurement) {
        Log.d("BPG", "About to add a new measurement using DAO")
        measurementDao.insertMeasurement(measurement)
        Log.d("BPG", "Added a new measurement using DAO")
    }

    override suspend fun delete(measurement: Measurement) {
        Log.d("BPG", "About to delete an existing measurement using DAO")
        measurementDao.deleteMeasurement(measurement)
        Log.d("BPG", "Deleted an existing measurement using DAO")
    }

    override suspend fun getLastMeasurementDate() : Flow<Date> {
        Log.d("BPG", "About to fetch a list of measurements from DAO")
        val result = measurementDao.getLastMeasurementDate()
        Log.d("BPG", "Fetched list of measurements from DAO")
        return result
    }
}