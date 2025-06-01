@file:OptIn(ExperimentalMaterial3Api::class)

package pl.sebcel.bpg.extensions

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

fun Date.stripTime() : Date {
    val df = SimpleDateFormat.getDateInstance()
    val result = df.parse(df.format(this))!!
    return result
}

fun Date.hour() : Int {
    val cal = Calendar.getInstance().also {
        it.timeZone = TimeZone.getDefault()
        it.time = this
    }
    return cal.get(Calendar.HOUR_OF_DAY)
}

fun Date.minute() : Int {
    val cal = Calendar.getInstance().also {it.time = this}
    return cal.get(Calendar.MINUTE)
}