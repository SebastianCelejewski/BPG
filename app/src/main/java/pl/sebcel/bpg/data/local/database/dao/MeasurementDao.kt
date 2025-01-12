package pl.sebcel.bpg.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.sebcel.bpg.data.local.database.model.Measurement
import java.util.Date

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measurement ORDER BY uid DESC")
    fun getMeasurements(): Flow<List<Measurement>>

    @Query("SELECT date FROM measurement ORDER BY date DESC LIMIT 1")
    fun getLastMeasurementDate() : Flow<Date>

    @Insert
    suspend fun insertMeasurement(item: Measurement)

    @Delete
    suspend fun deleteMeasurement(item: Measurement)
}