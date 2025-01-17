package pl.sebcel.bpg.data.local.database.converters

import androidx.room.TypeConverter
import java.util.Date

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