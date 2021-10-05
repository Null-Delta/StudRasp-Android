package com.zednull.studrasp.components

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kittinunf.fuel.Fuel
import com.zednull.studrasp.LoadTimeTableActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.zednull.studrasp.structure.TimeTableStructure
import com.zednull.studrasp.structure.mainDomain
import com.zednull.studrasp.structure.requestStruct
import com.zednull.studrasp.weekIndex
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@ExperimentalMaterialApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun TimeTableView(date: Date, timeTable: MutableState<TimeTableStructure>, selectedDay: PagerState, paddingValues: PaddingValues) {
    val context = LocalContext.current

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
            }
        }
    }

    val ticker = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = ticker.value) {
        if(Date().time - context.getSharedPreferences("preferences",Context.MODE_PRIVATE)!!.getLong("update_time",0) > 1000 * 60 * 10) {
            Fuel.post("https://$mainDomain/main.php", listOf("action" to "get_timetable", "id" to timeTable.value.TableID.toString()))
                .responseString { _, _, result ->
                    val request: requestStruct = Gson().fromJson(result.get(),
                        requestStruct::class.java)
                    if(request.error.code == 0) {
                        timeTable.value = request.timetable!!.json!!
                        timeTable.value.TableID = request.timetable!!.id
                        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                        editor.putString("timetable", Gson().toJson(timeTable.value))
                        editor.apply()
                    }
                }

            val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
            editor.putLong("update_time", Date().time)
            editor.apply()
        }
    }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp,16.dp,16.dp,16.dp)
                    .height(48.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = if ( timeTable.value.name != "")
                            timeTable.value.name
                        else
                            "Выбрать",
                        fontSize = 24.sp,
                        color = MaterialTheme.colors.primary,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    loadRequest.launch(Intent(context, LoadTimeTableActivity::class.java))
                                }
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true))

                if(timeTable.value.name != "") {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "Обновить",
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primary,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        Fuel.post("https://$mainDomain/main.php", listOf("action" to "get_timetable", "id" to timeTable.value.TableID.toString()))
                                            .responseString { _, _, result ->
                                                val request: requestStruct = Gson().fromJson(result.get(),
                                                    requestStruct::class.java)
                                                if(request.error.code == 0) {
                                                    timeTable.value = request.timetable!!.json!!
                                                    timeTable.value.TableID = request.timetable!!.id
                                                    context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                                                    val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                                                    editor.putString("timetable", Gson().toJson(timeTable.value))
                                                    editor.apply()
                                                }
                                            }
                                    }
                                )
                            }
                        )
                    }
                }
            }

            //if(timeTable.value.name != "") {
            TimeTable(timeTable, date, false, date.weekIndex(), selectedDay, null)
            //}
    }
}