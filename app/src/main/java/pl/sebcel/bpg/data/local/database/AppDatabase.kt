package pl.sebcel.bpg.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.sebcel.bpg.data.local.database.converters.DateConverter
import pl.sebcel.bpg.data.local.database.dao.NotificationDao
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.data.local.database.model.MeasurementDao
import pl.sebcel.bpg.data.local.database.model.Notification

@Database(
    entities = [Measurement::class, Notification::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
    abstract fun notificationDao(): NotificationDao
}