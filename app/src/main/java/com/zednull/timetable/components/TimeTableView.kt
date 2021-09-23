package com.zednull.timetable.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zednull.timetable.LoadTimeTableActivity
import com.zednull.timetable.minutes
import com.zednull.timetable.ui.theme.TimeTableTheme
import com.zednull.timetable.weekDayNum
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.zednull.timetable.structure.TimeTableStructure
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun TimeTableView(date: Date, timeTable: MutableState<TimeTableStructure>, selectedDay: MutableState<Int>, paddingValues: PaddingValues) {
    val context = LocalContext.current

    val pagerState = rememberPagerState(
        pageCount = 7,
        selectedDay.value,0f,7,false
    )

    val loadRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null) {
            if(result.resultCode == 1) {
                context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                editor.putString("timetable", result.data!!.getStringExtra("timetable"))
                editor.apply()


                timeTable.value = Gson().fromJson(result.data!!.getStringExtra("timetable"), TimeTableStructure::class.java)
                Log.i("test", (timeTable.value.lessonsTime).toString())
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
            selectedDay.value = pagerState.currentPage
    }

    LaunchedEffect(selectedDay.value) {
        pagerState.animateScrollToPage(selectedDay.value)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp,16.dp,16.dp,16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Расписание",
                fontSize = 32.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.wrapContentHeight()
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))
        }

        Row(
            modifier = Modifier
                .padding(16.dp,0.dp,16.dp,16.dp),
        ) {
            TextButton(
                onClick = {
                    Log.i("check","here");
                    loadRequest.launch(Intent(context, LoadTimeTableActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary,
                    disabledBackgroundColor = MaterialTheme.colors.background,
                    disabledContentColor = MaterialTheme.colors.secondary
                ),
            ) {
                Text(
                    text = if(timeTable.value.name == "") "Выбрать" else timeTable.value.name,
                    fontSize = 20.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))
        }

        if(timeTable.value.name != "") {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
            ) { page ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState(), true, null, false)
                        .fillMaxSize()
                        .padding(16.dp, 0.dp, 16.dp, 76.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
                    ) {
                        Text(
                            text = "Текущая неделя",
                            fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSecondary
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .weight(1f, true)
                        )

                        Text(
                            text = timeTable.value.getWeekName(date, 0),
                            fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSecondary
                        )
                    }

                    if (timeTable.value.days[page].getLessons(Date(), 0).isEmpty()) {
                        Text(
                            text = "Сегодня пар нет",
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

                    repeat(timeTable.value.days[page].getLessons(Date(), 0).size) {
                        Card(
                            date = date,
                            lesson = timeTable.value.days[page].getLessons(Date(), 0)[it],
                            state = cardState(date, timeTable, page, it),
                            time = timeTable.value.lessonsTime[timeTable.value.days[page].getLessons(Date(), 0)[it].lessonNumber - 1]
                        )
                        if (it != 15) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Row(
                        modifier = Modifier.padding(0.dp, 24.dp, 0.dp, 16.dp)
                    ) {
                        Text(
                            text = "Следующая неделя",
                            fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSecondary
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .weight(1f, true)
                        )

                        Text(
                            text = timeTable.value.getWeekName(date, 1),
                            fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSecondary
                        )
                    }

                    if (timeTable.value.days[page].getLessons(Date(), 1).isEmpty()) {
                        Text(
                            text = "Сегодня пар нет",
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

                    repeat(timeTable.value.days[page].getLessons(Date(), 1).size) {
                        Card(
                            date = date,
                            lesson = timeTable.value.days[page].getLessons(Date(), 1)[it],
                            state = CardState.highlight,
                            time = timeTable.value.lessonsTime[timeTable.value.days[page].getLessons(Date(),1)[it].lessonNumber - 1]
                        )
                        if (it != 15) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}


fun cardState(date: Date, timeTable: MutableState<TimeTableStructure>, page: Int, lesson: Int): CardState {
    return if(date.weekDayNum() - 1 != page) CardState.highlight
    else if(date.minutes() < timeTable.value.lessonsTime[timeTable.value.days[page].getLessons(Date(),0)[lesson].lessonNumber].start &&
        (lesson == 0 || date.minutes() > timeTable.value.lessonsTime[timeTable.value.days[page].getLessons(Date(),0)[lesson - 1].lessonNumber].end)) CardState.wait
    else if (date.minutes() >= timeTable.value.lessonsTime[timeTable.value.days[page].getLessons(Date(),0)[lesson].lessonNumber].start &&
        date.minutes() <= timeTable.value.lessonsTime[timeTable.value.days[page].getLessons(Date(),0)[lesson].lessonNumber].end) CardState.active
    else CardState.highlight
}

@SuppressLint("UnrememberedMutableState")
@ExperimentalPagerApi
@InternalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun preview2() {
    TimeTableTheme() {
        //TimeTableView(date = Date(), timeTable = timeTableStructure, selectedDay = mutableStateOf(0) )
    }
}