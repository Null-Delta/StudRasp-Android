package com.zednull.studrasp.structure

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.zednull.studrasp.components.globalTablesInfo
import com.zednull.studrasp.weekIndex
import java.util.*


class Lesson(var name: String, var teacherName: String, var audience: String, var type: String, var lessonNumber: Int) {
    fun isEqual(l: Lesson): Boolean =
        name == l.name && teacherName == l.teacherName && audience == l.audience && type == l.type && lessonNumber == l.lessonNumber
}

class Day(var lessons1: SnapshotStateList<Lesson> = listOf<Lesson>().toMutableStateList(),var lessons2: SnapshotStateList<Lesson> = listOf<Lesson>().toMutableStateList()) {
    fun getLessons(date: Date, index: Int): SnapshotStateList<Lesson> {
        return if(date.weekIndex() == 0) ( if(index == 0) lessons1 else lessons2) else ( if(index == 0) lessons2 else lessons1)
    }

    fun changeLessons(date: Date, index: Int, action: (SnapshotStateList<Lesson>) -> Unit) {
        if(date.weekIndex() != index) {
            action(lessons2)
        } else {
            action(lessons1)
        }
    }

    fun isEqual(d: Day): Boolean {
        if(lessons1.size - d.lessons1.size != 0 || lessons2.size - d.lessons2.size != 0) return false

        if(lessons1.size > 0)
        for(i in 0 until lessons1.size) {
            if(!lessons1[i].isEqual(d.lessons1[i])) {
                return false
            }
        }

        if(lessons2.size > 0)
        for(i in 0 until lessons2.size) {
            if(!lessons2[i].isEqual(d.lessons2[i])) {
                return false
            }
        }

        return true
    }
}

class LessonTime(var start: Int, var end: Int) {
    fun isEqual(t: LessonTime): Boolean {
        return start == t.start && end == t.end
    }
}

class TimeTableStructure(
    var name: String,
    var firstWeek: String,
    var secondWeek: String,
    var days: SnapshotStateList<Day>,
    var lessonsTime: SnapshotStateList<LessonTime>,
    var TableID: Int,
    var invite_code: String? = null) {
    fun getWeekName(date: Date, index: Int): String {
        return if(date.weekIndex() == 0) (if(index == 0) firstWeek else secondWeek) else (if(index == 0) secondWeek else firstWeek)
    }

    fun isEqual(with: TimeTableStructure): Boolean {
        return name == with.name &&
                firstWeek == with.firstWeek &&
                secondWeek == with.secondWeek &&
                TableID == with.TableID &&
                isEqualDays(with.days) &&
                isEqualTimes(with.lessonsTime)
    }

    fun isEqualDays(ds: SnapshotStateList<Day>): Boolean {
        for(i in 0 until 7) {
            if(!days[i].isEqual(ds[i])) {
                return false
            }
        }
        return true
    }

    fun isEqualTimes(times: SnapshotStateList<LessonTime>): Boolean {
        for(i in 0 until 8) {
            if(!lessonsTime[i].isEqual(times[i])) {
                return false
            }
        }
        return true
    }

}

val emptyTimeTable: TimeTableStructure
    get() = TimeTableStructure("","","", listOf(
        Day(),Day(),Day(),Day(),Day(),Day(),Day()
    ).toMutableStateList(), listOf(
        LessonTime(480,570),
        LessonTime(580,670),
        LessonTime(690,780),
        LessonTime(790,880),
        LessonTime(900,990),
        LessonTime(1000,1090),
        LessonTime(1100,1190),
        LessonTime(1200,1290)
    ).toMutableStateList(), -1)

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

class user(var login: String, var session: String, var email: String) {

}