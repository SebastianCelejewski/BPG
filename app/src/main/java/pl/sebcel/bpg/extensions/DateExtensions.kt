package pl.sebcel.bpg.extensions

import java.text.SimpleDateFormat
import java.util.Date

fun Date.stripHours() : Date {
    val df = SimpleDateFormat.getDateInstance()
    return df.parse(df.format(this))!!
}

fun Date.stripDate() : Date {
    val df = SimpleDateFormat.getTimeInstance()
    return df.parse(df.format(this))!!
}