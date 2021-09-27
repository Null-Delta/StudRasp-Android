package com.zednull.studrasp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zednull.studrasp.minutes
import com.zednull.studrasp.structure.Lesson
import com.zednull.studrasp.structure.LessonTime
import com.zednull.studrasp.ui.theme.TimeTableTheme
import java.util.*

enum class CardState {
    Active, Wait, Highlight, Select
}

fun convertToTime(t: Int): String {
    val h = "${t / 60}"
    var m = "${t % 60}"
    if(m.length == 1) m = "0$m"
    return "$h:$m"
}

fun getTimeUntilStart(date: Date, lesson: LessonTime): String {
    val h = (lesson.start - date.minutes()) / 60
    val m = (lesson.start - date.minutes()) % 60
    return if(h == 0) "${m}м" else "${h}ч ${m}м"
}

fun getTimeUntilEnd(date: Date, lesson: LessonTime): String {
    val h = (lesson.end - date.minutes()) / 60
    val m = (lesson.end - date.minutes()) % 60
    return if(h == 0) "${m}м" else "${h}ч ${m}м"
}

@Composable
fun Card(date: Date, lesson: Lesson, state: CardState, time: LessonTime, onTap: () -> (Unit) = {}, onLongTap: () -> (Unit) = {}) {
    Column(
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        //if(state != CardState.Select)
                        onLongTap()
                    },
                    onTap = {
                        onTap()
                    }
                )
            }
    ) {
        Row {
            Box(
               modifier = Modifier
                   .width(24.dp)
                    .height(24.dp)
                    .background(
                        if (state == CardState.Highlight || state == CardState.Select) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
                    , CircleShape)
            ) {
                Text(
                    text = "${lesson.lessonNumber}" ,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 12.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (state == CardState.Highlight || state == CardState.Select) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.height(24.dp)
            ) {
                Text(
                    text = "${convertToTime(time.start)} - ${convertToTime(time.end)}",
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
                        CardState.Wait -> MaterialTheme.colors.primary
                        CardState.Select -> MaterialTheme.colors.onPrimary
                        else -> MaterialTheme.colors.secondary
                    },
                    MaterialTheme.shapes.medium)
        ) {
            if(state == CardState.Wait) {
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
                        text = getTimeUntilStart(date, time),
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
                        if (state == CardState.Active) 4.dp else 0.dp,
                        MaterialTheme.shapes.medium
                    )
                    .background(
                        if (state == CardState.Wait || state == CardState.Highlight)
                            MaterialTheme.colors.secondary
                        else if (state == CardState.Active)
                            MaterialTheme.colors.primary
                        else if (state == CardState.Select)
                            Color.Transparent
                        else MaterialTheme.colors.background,
                        MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
            ) {
                if(state == CardState.Select) {
                    Text(
                        text = "Добавить пару",
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    Row {
                        Text(
                            text = lesson.name,
                            color = if (state == CardState.Active) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                            fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        Text(
                            text = "${lesson.audience} / ${lesson.type}",
                            color = if (state == CardState.Active) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                            fontSize = 14.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, true)
                        )

                        Text(
                            text = lesson.teacherName,
                            color = if (state == CardState.Active) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                            fontSize = 14.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            if(state == CardState.Active) {
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
                        text = getTimeUntilEnd(date, time),
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
fun CardPreview() {
    TimeTableTheme {
        Card(date = Date(), lesson = Lesson("Дискретка","анимешка рандомная","дача моргена","гайд по геншину", lessonNumber = 0), state = CardState.Active, LessonTime(0,0))
    }
}