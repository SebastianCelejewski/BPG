package pl.sebcel.bpg.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.sebcel.bpg.data.local.repositories.DefaultMeasurementRepository
import pl.sebcel.bpg.data.local.repositories.MeasurementRepository
import pl.sebcel.bpg.data.local.database.model.Measurement
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsMeasurementRepository(measurementRepository: DefaultMeasurementRepository): MeasurementRepository
}

class FakeMeasurementRepository @Inject constructor() : MeasurementRepository {
    override val measurements: Flow<List<Measurement>> = flowOf(fakeMeasurements)

    override suspend fun add(measurement: Measurement) {
        throw NotImplementedError()
    }

    override suspend fun delete(measurement: Measurement) {
        throw NotImplementedError()
    }

    override suspend fun getLastMeasurementDate() : Flow<Date> {
        return flowOf(Date())
    }
}

val fakeMeasurements = listOf(
    Measurement(date = Date(), pain = 1, comment = "Fake data 1"),
    Measurement(date = Date(), pain = 2, comment = "Fake data 2"),
    Measurement(date = Date(), pain = 0, comment = "Fake data 3")
)