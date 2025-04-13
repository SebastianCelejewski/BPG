package pl.sebcel.bpg.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import pl.sebcel.bpg.data.local.database.converters.DateConverter
import java.util.Date

@Entity
data class Measurement (
    @TypeConverters(DateConverter::class)
    val date : Date,

    val pain : Int,

    val comment : String,

    val weatherDescription: String?,

    val periodStateDescription: String?,

    val locationDescription: String?,

    val durationDescription: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

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