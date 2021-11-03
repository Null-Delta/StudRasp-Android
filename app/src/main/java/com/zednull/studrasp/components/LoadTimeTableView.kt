package com.zednull.studrasp.components

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.ScrollView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kittinunf.fuel.Fuel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zednull.studrasp.R
import com.zednull.studrasp.structure.TimeTableStructure
import com.zednull.studrasp.structure.emptyTimeTable
import com.zednull.studrasp.structure.mainDomain
import com.zednull.studrasp.structure.requestStruct
import com.zednull.studrasp.ui.theme.TimeTableTheme


class PinnedTable(var name: String, var invite_code: String)

@Composable
fun DrawTable2(
    name: String,
    code: String,
    onDelete: () -> (Unit) = {},
    onTap: () -> (Unit) = {})
{
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp,0.dp,0.dp,8.dp)
            .height(36.dp)
            .background(
                    MaterialTheme.colors.secondary,
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
            color = MaterialTheme.colors.primary,
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
            color =  MaterialTheme.colors.onSecondary,
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

        Box(
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),
        ) {

            IconButton(
                onClick = { onDelete() },
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .background(Color.Transparent, MaterialTheme.shapes.medium)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun LoadTimeTableView(loadTable: MutableState<TimeTableStructure>) {
    var systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    var barColor = MaterialTheme.colors.background

    var context = LocalContext.current

    var code = remember { mutableStateOf("") }
    var errorText = remember { mutableStateOf("") }
    var isErrorShow = remember { mutableStateOf(false) }

    SideEffect {
        systemController.setNavigationBarColor(
            barColor,useDarkIcons
        )
        systemController.setStatusBarColor(
            barColor,useDarkIcons
        )
    }

    var lastTables: SnapshotStateList<PinnedTable> =
            Gson().fromJson(
                context.getSharedPreferences(
                    "preferences",
                    Context.MODE_PRIVATE
                ).getString(
                    "lastTables",
                    Gson().toJson(List(0) { PinnedTable("","") })
                ), object : TypeToken<SnapshotStateList<PinnedTable>>() { }.type
            )

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
    
    if(isErrorShow.value) {
        AlertDialog(
            onDismissRequest = { isErrorShow.value = false },
            title = { Text(text = "Ошибка") },
            text = {
                Text(text = errorText.value)
            },
            confirmButton = {
                // below line we are adding on click
                // listener for our confirm button.
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputEditText(
                value = code.value, modifier = Modifier
                    .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .weight(1f, true)
                    .height(42.dp)
                    .padding(8.dp, 0.dp, 8.dp, 0.dp),
                onValueChange = {
                    code.value = it
                },
                placeHolderString = "Код",
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    KeyboardCapitalization.None,
                    false,
                    KeyboardType.Text,
                    ImeAction.Default
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            TextButton(onClick = {
                Fuel.post("https://$mainDomain/main.php", listOf("action" to "get_timetable_by_invite_code", "invite_code" to code.value))
                    .responseString { request, response, result ->
                        var request: requestStruct = Gson().fromJson(result.get(),requestStruct::class.java)
                        if(request.error.code != 0) {
                            errorText.value = request.error.message
                            isErrorShow.value = true
                        } else {
                            loadTable.value = request.timetable!!.json!!
                            loadTable.value.TableID = request.timetable!!.id
                            loadTable.value.invite_code = code.value

                            if(lastTables.firstOrNull { v ->
                                   v.name == loadTable.value.name &&
                                    v.invite_code == loadTable.value.invite_code!!.lowercase()
                            } == null) {
                                lastTables.add(
                                    PinnedTable(
                                        loadTable.value.name,
                                        loadTable.value.invite_code!!.lowercase()
                                    )
                                )

                                if(lastTables.size > 5)
                                    lastTables.removeFirst()
                            }
                            var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                            editor.putString("lastTables", Gson().toJson(lastTables))
                            editor.apply()
                        }
                    }
            },
                modifier = Modifier
                    .height(42.dp)
                    .padding(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary,
                    disabledBackgroundColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colors.onSecondary
                ),
                enabled = code.value != ""
            ) {
                Text(text = "Добавить",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        LazyColumn() {
            if(lastTables.size > 0) {
                item{
                    Text("Недавние",
                        fontSize = 20.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(0.dp,16.dp,0.dp,16.dp)
                    )
                }

                items(lastTables) {
                    DrawTable2(
                        name = it.name,
                        code = it.invite_code,
                        onTap = {
                            Fuel.post("https://$mainDomain/main.php", listOf("action" to "get_timetable_by_invite_code", "invite_code" to it.invite_code))
                                .responseString { request, response, result ->
                                    var request: requestStruct = Gson().fromJson(result.get(),requestStruct::class.java)
                                    if(request.error.code != 0) {
                                        errorText.value = request.error.message
                                        isErrorShow.value = true

                                        lastTables.removeAll{v ->
                                            v.name == it.name && v.invite_code == it.invite_code
                                        }
                                        var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                                        editor.putString("lastTables", Gson().toJson(lastTables))
                                        editor.apply()

                                    } else {
                                        loadTable.value = request.timetable!!.json!!
                                        loadTable.value.TableID = request.timetable!!.id
                                        loadTable.value.invite_code = it.invite_code
                                    }
                                }
                        },
                        onDelete = {
                            lastTables.removeAll { p -> p == it }
                            var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                            editor.putString("lastTables", Gson().toJson(lastTables))
                            editor.apply()
                        })
                }
            }

            if(pinnedTables.size > 0) {
                item{
                    Text("Избранные",
                        fontSize = 20.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(0.dp,16.dp,0.dp,16.dp)
                    )
                }

                items(pinnedTables) {
                    DrawTable2(
                        name = it.name,
                        code = it.invite_code,
                        onTap = {
                            Fuel.post("https://$mainDomain/main.php", listOf("action" to "get_timetable_by_invite_code", "invite_code" to it.invite_code))
                                .responseString { request, response, result ->
                                    var request: requestStruct = Gson().fromJson(result.get(),requestStruct::class.java)
                                    if(request.error.code != 0) {
                                        errorText.value = request.error.message
                                        isErrorShow.value = true

                                        pinnedTables.removeAll{v ->
                                            v.name == it.name && v.invite_code == it.invite_code
                                        }
                                        var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                                        editor.putString("pinnedTables", Gson().toJson(pinnedTables))
                                        editor.apply()
                                    } else {
                                        loadTable.value = request.timetable!!.json!!
                                        loadTable.value.TableID = request.timetable!!.id
                                        loadTable.value.invite_code = it.invite_code
                                    }
                                }
                        },
                        onDelete = {
                            pinnedTables.removeAll { p -> p == it }
                            var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                            editor.putString("pinnedTables", Gson().toJson(pinnedTables))
                            editor.apply()
                        })
                }
            }
        }
        Spacer(modifier = Modifier
            .fillMaxSize()
            .weight(1f, true))
    }
}

@Composable
fun InputEditText(
    value: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    contentTextStyle: TextStyle = TextStyle.Default,
    hintTextStyle: TextStyle = TextStyle.Default,
    placeHolderString: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorColor: Color = MaterialTheme.colors.primary,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        modifier = modifier,
        textStyle = TextStyle(
            MaterialTheme.colors.primary, 16.sp, FontWeight.Medium,
            FontStyle.Normal, null, MaterialTheme.typography.body1.fontFamily,
            null),
        decorationBox = {innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        if (contentTextStyle.textAlign == TextAlign.Start)
                            IntOffset(x = 10, y = 0)
                        else
                            IntOffset(x = 0, y = 0)
                    },
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeHolderString,
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                innerTextField()

            }
        },
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(cursorColor),
    )
}


@Preview(showBackground = true)
@Composable
fun previewLoadTimeTableView() {
    TimeTableTheme {

    }
}