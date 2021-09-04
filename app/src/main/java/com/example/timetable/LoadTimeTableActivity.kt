package com.example.timetable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.timetable.components.LoadTimeTableView
import com.example.timetable.ui.theme.TimeTableTheme

class LoadTimeTableActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTableTheme {
                LoadTimeTableView()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadTimeTableActivityPreview() {
    TimeTableTheme {
        LoadTimeTableView()
    }
}