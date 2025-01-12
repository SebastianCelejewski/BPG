package pl.sebcel.bpg.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.local.database.AppDatabase
import pl.sebcel.bpg.data.local.database.dao.NotificationDao
import pl.sebcel.bpg.data.local.database.model.MeasurementDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideMeasurementDao(appDatabase: AppDatabase): MeasurementDao {
        return appDatabase.measurementDao()
    }

    @Provides
    fun provideNotificationDao(appDatabase: AppDatabase): NotificationDao {
        return appDatabase.notificationDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            BpgApplication.instance.getString(R.string.bpg_database_name)
        ).build()
    }
}