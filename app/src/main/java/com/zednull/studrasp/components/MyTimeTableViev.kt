package com.zednull.studrasp.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.github.kittinunf.fuel.Fuel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.studrasp.R
import com.zednull.studrasp.structure.*

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
        return if(selectedType == MyTimeTableState.local) {
            localTables[selectedTable]
        } else {
            globalSavedTables.first {
                it.id == selectedID
            }.table
        }
    }

    fun saveArray(state: MyTimeTableState, context: Context) {
        if(state == MyTimeTableState.local) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
            editor.putString("localTables", Gson().toJson(localTables))
            editor.apply()
        } else {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
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

    private fun saveSavedTables(context: Context) {
        val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
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
    onChangesDelete: () -> (Unit) = {},
    onSet: () -> (Unit) = {},
    onCopy: () -> (Unit) = {})
{
    val expan = remember { mutableStateOf(false) }

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


            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
            ) {

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

                    if(state == MyTimeTableState.global || state == MyTimeTableState.changed) {
                        DropdownMenuItem(onClick = {
                            expan.value = false
                            onSet()
                        }) {
                            Text(
                                text = "Использовать",
                                color = MaterialTheme.colors.primary,
                                fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                            )
                        }

                        DropdownMenuItem(onClick = {
                            expan.value = false
                            onCopy()
                        }) {
                            Text(
                                text = "Скопировать код",
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
fun MyTimeTableView(navigation: NavHostController, user: MutableState<user>, tables: MutableState<SavedTables>, code: MutableState<String>) {
    val systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val barColor = MaterialTheme.colors.background

    val context = LocalContext.current
    val activity =  remember { context.getActivity() }
    val wasLoad = remember { mutableStateOf(false)}
    val clipboard = LocalClipboardManager.current

    val isErrorShow = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    SideEffect {
        systemController.setNavigationBarColor(
            barColor,useDarkIcons
        )
        systemController.setStatusBarColor(
            barColor,useDarkIcons
        )
    }

    val localList = tables.value.localTables.toMutableStateList()
    val globalList = tables.value.globalTables.toMutableStateList()

    val isDeleting = remember { mutableStateOf(false) }
    var isDeleteDialogOpen = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isDeleting.value) {
        if(isDeleting.value) {
            if(tables.value.selectedType == MyTimeTableState.local) {
                tables.value.localTables.remove(tables.value.selectedTable())
            } else {
                val item = tables.value.globalTables[tables.value.selectedTable]

                Fuel.post(
                    "https://$mainDomain/main.php",
                    listOf(
                        "action" to "delete_timetable",
                        "login" to user.value.login,
                        "session" to user.value.session,
                        "id" to item.id
                    )
                ).responseString {_,_, result ->
                    val request = Gson().fromJson(result.get(), requestStruct::class.java)

                    if(request.error.code == 0) {
                        tables.value.globalTables.removeAll {
                            it.id == item.id
                        }

                        tables.value.clearSaved(context)
                        tables.value.saveArray(MyTimeTableState.global, context)
                    } else {
                        errorMessage.value = request.error.message
                        isErrorShow.value = true
                    }
                }
            }
            isDeleting.value = false
        }
    }

    if(isErrorShow.value) {
        AlertDialog(
            onDismissRequest = { isErrorShow.value = false },
            title = { Text(text = "Ошибка") },
            text = {
                Text(text = errorMessage.value)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isErrorShow.value = false
                    }
                ) {
                    Text("Ок", color = Color.Black)
                }
            },
        )
    }

    LaunchedEffect(key1 = wasLoad.value) {
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

                    tables.value.saveArray(MyTimeTableState.global, context)
                } else {
                    errorMessage.value = request.error.message
                    isErrorShow.value = true
                }
            }
        }
    }

    if(isDeleteDialogOpen.value) {
        AlertDialog(
            onDismissRequest = {  },
            title = {
                Text(
                    text = "Удаление",
                    fontSize = 20.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(0.dp, 16.dp, 0.dp, 16.dp)
                        .fillMaxWidth()
                ) },
            text = {
                Text(
                    text = "Вы уверены, что хотите удалить расписание?",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(0.dp, 16.dp, 0.dp, 16.dp)
                        .fillMaxWidth()
                ) },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDeleting.value = true
                        isDeleteDialogOpen.value = false
                    },
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 8.dp),
                    ) {
                    Text("Да",
                        fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDeleteDialogOpen.value = false
                    },
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 8.dp),
                ) {
                    Text("Нет",
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    )
                }
            },
        )
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

            TextButton(
                onClick = {
                    val tbl = emptyTimeTable
                    tbl.name = "Без имени"
                    tables.value.localTables.add(tbl)
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
                Row(
                    modifier = Modifier.padding(16.dp,16.dp,16.dp,8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = "Опубликованные",
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colors.primary
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true))

                    TextButton(onClick = {
                        wasLoad.value = false
                    }) {
                        Text(
                            text = "Обновить",
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }

            items(tables.value.globalTables) {item ->
                DrawTable(
                    state = if(tables.value.isLoad(item.id) && tables.value.globalSavedTables.first {
                        it.id == item.id.toInt()
                        }.isChange()
                    ) MyTimeTableState.changed else MyTimeTableState.global,
                    name = if(tables.value.isLoad(item.id) && tables.value.globalSavedTables.first {
                            it.id == item.id.toInt()
                        }.isChange()
                    ) tables.value.globalSavedTables.first {
                        it.id == item.id.toInt()
                    }.table.name else item.name,
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
                            val request = Gson().fromJson(result.get(), requestStruct::class.java)

                            if(request.error.code == 0) {
                                item.name = tables.value.globalSavedTables.first {
                                    it.id == item.id.toInt()
                                }.table.name

                                tables.value.globalSavedTables.removeAll {
                                    it.id == item.id.toInt()
                                }

                                tables.value.saveArray(MyTimeTableState.global, context)
                                tables.value.clearSaved(context)
                            } else {
                                errorMessage.value = request.error.message
                                isErrorShow.value = true
                            }
                        }
                    },
                    {
                        tables.value.selectedType = MyTimeTableState.global
                        tables.value.selectedTable = globalList.indexOfFirst { v ->
                            v.id == item.id
                        }
                        isDeleteDialogOpen.value = true
                    }, {
                        if(tables.value.isLoad(
                                tables.value.globalTables[globalList.indexOfFirst {
                                    it.id == item.id
                                }].id
                            ) && tables.value.globalSavedTables.first {
                            it.id == tables.value.globalTables[globalList.indexOfFirst {
                                it.id == item.id
                            }].id.toInt()
                            }.isChange()) {
                            tables.value.selectedType = MyTimeTableState.global
                            tables.value.selectedID = tables.value.globalTables[globalList.indexOfFirst {
                                it.id == item.id
                            }].id.toInt()
                            tables.value.selectedTable = globalList.indexOfFirst {
                                it.id == item.id
                            }

                            navigation.navigate("editor")

                        } else {

                            Fuel.post(
                                "https://$mainDomain/main.php",
                                listOf(
                                    "action" to "get_timetable",
                                    "id" to tables.value.globalTables[globalList.indexOfFirst { v ->
                                        v == item
                                    }].id
                                )
                            ). responseString{_,_,result ->
                                val request = Gson().fromJson(result.get(), requestStruct::class.java)
                                if(request.error.code == 0) {
                                    val loadTable = request.timetable!!.json!!
                                    loadTable.TableID = request.timetable!!.id
                                    val secondTable = Gson().fromJson(result.get(), requestStruct::class.java).timetable!!.json!!

                                    secondTable.TableID = loadTable.TableID

                                    tables.value.globalSavedTables.removeAll {
                                        it.id == item.id.toInt()
                                    }
                                    tables.value.globalSavedTables.add(
                                        savedTimeTableInfo(
                                            loadTable.TableID,
                                            loadTable,
                                            secondTable
                                        )
                                    )

                                    tables.value.saveArray(MyTimeTableState.global, context)

                                    tables.value.selectedType = MyTimeTableState.global
                                    tables.value.selectedID = tables.value.globalTables[globalList.indexOfFirst {
                                        it.id == item.id
                                    }].id.toInt()
                                    tables.value.selectedTable = globalList.indexOfFirst {
                                        it.id == item.id
                                    }
                                    activity!!.runOnUiThread {
                                        navigation.navigate("editor")
                                    }
                                } else {
                                    errorMessage.value = request.error.message
                                    isErrorShow.value = true
                                }
                            }
                        }
                    }, {
                        tables.value.globalSavedTables.removeAll {
                            it.id == item.id.toInt()
                        }
                    }, {
                        code.value = item.invite_code!!
                    }, {
                        clipboard.setText(AnnotatedString(item.invite_code!!))
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

            items(tables.value.localTables) {
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
                            errorMessage.value = request.error.message
                            isErrorShow.value = true
                        }
                    }
                },
                    onDelete = {
                        tables.value.selectedType = MyTimeTableState.local
                        tables.value.selectedTable = localList.indexOfFirst { v ->
                            v == it
                        }
                        isDeleteDialogOpen.value = true
                },
                    onTap = {
                        tables.value.selectedID = -1
                        tables.value.selectedType = MyTimeTableState.local
                        Log.i("test",it.name)
                        tables.value.selectedTable = localList.indexOfFirst {value ->
                            value == it
                        }
                        navigation.navigate("editor")
                })
            }
        }
    }
}