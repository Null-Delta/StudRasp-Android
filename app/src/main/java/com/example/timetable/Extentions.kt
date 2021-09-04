package com.example.timetable

import java.security.MessageDigest
import java.time.LocalDate
import java.util.*

fun Date.dayOfMounts() : Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.dayOfWeek() : Int {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK)
}

fun Date.weekDayNum(): Int {
    return when (this.dayOfWeek()) {
        Calendar.MONDAY -> 1
        Calendar.TUESDAY -> 2
        Calendar.WEDNESDAY -> 3
        Calendar.THURSDAY -> 4
        Calendar.FRIDAY -> 5
        Calendar.SATURDAY -> 6
        Calendar.SUNDAY -> 7
        else -> -1
    }
}

fun Date.isToday() : Boolean {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()

    cal1.time = Date()
    cal2.time = this

    return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
}

fun Date.firstDayOfWeek() : Date {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.time = this
    calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek

    return calendar.time
}

fun Date.addDays(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, days)
    return calendar.time
}

fun Date.addMinutes(minutes: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MINUTE, minutes)
    return calendar.time
}

fun Date.weekIndex(): Int {
    var checkDate = this.firstDayOfWeek()
    var calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.time = checkDate

    return (calendar.get(Calendar.WEEK_OF_YEAR) + 1) % 2
}

fun Date.weekDayName(): String {
    return when (this.dayOfWeek()) {
        Calendar.MONDAY -> "Пн"
        Calendar.TUESDAY -> "Вт"
        Calendar.WEDNESDAY -> "Ср"
        Calendar.THURSDAY -> "Чт"
        Calendar.FRIDAY -> "Пт"
        Calendar.SATURDAY -> "Сб"
        Calendar.SUNDAY -> "Вс"
        else -> ""
    }
}

fun Date.minutes(): Int {
    var calendar = Calendar.getInstance()
    calendar.time = this

    return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
}

fun String.sha256(): String {
    return MessageDigest.getInstance("SHA-256").digest(this.toByteArray()).toHex()
}
fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}