package com.example.mall

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private const val DATE_FORMAT = "EEE dd-MM-yyyy"
    private const val TIME_FORMAT = "hh:mm:ss"

    fun currentDate(): Long = System.currentTimeMillis()
    fun randomDate(): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        cal.add(Calendar.DAY_OF_WEEK, (1..4).random())
        return cal.timeInMillis
    }


    fun millisToDate(millis: Long): String = SimpleDateFormat(DATE_FORMAT).format(Date(millis))
    fun millisToTime(millis: Long): String = SimpleDateFormat(TIME_FORMAT).format(Date(millis))
}