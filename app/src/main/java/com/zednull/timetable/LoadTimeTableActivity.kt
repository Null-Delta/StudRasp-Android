package com.zednull.timetable

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zednull.timetable.components.LoadTimeTableView
import com.zednull.timetable.structure.TimeTableStructure
import com.zednull.timetable.ui.theme.TimeTableTheme
import com.google.gson.Gson
import com.zednull.timetable.structure.Day
import com.zednull.timetable.structure.Lesson
import com.zednull.timetable.structure.LessonTime

class LoadTimeTableActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTableTheme {
                var loadedTable = remember {
                    mutableStateOf( TimeTableStructure("","","",
                        listOf<Day>().toMutableStateList(),
                        listOf(
                        LessonTime(0,0),
                        LessonTime(0,0),
                        LessonTime(0,0),
                        LessonTime(0,0),
                        LessonTime(0,0),
                        LessonTime(0,0),
                        LessonTime(0,0)
                    ).toMutableStateList()))
                }

                LaunchedEffect(key1 = loadedTable.value) {
                    if(loadedTable.value.TableID != null) {
                        val data = Intent()
                        data.putExtra("timetable",Gson().toJson(loadedTable.value))
                        setResult(1, data)
                        finish()
                    }
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    LoadTimeTableView(loadedTable)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadTimeTableActivityPreview() {
    TimeTableTheme {
       // LoadTimeTableView()
    }
}