package pl.sebcel.bpg.extensions.dateextensions

import org.junit.Test
import pl.sebcel.bpg.extensions.stripTime
import java.util.Calendar
import java.util.Date
import org.junit.Assert.assertEquals

class When_stripping_time_from_Date_object {

    private val inputDate = Date()
    private var outputDate: Date = inputDate.stripTime()

    @Test fun should_set_hour_to_zero() {
        assertEquals(0, outputDate.get(Calendar.HOUR))
    }

    @Test fun should_set_minute_to_zero() {
        assertEquals(0, outputDate.get(Calendar.MINUTE))
    }

    @Test fun should_set_second_to_zero() {
        assertEquals(0, outputDate.get(Calendar.SECOND))
    }

    @Test fun should_set_milliseconds_to_zero() {
        assertEquals(0, outputDate.get(Calendar.MILLISECOND))
    }

    @Test fun should_not_modify_year() {
        assertEquals(inputDate.get(Calendar.YEAR), outputDate.get(Calendar.YEAR))
    }

    @Test fun should_not_modify_month() {
        assertEquals(inputDate.get(Calendar.MONTH), outputDate.get(Calendar.MONTH))
    }

    @Test fun should_not_modify_day_of_a_month() {
        assertEquals(inputDate.get(Calendar.DAY_OF_MONTH), outputDate.get(Calendar.DAY_OF_MONTH))
    }

    @Test fun should_not_modify_the_time_zone() {
        assertEquals(inputDate.get(Calendar.ZONE_OFFSET), outputDate.get(Calendar.ZONE_OFFSET))
    }

    private fun Date.get(field : Int) : Int {
        return Calendar.getInstance().also {it.time = this}.get(field)
    }
}