package pl.sebcel.bpg.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.sebcel.bpg.data.local.database.converters.DateConverter
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.data.local.database.model.MeasurementDao

@Database(entities = [Measurement::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
}