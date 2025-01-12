package pl.sebcel.bpg.data.local.repositories

import kotlinx.coroutines.flow.Flow
import pl.sebcel.bpg.data.local.database.dao.NotificationDao
import pl.sebcel.bpg.data.local.database.model.Notification
import java.util.Date
import javax.inject.Inject

interface NotificationRepository {
    val notifications: Flow<List<Notification>>

    suspend fun add(notification: Notification)

    suspend fun getLastNotificationDate() : Flow<Date>
}

class DefaultNotificationRepository @Inject constructor (private val notificationDao: NotificationDao) :
    NotificationRepository {
    override val notifications: Flow<List<Notification>> = notificationDao.getNotifications();

    override suspend fun add(notification: Notification) {
        notificationDao.insertNotification(notification)
    }

    override suspend fun getLastNotificationDate() : Flow<Date> {
        val result = notificationDao.getLastNotificationDate()
        return result
    }
}