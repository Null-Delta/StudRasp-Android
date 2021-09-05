package com.example.timetable

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.timetable.components.LoadTimeTableView
import com.example.timetable.structure.ServerTimeTable
import com.example.timetable.structure.TimeTableStructure
import com.example.timetable.ui.theme.TimeTableTheme
import com.google.gson.Gson

class LoadTimeTableActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTableTheme {
                var loadedTable = remember {
                    mutableStateOf(ServerTimeTable(-1, TimeTableStructure("","","", listOf())))
                }

                LaunchedEffect(key1 = loadedTable.value) {
                    if(loadedTable.value.id != -1) {
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