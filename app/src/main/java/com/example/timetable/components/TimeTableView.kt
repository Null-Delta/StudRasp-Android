package com.example.timetable.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
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
import com.example.timetable.LoadTimeTableActivity
import com.example.timetable.minutes
import com.example.timetable.structure.Day
import com.example.timetable.structure.Lesson
import com.example.timetable.structure.ServerTimeTable
import com.example.timetable.structure.TimeTableStructure
import com.example.timetable.ui.theme.TimeTableTheme
import com.example.timetable.weekDayNum
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun TimeTableView(date: Date, timeTable: MutableState<ServerTimeTable>, selectedDay: MutableState<Int>, paddingValues: PaddingValues) {
    val context = LocalContext.current

    //var scrolls = listOf<ScrollState>()
    val scrollState = rememberScrollState(0)

    val pagerState = rememberPagerState(
        pageCount = 7,
        selectedDay.value,0f,2,false
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

                timeTable.value = Gson().fromJson(result.data!!.getStringExtra("timetable"), ServerTimeTable::class.java)
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
                .padding(16.dp,0.dp,16.dp,16.dp),
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
                    text = if(timeTable.value.info.name == "") "Выбрать" else timeTable.value.info.name,
                    fontSize = 20.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))
        }

        if(timeTable.value.id != -1) {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
            ) { page ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState(), true, null, false)
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
                            text = timeTable.value.info.getWeekName(date, 0),
                            fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSecondary
                        )
                    }

                    if (timeTable.value.info.days[page].getLessons(Date(), 0).isEmpty()) {
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

                    repeat(timeTable.value.info.days[page].getLessons(Date(), 0).size) {
                        Card(
                            date = date,
                            lesson = timeTable.value.info.days[page].getLessons(Date(), 0)[it],
                            state =
                            if (date.minutes() >= timeTable.value.info.days[page].getLessons(
                                    Date(),
                                    0
                                )[it].start &&
                                date.minutes() <= timeTable.value.info.days[page].getLessons(
                                    Date(),
                                    0
                                )[it].end
                            )
                                CardState.active
                            else if (date.minutes() < timeTable.value.info.days[page].getLessons(
                                    Date(),
                                    0
                                )[it].start &&
                                (it == 0 || date.minutes() > timeTable.value.info.days[page].getLessons(
                                    Date(),
                                    0
                                )[it - 1].end) && (date.weekDayNum() - 1 == selectedDay.value)
                            )
                                CardState.wait else CardState.highlight
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
                            text = timeTable.value.info.getWeekName(date, 1),
                            fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSecondary
                        )
                    }

                    if (timeTable.value.info.days[page].getLessons(Date(), 1).isEmpty()) {
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

                    repeat(timeTable.value.info.days[page].getLessons(Date(), 1).size) {
                        Card(
                            date = date,
                            lesson = timeTable.value.info.days[page].getLessons(Date(), 1)[it],
                            state = CardState.highlight
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