package com.example.remedi.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateTimeUtils {

    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun formatDate(day: Int, month: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        val formatter = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val formatter = SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun getCalendarFromDateTime(date: String, time: String): Calendar? {
        return try {
            val formatter = SimpleDateFormat(
                "${Constants.DATE_FORMAT} ${Constants.TIME_FORMAT}",
                Locale.getDefault()
            )

            val parsedDate = formatter.parse("$date $time")

            Calendar.getInstance().apply {
                if (parsedDate != null) {
                    this.time = parsedDate
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}