package com.zednull.studrasp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zednull.studrasp.components.LoadTimeTableView
import com.zednull.studrasp.structure.TimeTableStructure
import com.zednull.studrasp.ui.theme.TimeTableTheme
import com.google.gson.Gson
import com.zednull.studrasp.structure.Day
import com.zednull.studrasp.structure.LessonTime

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
                    ).toMutableStateList(), -1))
                }

                LaunchedEffect(key1 = loadedTable.value) {
                    if(loadedTable.value.TableID != -1) {
                        val data = Intent()
                        Log.i("check",Gson().toJson(loadedTable.value))
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