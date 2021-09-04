package com.example.timetable.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetable.LoadTimeTableActivity
import com.example.timetable.minutes
import com.example.timetable.structure.Day
import com.example.timetable.structure.Lesson
import com.example.timetable.structure.TimeTableStructure
import com.example.timetable.weekDayNum
import java.util.*


val timeTableStructure = TimeTableStructure("26/2", "Чс", "Зн",
    listOf(
        Day(
            listOf(
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 0, 90),
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 100, 190),
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 200, 290),
            ),
            listOf(
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 0, 90),
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 100, 190),
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 200, 290),
            )
        ),
        Day(),
        Day(),
        Day(),
        Day(),
        Day(
            listOf(
            Lesson("Дискретка", "Жук А.С.", "А305","Лк", 0, 90),
            Lesson("Дискретка", "Жук А.С.", "А305","Лк", 100, 190),
            Lesson("Дискретка", "Жук А.С.", "А305","Лк", 200, 290),
            ),
            listOf(
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 230, 420),
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 430, 520),
                Lesson("Дискретка", "Жук А.С.", "А305","Лк", 530, 620),
            )
        ),
        Day()
    )
)

@Composable
fun TimeTableView(date: Date, timeTable: TimeTableStructure, selectedDay: Int) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp,0.dp,0.dp,16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Расписание",
                fontSize = 24.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.wrapContentHeight()
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))
            TextButton(
                onClick = {
                    context.startActivity(Intent(context, LoadTimeTableActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary
                ),
            ) {
                Text(
                    text = if(timeTable.name == "") "Выбрать" else timeTable.name,
                    fontSize = 20.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(0), true, null, false)
                .padding(0.dp, 0.dp, 0.dp, 114.dp)
        ) {
            Row(
                modifier = Modifier.padding(0.dp,0.dp,0.dp,8.dp)
            ) {
                Text(
                    text = "Текущая неделя",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth(1f)
                    .weight(1f, true))

                Text(
                    text = timeTable.getWeekName(date, 0),
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary
                )
            }

            if(timeTable.days[selectedDay].getLessons(Date(), 0).isEmpty()) {
                Text(
                    text ="Сегодня пар нет",
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSecondary,
                    textAlign = TextAlign.Center
                )
            }

            repeat(timeTable.days[selectedDay].getLessons(Date(),0).size) {
                Card(date = date, lesson = timeTable.days[selectedDay].getLessons(Date(),0)[it],
                    state =
                        if(date.weekDayNum() - 1 != selectedDay)
                            CardState.disable
                        else if(date.minutes() > timeTable.days[selectedDay].getLessons(Date(),0)[it].end)
                            CardState.disable
                        else if(date.minutes() >= timeTable.days[selectedDay].getLessons(Date(),0)[it].start &&
                            date.minutes() <= timeTable.days[selectedDay].getLessons(Date(),0)[it].end)
                              CardState.active
                        else if(date.minutes() < timeTable.days[selectedDay].getLessons(Date(),0)[it].start &&
                            (it ==  0 || date.minutes() > timeTable.days[selectedDay].getLessons(Date(),0)[it - 1].end))
                                CardState.wait else CardState.highlight
                )
                if(it != 15) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Row(
                modifier = Modifier.padding(0.dp,16.dp,0.dp,8.dp)
            ) {
                Text(
                    text = "Следующая неделя",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth(1f)
                    .weight(1f, true))

                Text(
                    text = timeTable.getWeekName(date, 1),
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary
                )
            }

            if(timeTable.days[selectedDay].getLessons(Date(), 1).isEmpty()) {
                Text(
                    text ="Сегодня пар нет",
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSecondary,
                    textAlign = TextAlign.Center
                )
            }

            repeat(timeTable.days[selectedDay].getLessons(Date(),1).size) {
                Card(date = date, lesson = timeTable.days[selectedDay].getLessons(Date(),1)[it], state = CardState.disable)
                if(it != 15) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview2() {

}