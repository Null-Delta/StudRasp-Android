package com.zednull.timetable.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zednull.timetable.components.ui.MyTimeTableState
import com.zednull.timetable.minutes
import com.zednull.timetable.structure.TimeTableStructure
import com.zednull.timetable.weekDayNum
import com.zednull.timetable.weekIndex
import java.util.*
import kotlin.time.days


fun CardState(week: Int,index: Int, lesson: Int, table: TimeTableStructure, date: Date, isEditing: Boolean): CardState {
    if (isEditing && !isLesson(week, index, lesson, table, date)) {
        return CardState.select
    } else if(isEditing) {
        return CardState.highlight
    }

    return if(date.weekDayNum() - 1 != index) CardState.highlight
    else if(date.minutes() < table.lessonsTime[lesson - 1].start && (
                (table.days[index].getLessons(date, week).firstOrNull { it.lessonNumber < lesson } != null) || (date.minutes() > table.lessonsTime[
                        getLastLesson(index, lesson, table, date, week)
                ].end)
            )) CardState.wait
    else if(date.minutes() >= table.lessonsTime[lesson - 1].start && date.minutes() <= table.lessonsTime[lesson - 1].end) CardState.active
    else CardState.highlight

}

fun getLastLesson(index: Int, lesson: Int, table: TimeTableStructure, date: Date, week: Int): Int {
    return table.days[index].getLessons(date, week).maxOf {
        if (it.lessonNumber >= lesson) -1 else it.lessonNumber
    }
}

fun isLesson(week: Int, index: Int, lesson: Int, table: TimeTableStructure, date: Date): Boolean {
    return table.days[index].getLessons(date, if (week == date.weekIndex()) 0 else 1).firstOrNull {
        it.lessonNumber == lesson
    } != null
}

@Composable
fun WeekView(
    isWeekNow: Boolean,
    activeTable: MutableState<TimeTableStructure>,
    date: Date,
    isEditing: Boolean,
    dayIndex: Int) {
    Column() {
        Row() {
            Text(
                text = if (isWeekNow) "Текущая неделя" else "Следующая неделя",
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.fillMaxWidth().weight(1f))

            Text(
                text = activeTable.value.getWeekName(date, if(isWeekNow) 0 else 1),
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium
            )
        }

        if(isEditing) {

        } else {
            
        }
    }
}


@Composable
fun TimeTable() {

}