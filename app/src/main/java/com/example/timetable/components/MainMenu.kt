package com.example.timetable.components

import android.os.Debug
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timetable.*
import com.example.timetable.structure.Day
import com.example.timetable.structure.TimeTableStructure
import com.example.timetable.ui.theme.TimeTableTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun MainMenu(date: Date, selectedDay: MutableState<Int>) {
    var systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    var barColor = MaterialTheme.colors.background

    SideEffect {
        systemController.setNavigationBarColor(
            barColor,useDarkIcons
        )
        systemController.setStatusBarColor(
            barColor,useDarkIcons
        )
    }

    Box {
        TimeTableView(date, timeTableStructure, selectedDay.value)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier
                .fillMaxSize()
                .weight(1f, true)
                .background(Color.Transparent))
            WeekView(date,  selectedDay)
            Divider(
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
            )
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.background
            ) {

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    var state = remember { mutableStateOf(2) }
    TimeTableTheme {
        MainMenu(Date(), state)
    }
}