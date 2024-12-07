package pl.sebcel.bpg.data.di

import android.icu.util.Measure
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.sebcel.bpg.data.DefaultMeasurementRepository
import pl.sebcel.bpg.data.MyModelRepository
import pl.sebcel.bpg.data.DefaultMyModelRepository
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
    fun bindsMyModelRepository(myModelRepository: DefaultMyModelRepository): MyModelRepository

    @Singleton
    @Binds
    fun bindsMeasurementRepository(measurementRepository: DefaultMeasurementRepository): MeasurementRepository
}

class FakeMyModelRepository @Inject constructor() : MyModelRepository {
    override val myModels: Flow<List<String>> = flowOf(fakeMyModels)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }
}

class FakeMeasurementRepository @Inject constructor() : MeasurementRepository {
    override val measurements: Flow<List<Measurement>> = flowOf(fakeMeasurements)

    override suspend fun add(measurement: Measurement) {
        throw NotImplementedError()
    }
}

val fakeMyModels = listOf("One", "Two", "Three")

val fakeMeasurements = listOf(Measurement(date = Date(), pain = 1, comment = "Fake data"))