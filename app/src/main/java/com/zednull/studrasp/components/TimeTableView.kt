package com.zednull.studrasp.components

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kittinunf.fuel.Fuel
import com.zednull.studrasp.LoadTimeTableActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zednull.studrasp.R
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
fun TimeTableView(
    date: Date,
    timeTable: MutableState<TimeTableStructure>,
    selectedDay: PagerState,
    paddingValues: PaddingValues,
    sharedTable: MutableState<TimeTableStructure>) {
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
    var expan = remember { mutableStateOf(false) }
    val clipboard = LocalClipboardManager.current

    LaunchedEffect(key1 = ticker.value) {
        if(Date().time - context.getSharedPreferences("preferences",Context.MODE_PRIVATE)!!.getLong("update_time",0) > 1000 * 60 * 10) {
            Fuel.post("https://$mainDomain/main.php", listOf("action" to "get_timetable", "id" to timeTable.value.TableID.toString()))
                .responseString { _, _, result ->
                    val request: requestStruct = Gson().fromJson(result.get(),
                        requestStruct::class.java)
                    if(request.error.code == 0) {
                        request.timetable!!.json!!.invite_code = timeTable.value.invite_code!!

                        timeTable.value = request.timetable!!.json!!
                        timeTable.value.invite_code = request.timetable!!.json!!.invite_code
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
                    .padding(16.dp, 16.dp, 16.dp, 16.dp)
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
                        IconButton(
                            onClick = {
                                expan.value = true
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .width(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_more_horiz_24),
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }


                        DropdownMenu(
                            modifier = Modifier.background(
                                MaterialTheme.colors.background,
                                MaterialTheme.shapes.medium
                            ),
                            expanded = expan.value,
                            onDismissRequest = { expan.value = false }
                        ) {
                            if(timeTable.value.TableID >= 0) {
                                DropdownMenuItem(onClick = {
                                    Fuel.post("https://$mainDomain/main.php", listOf("action" to "get_timetable", "id" to timeTable.value.TableID.toString()))
                                        .responseString { _, _, result ->
                                            val request: requestStruct = Gson().fromJson(result.get(),
                                                requestStruct::class.java)
                                            if(request.error.code == 0) {
                                                request.timetable!!.json!!.invite_code = timeTable.value.invite_code!!

                                                timeTable.value = request.timetable!!.json!!
                                                timeTable.value.TableID = request.timetable!!.id

                                                context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                                                val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                                                editor.putString("timetable", Gson().toJson(timeTable.value))
                                                editor.apply()
                                            }

                                            expan.value = false
                                        }
                                }) {
                                    Text(
                                        text = "Обновить",
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 16.sp,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                    )
                                }

                                DropdownMenuItem(onClick = {
                                    clipboard.setText(AnnotatedString(timeTable.value.invite_code!!))
                                    expan.value = false
                                }) {
                                    Text(
                                        text = "Копировать код (${timeTable.value.invite_code!!})",
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 16.sp,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                    )
                                }

                                DropdownMenuItem(onClick = {
                                    var pinnedTables: SnapshotStateList<PinnedTable> =
                                        Gson().fromJson(
                                            context.getSharedPreferences(
                                                "preferences",
                                                Context.MODE_PRIVATE
                                            ).getString(
                                                "pinnedTables",
                                                Gson().toJson(List(0) { PinnedTable("","") })
                                            ), object : TypeToken<SnapshotStateList<PinnedTable>>() { }.type
                                        )

                                    if(pinnedTables.firstOrNull { v ->
                                            v.name == timeTable.value.name &&
                                                    v.invite_code == timeTable.value.invite_code!!.lowercase()
                                        } == null) {
                                        pinnedTables.add(
                                            PinnedTable(
                                                timeTable.value.name,
                                                timeTable.value.invite_code!!.lowercase()
                                            )
                                        )
                                    }

                                    var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                                    editor.putString("pinnedTables", Gson().toJson(pinnedTables))
                                    editor.apply()

                                    expan.value = false
                                }) {
                                    Text(
                                        text = "Добавить в избранные",
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 16.sp,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            } else {
                                DropdownMenuItem(onClick = {
                                    sharedTable.value = timeTable.value
                                    expan.value = false
                                }) {
                                    Text(
                                        text = "Поделиться",
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 16.sp,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            //if(timeTable.value.name != "") {
            TimeTable(timeTable, date, false, date.weekIndex(), selectedDay, null)
            //}
    }
}