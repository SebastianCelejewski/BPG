package pl.sebcel.bpg.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.sebcel.bpg.data.DefaultMeasurementRepository
import pl.sebcel.bpg.data.MeasurementRepository
import pl.sebcel.bpg.data.local.database.Measurement
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
}

val fakeMeasurements = listOf(Measurement(date = Date(), pain = 1, comment = "Fake data"))