package com.zednull.studrasp.components

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.zednull.studrasp.LoadListOfPartsActivity
import com.zednull.studrasp.minutes
import com.zednull.studrasp.structure.Lesson
import com.zednull.studrasp.structure.TimeTableStructure
import com.zednull.studrasp.weekDayNum
import com.zednull.studrasp.weekIndex
import java.util.*


fun CardState(week: Int,index: Int, lesson: Int, table: TimeTableStructure, date: Date, isEditing: Boolean): CardState {
    if (isEditing && !isLesson(week, index, lesson, table, date)) {
        return CardState.Highlight
    } else if(isEditing) {
        return CardState.Highlight
    }

    //if(lesson - 1 >= 0)
        //Log.i("testa","${index} : ${lesson} ${getLastLesson(index, lesson, table, date, week)}")
    //if(getLastLesson(index, lesson, table, date, week) > 1)
        //Log.i("testa","${date.minutes()} , ${table.lessonsTime[lesson - 1].start} ${table.lessonsTime[getLastLesson(index, lesson, table, date, week) - 1].end}")
    return if(date.weekDayNum() - 1 != index) CardState.Highlight
    else if(date.minutes() < table.lessonsTime[lesson - 1].start && (
                (table.days[index].getLessons(date, week).firstOrNull { it.lessonNumber < lesson } == null) || (date.minutes() > table.lessonsTime[
                        getLastLesson(index, lesson, table, date, week) - 1
                ].end)
            )) CardState.Wait
    else if(date.minutes() >= table.lessonsTime[lesson - 1].start && date.minutes() <= table.lessonsTime[lesson - 1].end) CardState.Active
    else CardState.Highlight

}

fun getLastLesson(index: Int, lesson: Int, table: TimeTableStructure, date: Date, week: Int): Int {
    var max = -1;
    for (i in 0 until table.days[index].getLessons(date, week).size) {
        if(table.days[index].getLessons(date, week)[i].lessonNumber > max &&
            table.days[index].getLessons(date, week)[i].lessonNumber < lesson) {
            max = table.days[index].getLessons(date, week)[i].lessonNumber
        }
    }
    return max
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

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))

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


@ExperimentalMaterialApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TimeTable(
    activeTable: MutableState<TimeTableStructure>,
    date: Date,
    isEditing: Boolean,
    dayIndex: Int,
    pagerState: PagerState,
    sheetState: ModalBottomSheetState? = null,
    selectedLessonParent: MutableState<Int>? = null,
    selectedWeekParend: MutableState<Int>? = null
) {
    var isLongPress = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isLongPress.value) {
        if(isLongPress.value) {
            sheetState!!.show()
            isLongPress.value = false
        }
    }

    var selectedLesson = remember { mutableStateOf(0) }
    var selectedWeek = remember { mutableStateOf(0) }

    var context = LocalContext.current

    val addLessonRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null && result.data != null) {
            activeTable.value.days[pagerState.currentPage].changeLessons(date, selectedWeek.value) {
                Log.i("test", "changed")

                it.removeAll { l ->
                    l.lessonNumber == selectedLesson.value
                }

                it.add(Lesson(
                    result.data!!.extras!!.getString("name", ""),
                    result.data!!.extras!!.getString("teacherName", ""),
                    result.data!!.extras!!.getString("audience", ""),
                    result.data!!.extras!!.getString("type", ""),
                    selectedLesson.value
                ))
            }
        }
    }

    HorizontalPager(state = pagerState) { page ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Row(
                    modifier = Modifier.padding(16.dp,0.dp,16.dp,8.dp)
                ) {
                    Text(
                        text = "Текущая неделя",
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f))

                    Text(
                        text = activeTable.value.getWeekName(date,0),
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if(isEditing) {
                items(8) { it ->
                    Card(
                        date = date,
                        lesson = if (isLesson(
                                date.weekIndex(),
                                page,
                                it + 1,
                                activeTable.value,
                                date
                            )
                        )
                            activeTable.value.days[page].getLessons(date, 0)
                                .first { l -> l.lessonNumber == it + 1 }
                        else Lesson("", "", "", "", it + 1),
                        state = if (isLesson(
                                date.weekIndex(),
                                page,
                                it + 1,
                                activeTable.value,
                                date
                            )
                        ) CardState.Highlight else CardState.Select,
                        time = activeTable.value.lessonsTime[it],
                        onTap = {
                            selectedLesson.value = it + 1
                            selectedWeek.value = 0

                            if(activeTable.value.days[page].getLessons(date,0).firstOrNull { l ->
                                    l.lessonNumber == it + 1
                                } != null) {
                                val intent = Intent(context, LoadListOfPartsActivity::class.java)

                                intent.putExtra("name", activeTable.value.days[page].getLessons(date, 0)
                                    .first { l -> l.lessonNumber == it + 1 }.name)
                                intent.putExtra("teacherName", activeTable.value.days[page].getLessons(date, 0)
                                    .first { l -> l.lessonNumber == it + 1 }.teacherName)
                                intent.putExtra("audience", activeTable.value.days[page].getLessons(date, 0)
                                    .first { l -> l.lessonNumber == it + 1 }.audience)
                                intent.putExtra("type", activeTable.value.days[page].getLessons(date, 0)
                                    .first { l -> l.lessonNumber == it + 1 }.type)

                                addLessonRequest.launch(intent)
                            } else {
                                val intent = Intent(context, LoadListOfPartsActivity::class.java)

                                intent.putExtra("name", "")
                                intent.putExtra("teacherName", "")
                                intent.putExtra("audience", "")
                                intent.putExtra("type", "")

                                addLessonRequest.launch(intent)
                            }
                               // addLessonRequest.launch(Intent(context, LoadListOfPartsActivity::class.java))
                        },
                        onLongTap = {
                            if(activeTable.value.days[page].getLessons(date,0).firstOrNull { l ->
                                l.lessonNumber == it + 1
                                } != null) {
                                selectedLessonParent!!.value = it + 1
                                selectedWeekParend!!.value = 0
                                isLongPress.value = true
                            }
                        }
                    )
                }
            } else if(activeTable.value.days[page].getLessons(date,0).size != 0) {
                items(activeTable.value.days[page].getLessons(date, 0)) { lesson ->
                    Card(
                        date,
                        lesson,
                        CardState(
                            date.weekIndex(),
                            page,
                            lesson.lessonNumber,
                            activeTable.value,
                            date,
                            isEditing
                        ),
                        activeTable.value.lessonsTime[lesson.lessonNumber - 1]
                    )
                }
            }

            if(!isEditing && activeTable.value.days[page].getLessons(date,0).size == 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 0.dp)
                            .height(64.dp)
                            .background(
                                MaterialTheme.colors.onPrimary,
                                MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Сегодня пар нет",
                            color = MaterialTheme.colors.onSecondary,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.padding(16.dp,8.dp,16.dp,8.dp)
                ) {
                    Text(
                        text = "Следующая неделя",
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f))

                    Text(
                        text = activeTable.value.getWeekName(date,1),
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if(isEditing) {
                items(8) { it ->
                    Card(
                        date = date,
                        lesson = if (isLesson(
                                if(date.weekIndex() == 0) 1 else 0,
                                page,
                                it + 1,
                                activeTable.value,
                                date
                            )
                        )
                            activeTable.value.days[page].getLessons(date, 1)
                                .first { l -> l.lessonNumber == it + 1 }
                        else Lesson("", "", "", "", it + 1),
                        state = if (isLesson(
                                if(date.weekIndex() == 0) 1 else 0,
                                page,
                                it + 1,
                                activeTable.value,
                                date
                            )
                        ) CardState.Highlight else CardState.Select,
                        time = activeTable.value.lessonsTime[it],
                        onTap = {
                            selectedLesson.value = it + 1
                            selectedWeek.value = 1
                            if(activeTable.value.days[page].getLessons(date,1).firstOrNull { l ->
                                    l.lessonNumber == it + 1
                                } != null) {
                                val intent = Intent(context, LoadListOfPartsActivity::class.java)

                                intent.putExtra("name", activeTable.value.days[page].getLessons(date, 1)
                                    .first { l -> l.lessonNumber == it + 1 }.name)
                                intent.putExtra("teacherName", activeTable.value.days[page].getLessons(date, 1)
                                    .first { l -> l.lessonNumber == it + 1 }.teacherName)
                                intent.putExtra("audience", activeTable.value.days[page].getLessons(date, 1)
                                    .first { l -> l.lessonNumber == it + 1 }.audience)
                                intent.putExtra("type", activeTable.value.days[page].getLessons(date, 1)
                                    .first { l -> l.lessonNumber == it + 1 }.type)

                                addLessonRequest.launch(intent)
                            } else {
                                val intent = Intent(context, LoadListOfPartsActivity::class.java)

                                intent.putExtra("name", "")
                                intent.putExtra("teacherName", "")
                                intent.putExtra("audience", "")
                                intent.putExtra("type", "")

                                addLessonRequest.launch(intent)
                            }
                        },
                        onLongTap = {
                            if(activeTable.value.days[page].getLessons(date,1).firstOrNull { l ->
                                    l.lessonNumber == it + 1
                                } != null) {
                                selectedLessonParent!!.value = it + 1
                                selectedWeekParend!!.value = 1
                                isLongPress.value = true
                            }
                        }
                    )
                }
            } else {
                items(activeTable.value.days[page].getLessons(date, 1)) { lesson ->
                    Card(
                        date,
                        lesson,
                        CardState.Highlight,
                        activeTable.value.lessonsTime[lesson.lessonNumber - 1]
                    )
                }
            }

            if(!isEditing && activeTable.value.days[page].getLessons(date,1).size == 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 0.dp)
                            .height(64.dp)
                            .background(
                                MaterialTheme.colors.onPrimary,
                                MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Сегодня пар нет",
                            color = MaterialTheme.colors.onSecondary,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(32.dp)
                )
            }
        }
    }
}
