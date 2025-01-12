package pl.sebcel.bpg.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import pl.sebcel.bpg.data.local.database.converters.DateConverter
import java.util.Date

@Entity
data class Notification (
    @TypeConverters(DateConverter::class)
    val date : Date
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}
