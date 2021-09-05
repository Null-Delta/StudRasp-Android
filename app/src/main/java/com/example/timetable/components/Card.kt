package com.example.timetable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetable.minutes
import com.example.timetable.structure.Lesson
import com.example.timetable.ui.theme.TimeTableTheme
import java.util.*

enum class CardState {
    active, wait, highlight
}

fun convertToTime(t: Int): String {
    var h = "${t / 60}"
    var m = "${t % 60}"
    if(m.length == 1) m = "0" + m
    return h + ":" + m
}

fun getTimeUntilStart(date: Date, lesson: Lesson): String {
    var h = (lesson.start - date.minutes()) / 60
    var m = (lesson.start - date.minutes()) % 60
    return if(h == 0) "${m}м" else "${h}ч ${m}м"
}

fun getTimeUntilEnd(date: Date, lesson: Lesson): String {
    var h = (lesson.end - date.minutes()) / 60
    var m = (lesson.end - date.minutes()) % 60
    return if(h == 0) "${m}м" else "${h}ч ${m}м"
}

@Composable
fun Card(date: Date, lesson: Lesson, state: CardState) {
    Column() {
        Row() {
            Box(
               modifier = Modifier
                   .width(24.dp)
                    .height(24.dp)
                    .background(
                        if (state == CardState.highlight) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
                    , CircleShape)
            ) {
                Text(
                    text = "${if (lesson.index== null) 0 else lesson.index!!}" ,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 12.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (state == CardState.highlight) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.height(24.dp)
            ) {
                Text(
                    text = "${convertToTime(lesson.start)} - ${convertToTime(lesson.end)}",
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .background(
                    when (state) {
                        CardState.wait -> MaterialTheme.colors.primary
                        else -> MaterialTheme.colors.secondary
                    },
                    MaterialTheme.shapes.medium)
        ) {
            if(state == CardState.wait) {
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "До начала пары",
                        color = MaterialTheme.colors.secondary,
                        fontSize = 14.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true))
                    Text(
                        text = getTimeUntilStart(date, lesson),
                        color = MaterialTheme.colors.secondary,
                        fontSize = 14.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .shadow(
                        if (state == CardState.active) 4.dp else 0.dp,
                        MaterialTheme.shapes.medium
                    )
                    .background(
                        if (state == CardState.wait || state == CardState.highlight)
                            MaterialTheme.colors.secondary
                        else if (state == CardState.active)
                            MaterialTheme.colors.primary
                        else MaterialTheme.colors.background,
                        MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
            ) {
                Row() {
                    Text(
                        text = lesson.name,
                        color = if (state == CardState.active) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row() {
                    Text(
                        text = "${lesson.audience} / ${lesson.type}",
                        color = if (state == CardState.active) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true))

                    Text(
                        text = lesson.teacherName,
                        color = if (state == CardState.active) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if(state == CardState.active) {
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "До конца пары",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true))
                    Text(
                        text = getTimeUntilEnd(date, lesson),
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}


@Preview(showBackground = false)
@Composable
fun preview() {
    TimeTableTheme() {
        Card(date = Date(), lesson = Lesson("Дискретка","Жук А.С.","А205","Лк",0,0, index = null), state = CardState.active)
    }
}