package com.zednull.timetable.structure

import com.zednull.timetable.weekIndex
import java.util.*


class Lesson(var name: String, var teacherName: String, var audience: String, var type: String, var start: Int, var end: Int, var index: Int?) {

}

class Day(var lessons1: List<Lesson> = listOf(),var lessons2: List<Lesson> = listOf()) {
    fun getLessons(date: Date, index: Int): List<Lesson> {
        return if(date.weekIndex() == 0) ( if(index == 0) lessons1 else lessons2) else ( if(index == 0) lessons2 else lessons1)
    }
}

class TimeTableStructure(var name: String, var firstWeek: String, var secondWeek: String, var days: List<Day>) {
    fun getWeekName(date: Date, index: Int): String {
        return if(date.weekIndex() == 0) (if(index == 0) firstWeek else secondWeek) else (if(index == 0) secondWeek else firstWeek)
    }

}

val emptyTimeTable: TimeTableStructure
    get() = TimeTableStructure("","","", listOf(
        Day(),Day(),Day(),Day(),Day(),Day(),Day()
    ))

val mainDomain = "studrasp.ru"

class ServerTimeTable(var id: Int, var info: TimeTableStructure) {

}

class error(var code: Int, var message: String) {

}

class requestStruct(var error: error, var timetable: ServerTimeTable?, var session: String?, var login: String?) {

}

class user(var login: String, var session: String) {

}