package pl.sebcel.bpg.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import pl.sebcel.bpg.data.local.database.converters.DateConverter
import pl.sebcel.bpg.data.local.database.dao.NotificationDao
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.data.local.database.model.MeasurementDao
import pl.sebcel.bpg.data.local.database.model.Notification

@Database(
    entities = [Measurement::class, Notification::class],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 2, to = 4),
        AutoMigration(from = 3, to = 4, spec = AppDatabase.AutoMigration3to4::class),
    ]
)

@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
    abstract fun notificationDao(): NotificationDao
    @RenameColumn(tableName = "Measurement", fromColumnName = "periodState", toColumnName = "periodStateDescription")
    @RenameColumn(tableName = "Measurement", fromColumnName = "location", toColumnName = "locationDescription")
    class AutoMigration3to4 : AutoMigrationSpec
}