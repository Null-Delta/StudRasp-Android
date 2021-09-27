package com.zednull.studrasp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kittinunf.fuel.Fuel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.studrasp.components.TimeTable
import com.zednull.studrasp.components.WeekView
import com.zednull.studrasp.structure.emptyTimeTable
import com.zednull.studrasp.structure.mainDomain
import com.zednull.studrasp.structure.requestStruct
import com.zednull.studrasp.ui.theme.TimeTableTheme
import java.util.*

class AddTableActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AddTableView(intent.getStringExtra("code")!!, this)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddTableView(code: String,act: Activity) {
    var isLoad = remember { mutableStateOf(false) }
    var loadedTable = remember { mutableStateOf(emptyTimeTable) }
    var selectedDay = remember { mutableStateOf(0)}

    val systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val barColor = MaterialTheme.colors.background

    SideEffect {
        systemController.setNavigationBarColor(
            barColor,useDarkIcons
        )
        systemController.setStatusBarColor(
            barColor,useDarkIcons
        )
    }

    LaunchedEffect(key1 = isLoad.value) {
        if(!isLoad.value) {
            Fuel.post("https://${mainDomain}/main.php",
            listOf(
                "action" to "get_timetable_by_invite_code",
                "invite_code" to code
            ))
            .responseString{_,_, result ->
                var request = Gson().fromJson(result.get(), requestStruct::class.java)

                if(request.error.code == 0) {
                    loadedTable.value = request.timetable!!.json!!
                    loadedTable.value.TableID = request.timetable!!.id
                    isLoad.value = true
                }
            }
        }
    }

    Column {
        if(!isLoad.value) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Загрузка...",
                    color = MaterialTheme.colors.onSecondary,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp, 0.dp,16.dp,8.dp)
                )
            }
        } else {
            Box {
                Column {
                    Row(
                        Modifier.padding(16.dp)
                    ) {
                        TextButton(onClick = {
                            act.setResult(-1)
                            act.finish()
                        },
                        ) {
                            Text(
                                text = "Назад",
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary
                            )
                        }

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, true))

                        TextButton(onClick = {
                            val data = Intent()
                            data.putExtra("timetable",Gson().toJson(loadedTable.value))
                            act.setResult(1, data)
                            act.finish()
                        },
                        ) {
                            Text(
                                text = "Использовать",
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Text(
                        text = loadedTable.value.name,
                        color = MaterialTheme.colors.primary,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp, 0.dp,16.dp,8.dp)
                    )

                    TimeTable(
                        activeTable = loadedTable,
                        date = Date(),
                        isEditing = false,
                        dayIndex = Date().weekIndex(),
                        selectedDay = selectedDay,
                    )
                }

                Column {
                    Spacer(modifier = Modifier.fillMaxHeight().weight(1f,true))
                    WeekView(date = Date(), selected = selectedDay)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview5() {
    TimeTableTheme {
        //Greeting2("Android")
    }
}