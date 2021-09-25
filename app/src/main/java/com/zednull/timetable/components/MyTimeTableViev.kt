package com.zednull.timetable.components.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.text.format.Time
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import java.util.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.github.kittinunf.fuel.Fuel
import com.google.gson.reflect.TypeToken
import com.zednull.timetable.LoadListOfPartsActivity
import com.zednull.timetable.LoadTimeTableActivity
import com.zednull.timetable.R
import com.zednull.timetable.components.CardState
import com.zednull.timetable.structure.*
import java.util.stream.Collectors.toList


enum class MyTimeTableState {
    global, local, changed
}

class SavedTables(
    var globalTables: SnapshotStateList<globalTablesInfo>,
    var localTables: SnapshotStateList<TimeTableStructure>,
    var globalSavedTables: SnapshotStateList<savedTimeTableInfo>,
    var selectedType: MyTimeTableState = MyTimeTableState.local,
    var selectedTable: Int = 0,
    var selectedID: Int = -1
) {
    fun isLoad(id: String): Boolean {
        return globalSavedTables.firstOrNull {
            it.id.toString() == id
        } != null
    }

    fun selectedTable(): TimeTableStructure {
        if(selectedType == MyTimeTableState.local) {
            return localTables[selectedTable]
        } else {
            return globalSavedTables.first {
                it.id == selectedID
            }.table
        }
    }

    fun modificateSelectedTable(action: (TimeTableStructure) -> (Unit)) {
        action(selectedTable())
    }

    fun saveArray(state: MyTimeTableState, context: Context) {
        if(state == MyTimeTableState.local) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
            editor.putString("localTables", Gson().toJson(localTables))
            editor.apply()
        } else {
            var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
            editor.putString("globalTables", Gson().toJson(globalTables))
            editor.apply()

            saveSavedTables(context = context)
        }
    }

    fun clearSaved(context: Context) {
        globalSavedTables.dropWhile {
            globalTables.firstOrNull { tbl ->
                tbl.id == it.id.toString()
            } != null
        }
        saveSavedTables(context = context)
    }

    fun saveSavedTables(context: Context) {
        var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
        editor.putString("globalSaved", Gson().toJson(globalSavedTables))
        editor.apply()
    }
}

class globalTablesInfo(var name: String, var id: String, var invite_code: String?)

class savedTimeTableInfo(var id: Int, var loaded: TimeTableStructure, var table: TimeTableStructure) {
    fun isChange(): Boolean {
        return !loaded.isEqual(table)
    }
}

@Composable
fun DrawTable(
    state: MyTimeTableState,
    name: String,
    code: String,
    onShare: () -> (Unit) = {},
    onDelete: () -> (Unit) = {},
    onTap: () -> (Unit) = {},
    onChangesDelete: () -> (Unit) = {})
{
    val isPressed = remember { mutableStateOf(false) }
    var expan = remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 9.dp)
            .fillMaxWidth()
            .height(36.dp)
            .background(
                if (state == MyTimeTableState.global || state == MyTimeTableState.local)
                    MaterialTheme.colors.secondary
                else
                    MaterialTheme.colors.secondaryVariant,
                MaterialTheme.shapes.medium
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onTap()
                    }
                )
            }
    )
    {

            Text(

                text = name,
                fontSize = 16.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                color = if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(8.dp, 0.dp, 8.dp, 0.dp)
                    .align(Alignment.CenterVertically)
            )

        Text(

            text = code,
            fontSize = 16.sp,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            color =  if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onError,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true)
        )

        if (state == MyTimeTableState.changed)
        {
            IconButton(
                onClick = {
                          onShare()
                },
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .background(Color.Transparent, MaterialTheme.shapes.medium)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.upload),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
        }


            Box(modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),) {

                IconButton(
                    onClick = { expan.value = true },
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp)
                        .background(Color.Transparent, MaterialTheme.shapes.medium)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_more_horiz_24),
                        contentDescription = null,
                        tint = if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.primary
                        else MaterialTheme.colors.primaryVariant
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
                    if (state == MyTimeTableState.local)
                        DropdownMenuItem(onClick = {
                            onShare()
                            expan.value = false
                        }) {
                            Text(
                                text = "Опубликовать",
                                color = MaterialTheme.colors.primary,
                                fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                )
                    }

                    if(state == MyTimeTableState.global) {
                        DropdownMenuItem(onClick = {
                            //onChangesDelete()
                            expan.value = false
                        }) {
                            Text(
                                text = "Скопировать ссылку",
                                color = MaterialTheme.colors.primary,
                                fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    if(state == MyTimeTableState.changed) {
                        DropdownMenuItem(onClick = {
                            onChangesDelete()
                            expan.value = false
                        }) {
                            Text(
                                text = "Удалить изменения",
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    DropdownMenuItem(onClick = {
                        onDelete()
                        expan.value = false
                    }) {
                        Text(
                            text = "Удалить",
                            color = Color.Red,
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

fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Composable
fun MyTimeTableView(navigation: NavHostController, user: MutableState<user>, tables: MutableState<SavedTables>) {
    val systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val barColor = MaterialTheme.colors.background

    var context = LocalContext.current
    var activity =  remember { context.getActivity() }
    var wasLoad = remember { mutableStateOf(false)}

    SideEffect {
        systemController.setNavigationBarColor(
            barColor,useDarkIcons
        )
        systemController.setStatusBarColor(
            barColor,useDarkIcons
        )
    }

    LaunchedEffect(key1 = wasLoad) {
        if(!wasLoad.value) {
            wasLoad.value = true
            Fuel.post("https://$mainDomain/main.php",
                listOf(
                    "action" to "get_my_timetables",
                    "login" to user.value.login,
                    "session" to user.value.session
                )
            ).responseString { _, _, result ->
                Log.i("test", result.get())
                val request = Gson().fromJson(result.get(), requestStruct::class.java)

                if(request.error.code == 0) {
                    user.value.session = request.session!!
                    tables.value.globalTables.removeAll { true }

                    for (i in request.timeTables!!) {
                        tables.value.globalTables.add(i)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .padding(16.dp, 16.dp, 16.dp, 13.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    navigation.popBackStack()
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
            )

            if (true) {
                TextButton(
                    onClick = {
                        var tbl = emptyTimeTable
                        tbl.name = "Без имени"
                        tables.value.localTables.add(tbl)

                        Log.i("test", "${tables.value.localTables.size}")
                        tables.value.saveArray(MyTimeTableState.local, context)
                    },
                ) {
                    Text(
                        text = "Добавить",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
        Text(
            text = "Мои расписания",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 31.sp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()

        ) {
            item {
                Text(
                    text = "Опубликованные",
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp, 22.dp, 16.dp, 11.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colors.primary
                )
            }

            items(tables.value.globalTables) {item ->
                DrawTable(
                    state = if(tables.value.isLoad(item.id) && tables.value.globalSavedTables.first {
                        it.id == item.id.toInt()
                        }.isChange()
                    ) MyTimeTableState.changed else MyTimeTableState.global,
                    name = item.name,
                    code = item.invite_code!!,
                    {
                        Fuel.post(
                            "https://$mainDomain/main.php",
                            listOf(
                                "action" to "update_timetable",
                                "login" to user.value.login,
                                "session" to user.value.session,
                                "id" to item.id,
                                "json" to Gson().toJson(tables.value.globalSavedTables.first {
                                    it.id == item.id.toInt()
                                }.table)
                            )
                        ).responseString{_,_,result ->
                            var request = Gson().fromJson(result.get(), requestStruct::class.java)

                            if(request.error.code == 0) {
                                item.name = tables.value.globalSavedTables.first {
                                    it.id == item.id.toInt()
                                }.table.name

                                tables.value.globalSavedTables.removeAll {
                                    it.id == item.id.toInt()
                                }
                            }
                        }
                    },
                    {
                        Fuel.post(
                            "https://$mainDomain/main.php",
                            listOf(
                                "action" to "delete_timetable",
                                "login" to user.value.login,
                                "session" to user.value.session,
                                "id" to item.id
                            )
                        ).responseString() {_,_, result ->
                            var request = Gson().fromJson(result.get(), requestStruct::class.java)

                            if(request.error.code == 0) {
                                tables.value.globalTables.removeAll {
                                    it.id == item.id
                                }

                                tables.value.clearSaved(context)
                                tables.value.saveArray(MyTimeTableState.global, context)
                            }
                        }
                    }, {
                        if(tables.value.isLoad(item.id) && tables.value.globalSavedTables.first {
                            it.id == item.id.toInt()
                            }.isChange()) {
                            tables.value.selectedType = MyTimeTableState.global
                            tables.value.selectedID = item.id.toInt()
                            tables.value.selectedTable = tables.value.globalTables.indexOfFirst {
                                it.id == item.id
                            }

                            navigation.navigate("editor")

                        } else {
                            Fuel.post(
                                "https://$mainDomain/main.php",
                                listOf(
                                    "action" to "get_timetable",
                                    "id" to item.id
                                )
                            ). responseString{_,_,result ->
                                var request = Gson().fromJson(result.get(), requestStruct::class.java)
                                if(request.error.code == 0) {

                                    var loadTable = request.timetable!!.json!!
                                    loadTable.TableID = request.timetable!!.id
                                    var secondTable = Gson().fromJson(result.get(), requestStruct::class.java).timetable!!.json!!

                                    secondTable.TableID = loadTable.TableID

                                    tables.value.globalSavedTables.removeAll {
                                        it.id == item.id.toInt()
                                    }
                                    tables.value.globalSavedTables.add(
                                        savedTimeTableInfo(
                                            loadTable.TableID!!,
                                            loadTable,
                                            secondTable
                                        )
                                    )

                                    tables.value.saveArray(MyTimeTableState.global, context)

                                    tables.value.selectedType = MyTimeTableState.global
                                    tables.value.selectedID = item.id.toInt()
                                    tables.value.selectedTable = tables.value.globalTables.indexOfFirst {
                                        it.id == item.id
                                    }
                                    activity!!.runOnUiThread {
                                        navigation.navigate("editor")
                                    }
                                }
                            }
                        }
                    }, {
                        tables.value.globalSavedTables.removeAll {
                            it.id == item.id.toInt()
                        }
                    })
            }

            item {
                Text(
                    text = "На устройстве",
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp, 22.dp, 16.dp, 11.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colors.primary
                )
            }

            items(tables.value.localTables) { it ->
                DrawTable(state = MyTimeTableState.local, name = it.name, code = "",
                    onShare = {
                    Fuel.post(
                        "https://$mainDomain/main.php",
                        listOf(
                            "action" to "create_timetable",
                            "login" to user.value.login,
                            "session" to user.value.session,
                            "json" to Gson().toJson(it)
                        )
                    ).responseString {_, _, result ->
                        val request = Gson().fromJson(result.get(), requestStruct::class.java)

                        if(request.error.code == 0) {
                            tables.value.globalTables.add(
                                globalTablesInfo(
                                    it.name,
                                    request.id!!,
                                    request.invite_code,
                                )
                            )

                            tables.value.localTables.remove(it)
                            tables.value.saveArray(MyTimeTableState.global, context)
                            tables.value.saveArray(MyTimeTableState.local, context)
                        } else {
                            Log.i("test", "error")
                        }
                    }
                },
                    onDelete = {
                        tables.value.localTables.remove(it)
                },
                    onTap = {
                        tables.value.selectedID = -1
                        tables.value.selectedType = MyTimeTableState.local
                        tables.value.selectedTable = tables.value.localTables.indexOfFirst {value ->
                            value == it
                        }

                        navigation.navigate("editor")
                })
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun MyTimeTableViewPreview() {
    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), true, null, false)

    ) {
        Row(
            Modifier
                .padding(16.dp, 16.dp, 16.dp, 13.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    //navigation.popBackStack()
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
            )

            if (true) {
                TextButton(
                    onClick = {
                        // в редактор
                    },
                ) {
                    Text(
                        text = "Добавить",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }

        Text(
            text = "Мои расписания",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 31.sp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        Text(
            text = "Опубликованные",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 22.dp, 16.dp, 11.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        repeat(3) {
            DrawTable(MyTimeTableState.global, "", "")
        }
        Text(
            text = "Имею доступ",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 22.dp, 16.dp, 11.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        repeat(3) {
            DrawTable(MyTimeTableState.local, "", "")
        }

        Text(
            text = "На устройстве",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 22.dp, 16.dp, 11.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        repeat(2) {
            DrawTable(MyTimeTableState.changed, "", "")
        }
    }
}