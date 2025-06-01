package pl.sebcel.bpg.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun Date.stripTime() : Date {
    val df = SimpleDateFormat.getDateInstance().also {it.timeZone = TimeZone.getTimeZone("UTC")}
    val result = df.parse(df.format(this))!!
    return result
}

fun Date.stripDate() : Date {
    val df = SimpleDateFormat("HH:mm:ss.SSS'Z'").also {it.timeZone = TimeZone.getTimeZone("UTC")}
    val result = df.parse(df.format(this))!!
    return result
}