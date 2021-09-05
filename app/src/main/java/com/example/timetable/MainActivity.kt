package com.example.timetable

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.timetable.components.MainMenu
import com.example.timetable.ui.theme.TimeTableTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import java.util.*

@InternalCoroutinesApi
@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    var date = mutableStateOf(Date())
    var selectedDay = mutableStateOf(date.value.weekDayNum() - 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TimeTableTheme {
                LaunchedEffect(key1 = date.value) {
                    delay(20 * 1000)
                    date.value = Date()
                }
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainMenu(date.value, selectedDay)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        date.value = Date()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimeTableTheme {
        Greeting("Android")
    }
}