package com.zednull.timetable.structure

import com.zednull.timetable.components.ui.globalTablesInfo
import com.zednull.timetable.weekIndex
import java.util.*


class Lesson(var name: String, var teacherName: String, var audience: String, var type: String, var lessonNumber: Int) {

}

class Day(var lessons1: List<Lesson> = listOf(),var lessons2: List<Lesson> = listOf()) {
    fun getLessons(date: Date, index: Int): List<Lesson> {
        return if(date.weekIndex() == 0) ( if(index == 0) lessons1 else lessons2) else ( if(index == 0) lessons2 else lessons1)
    }

    fun changeLessons(date: Date, index: Int, action: (List<Lesson>) -> Void) {
        if(date.weekIndex() != index) {
            action(lessons2)
        } else {
            action(lessons1)
        }
    }
}

class LessonTime(var start: Int, var end: Int) { }

class TimeTableStructure(var name: String, var firstWeek: String, var secondWeek: String, var days: List<Day>, var lessonsTime: List<LessonTime>, var TableID: Int? = null) {
    fun getWeekName(date: Date, index: Int): String {
        return if(date.weekIndex() == 0) (if(index == 0) firstWeek else secondWeek) else (if(index == 0) secondWeek else firstWeek)
    }

}

val emptyTimeTable: TimeTableStructure
    get() = TimeTableStructure("","","", listOf(
        Day(),Day(),Day(),Day(),Day(),Day(),Day()
    ), listOf(
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0)
    ))

val mainDomain = "studrasp.ru"

class error(var code: Int, var message: String) {

}

class requestStruct(
    var error: error,
    var timetable: requestTable?,
    var session: String?,
    var login: String?,
    var email: String?,
    var timeTables: MutableList<globalTablesInfo>?,
    var id: String?,
    var invite_code: String?
    )

class requestTable(var id: Int, var json: TimeTableStructure?) {

}

class user(var login: String, var session: String) {

}