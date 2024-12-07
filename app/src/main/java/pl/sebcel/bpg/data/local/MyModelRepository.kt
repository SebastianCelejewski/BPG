package pl.sebcel.bpg.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.MeasurementDao
import pl.sebcel.bpg.data.local.database.MyModel
import pl.sebcel.bpg.data.local.database.MyModelDao
import javax.inject.Inject

interface MyModelRepository {
    val myModels: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultMyModelRepository @Inject constructor(
    private val myModelDao: MyModelDao
) : MyModelRepository {

    override val myModels: Flow<List<String>> =
        myModelDao.getMyModels().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        myModelDao.insertMyModel(MyModel(name = name))
    }
}

interface MeasurementRepository {
    val measurements: Flow<List<Measurement>>

    suspend fun add(measurement : Measurement)
}

class DefaultMeasurementRepository @Inject constructor (private val measurementDao: MeasurementDao) : MeasurementRepository {
    override val measurements: Flow<List<Measurement>> = measurementDao.getMeasurements();

    override suspend fun add(measurement: Measurement) {
        measurementDao.insertMeasurement(measurement)
    }
}