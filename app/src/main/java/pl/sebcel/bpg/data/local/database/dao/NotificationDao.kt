package pl.sebcel.bpg.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.data.local.database.model.Notification
import java.util.Date

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notification ORDER BY uid DESC")
    fun getNotifications(): Flow<List<Measurement>>

    @Query("SELECT date FROM notification ORDER BY date DESC LIMIT 1")
    fun getLastNotificationDate() : Flow<Date>

    @Insert
    suspend fun insertNotification(item: Notification)

}