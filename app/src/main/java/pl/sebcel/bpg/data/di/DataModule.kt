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
import pl.sebcel.bpg.data.local.repositories.DefaultNotificationRepository
import pl.sebcel.bpg.data.local.repositories.NotificationRepository
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsMeasurementRepository(measurementRepository: DefaultMeasurementRepository): MeasurementRepository

    @Singleton
    @Binds
    fun bindsNotificationRepository(notificationRepository: DefaultNotificationRepository): NotificationRepository
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
    Measurement(date = Date(), pain = 1, comment = "Fake data 1", weatherDescription = "Weather 1", periodStateDescription = "State 1", locationDescription = "Location 1", durationDescription = "Duration 1"),
    Measurement(date = Date(), pain = 2, comment = "Fake data 2", weatherDescription = "Weather 2", periodStateDescription = "State 2", locationDescription = "Location 2", durationDescription = "Duration 2"),
    Measurement(date = Date(), pain = 0, comment = "Fake data 3", weatherDescription = "Weather 3", periodStateDescription = "State 3", locationDescription = "Location 3", durationDescription = "Duration 3")
)