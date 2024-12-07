package pl.sebcel.bpg.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MyModel::class, Measurement::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myModelDao(): MyModelDao
    abstract fun measurementDao(): MeasurementDao
}