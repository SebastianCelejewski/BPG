package pl.sebcel.bpg.extensions.dateextensions

import org.junit.Test
import java.util.Calendar
import java.util.Date
import org.junit.Assert.assertEquals
import pl.sebcel.bpg.extensions.stripDate
import java.util.TimeZone

class When_stripping_date_from_Date_object {

    private val inputDate = Date()
    private var outputDate: Date = inputDate.stripDate()

    @Test fun should_not_modify_hour() {
        assertEquals(inputDate.get(Calendar.HOUR), outputDate.get(Calendar.HOUR))
    }

    @Test fun should_not_modify_minute() {
        assertEquals(inputDate.get(Calendar.MINUTE), outputDate.get(Calendar.MINUTE))
    }

    @Test fun should_not_modify_second() {
        assertEquals(inputDate.get(Calendar.SECOND), outputDate.get(Calendar.SECOND))
    }

    @Test fun should_not_modify_millisecond() {
        assertEquals(inputDate.get(Calendar.MILLISECOND), outputDate.get(Calendar.MILLISECOND))
    }

    @Test fun should_set_year_to_1970() {
        assertEquals(1970, outputDate.get(Calendar.YEAR))
    }

    @Test fun should_set_month_to_JANUARY() {
        assertEquals(0, outputDate.get(Calendar.MONTH))
    }

    @Test fun should_set_day_of_a_month_to_1() {
        assertEquals(1, outputDate.get(Calendar.DAY_OF_MONTH))
    }

    @Test fun should_not_modify_the_time_zone() {
        assertEquals(inputDate.get(Calendar.ZONE_OFFSET), outputDate.get(Calendar.ZONE_OFFSET))
    }

    private fun Date.get(field : Int) : Int {
        return Calendar.getInstance().also {
            it.time = this
            it.timeZone = TimeZone.getTimeZone("UTC")
        }.get(field)
    }
}