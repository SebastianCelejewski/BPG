package pl.sebcel.bpg.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Entity
data class Measurement (
    @TypeConverters(DateConverter::class)
    val date : Date,

    val pain : Int,

    val comment : String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

object DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measurement ORDER BY uid DESC")
    fun getMeasurements(): Flow<List<Measurement>>

    @Insert
    suspend fun insertMeasurement(item: Measurement)
}