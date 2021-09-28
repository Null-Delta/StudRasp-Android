package com.zednull.studrasp.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zednull.studrasp.structure.LessonTime

@Composable
fun TimeSettingsView(navigation: NavController, tables: MutableState<SavedTables>) {
    var selectedTimeH = remember { mutableStateOf("") }
    var selectedTimeM = remember { mutableStateOf("") }

    var selectedLesson = remember { mutableStateOf(-1) }
    var selectedPart = remember { mutableStateOf(0) }

    var localTimes = remember { mutableStateListOf<LessonTime>(
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0),
        LessonTime(0,0)
    ) }

    var firstOpen = remember { mutableStateOf(false)}

    LaunchedEffect(key1 = firstOpen.value) {
        if(!firstOpen.value) {
            firstOpen.value = true
            localTimes.removeAll { true }
            for (i in tables.value.selectedTable().lessonsTime) {
                localTimes.add(LessonTime(i.start, i.end))
            }
        }
    }

    if(selectedLesson.value != -1) {
        AlertDialog(
            onDismissRequest = {  },
            title = {
                Text(
                    text = (if(selectedPart.value == 0) "Начало" else "Конец") + " ${selectedLesson.value + 1} пары",
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
                Column() {
                    InputEditText(
                        value = "",
                        onValueChange = {
                            //inText.value = it
                        },
                        placeHolderString = "",
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            //.background(MaterialTheme.colors.onPrimary, MaterialTheme.shapes.medium)
                            .padding(8.dp, 0.dp, 8.dp, 0.dp),
                        enabled = false
                    )

                    Row() {

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, true))
                        Box()
                        {
                            InputEditText(
                                value = selectedTimeH.value,
                                onValueChange = {
                                    if (it.length >= 4)
                                    {
                                        selectedTimeH.value = ""
                                    }
                                    else if(it.length == 3) {
                                        if (selectedTimeH.value[0] == selectedTimeH.value[1] && it[0] == selectedTimeH.value[1] && it[1] == selectedTimeH.value[1] && it[2] == selectedTimeH.value[1])
                                        {
                                            selectedTimeH.value = " "
                                            selectedTimeH.value = (it[0].toString() + it[1].toString()).filter { "1234567890".contains(it) }
                                        }
                                        else if (selectedTimeH.value == (it[0].toString()+it[1].toString()).filter { "1234567890".contains(it) })
                                            selectedTimeH.value = (it[1].toString()+it[2].toString()).filter { "1234567890".contains(it) }
                                        else if (selectedTimeH.value == (it[0].toString()+it[2].toString()).filter { "1234567890".contains(it) })
                                            selectedTimeH.value = (it[2].toString()+it[1].toString()).filter { "1234567890".contains(it) }
                                        else if (selectedTimeH.value == (it[1].toString()+it[2].toString()).filter { "1234567890".contains(it) })
                                            selectedTimeH.value = (it[2].toString()+it[0].toString()).filter { "1234567890".contains(it) }
                                        else selectedTimeH.value = ""
                                    }
                                    else if (it.length == 2)
                                    {
                                        if (selectedTimeH.value == (it[0].toString()).filter { "1234567890".contains(it) })
                                            selectedTimeH.value = it.filter { "1234567890".contains(it) }
                                        else if (selectedTimeH.value == (it[1].toString()).filter { "1234567890".contains(it) })
                                            selectedTimeH.value = (it[1].toString()+it[0].toString()).filter { "1234567890".contains(it) }
                                        else
                                            selectedTimeH.value = ""
                                    }
                                    else
                                        selectedTimeH.value = it.filter { "1234567890".contains(it) };
                                },
                                placeHolderString = "",
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(48.dp)
                                    .alpha(0.0f)
                                    .background(
                                        MaterialTheme.colors.onPrimary,
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(8.dp, 0.dp, 8.dp, 0.dp),
                                keyboardOptions = KeyboardOptions(
                                    KeyboardCapitalization.None,
                                    false,
                                    KeyboardType.Number,
                                    ImeAction.Default
                                ),
                                //enabled = false
                            )

                            Text(
                                if (selectedTimeH.value.length == 2) {
                                    if (selectedTimeH.value.toInt() >= 24)
                                        "0"+selectedTimeH.value[1].toString()
                                    else
                                        selectedTimeH.value
                                }
                                else
                                    if (selectedTimeH.value.length == 1)
                                        "0"+selectedTimeH.value
                                    else
                                        "00",
                                fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color =MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(48.dp)
                                    .background(
                                        MaterialTheme.colors.onPrimary,
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(8.dp, 6.dp, 8.dp, 0.dp),
                                )
                        }
                        //Text(" : ")
                        Spacer(modifier = Modifier.width(8.dp))
                        Box()
                        {
                            InputEditText(
                                value = selectedTimeM.value,
                                onValueChange = {
                                    if (it.length >= 4) {
                                        selectedTimeM.value = ""
                                    } else if (it.length == 3) {
                                        if (selectedTimeM.value[0] == selectedTimeM.value[1] && it[0] == selectedTimeM.value[1] && it[1] == selectedTimeM.value[1] && it[2] == selectedTimeM.value[1]) {
                                            selectedTimeM.value = " "
                                            selectedTimeM.value =
                                                (it[0].toString() + it[1].toString()).filter {
                                                    "1234567890".contains(it)
                                                }
                                        } else if (selectedTimeM.value == (it[0].toString() + it[1].toString()).filter {
                                                "1234567890".contains(
                                                    it
                                                )
                                            })
                                            selectedTimeM.value =
                                                (it[1].toString() + it[2].toString()).filter {
                                                    "1234567890".contains(it)
                                                }
                                        else if (selectedTimeM.value == (it[0].toString() + it[2].toString()).filter {
                                                "1234567890".contains(
                                                    it
                                                )
                                            })
                                            selectedTimeM.value =
                                                (it[2].toString() + it[1].toString()).filter {
                                                    "1234567890".contains(it)
                                                }
                                        else if (selectedTimeM.value == (it[1].toString() + it[2].toString()).filter {
                                                "1234567890".contains(
                                                    it
                                                )
                                            })
                                            selectedTimeM.value =
                                                (it[2].toString() + it[0].toString()).filter {
                                                    "1234567890".contains(it)
                                                }
                                        else selectedTimeM.value = ""
                                    } else if (it.length == 2) {
                                        if (selectedTimeM.value == (it[0].toString()).filter {
                                                "1234567890".contains(
                                                    it
                                                )
                                            })
                                            selectedTimeM.value =
                                                it.filter { "1234567890".contains(it) }
                                        else if (selectedTimeM.value == (it[1].toString()).filter {
                                                "1234567890".contains(
                                                    it
                                                )
                                            })
                                            selectedTimeM.value =
                                                (it[1].toString() + it[0].toString()).filter {
                                                    "1234567890".contains(it)
                                                }
                                        else
                                            selectedTimeM.value = ""
                                    } else
                                        selectedTimeM.value =
                                            it.filter { "1234567890".contains(it) };
                                },
                                placeHolderString = "",
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(48.dp)
                                    .alpha(0.0f)
                                    .background(
                                        MaterialTheme.colors.onPrimary,
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(8.dp, 0.dp, 8.dp, 0.dp),
                                keyboardOptions = KeyboardOptions(
                                    KeyboardCapitalization.None,
                                    false,
                                    KeyboardType.Number,
                                    ImeAction.Default
                                ),
                            )

                            Text(
                                if (selectedTimeM.value.length == 2) {
                                    if (selectedTimeM.value.toInt() >= 60)
                                        "0"+selectedTimeM.value[1].toString()
                                    else
                                        selectedTimeM.value
                                }
                                else
                                    if (selectedTimeM.value.length == 1)
                                        "0"+selectedTimeM.value
                                    else
                                        "00",
                                fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.body1.fontFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color =MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(48.dp)
                                    .background(
                                        MaterialTheme.colors.onPrimary,
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(8.dp, 6.dp, 8.dp, 0.dp),
                            )
                        }
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, true))



                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (selectedTimeH.value.length == 2) {
                            if (selectedTimeH.value.toInt() >= 24)
                                selectedTimeH.value = "0"+selectedTimeH.value[1].toString()
                        }
                        else if (selectedTimeH.value.length == 1)
                            selectedTimeH.value = "0"+selectedTimeH.value
                        else
                            selectedTimeH.value = "00"

                        if (selectedTimeH.value.length == 2) {
                            if (selectedTimeH.value.toInt() >= 60)
                                selectedTimeH.value = "0"+selectedTimeH.value[1].toString()
                        }
                        else if (selectedTimeH.value.length == 1)
                            selectedTimeH.value = "0"+selectedTimeH.value
                        else
                            selectedTimeH.value = "00"

                        if(selectedPart.value == 0) {
                            localTimes[selectedLesson.value].start = selectedTimeH.value.toInt() * 60 +
                                    selectedTimeM.value.toInt()
                        } else {
                            localTimes[selectedLesson.value].end = selectedTimeH.value.toInt() * 60 +
                                    selectedTimeM.value.toInt()
                        }

                        selectedLesson.value = -1
                    },
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 8.dp),



                    ) {
                    Text("Сохранить",
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
                        selectedLesson.value = -1
                    },
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 8.dp),
                ) {
                    Text("Отмена",
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
    Column() {
        Row(
            modifier = Modifier.padding(8.dp,8.dp,8.dp,16.dp)
        ) {
            TextButton(onClick = {
                navigation.popBackStack()
                 }) {
                Text(
                    text = "Назад",
                    color = MaterialTheme.colors.primary,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))

            TextButton(onClick = {
                tables.value.selectedTable().lessonsTime = localTimes
                navigation.popBackStack()
            }) {
                Text(
                    text = "Сохранить",
                    color = MaterialTheme.colors.primary,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        }

        Text(
            text = "Время пар",
            color = MaterialTheme.colors.primary,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(16.dp,0.dp,16.dp,16.dp)
        )

        LazyColumn() {
            items(8) { item ->
                Row(
                    modifier = Modifier
                        .padding(16.dp, 0.dp, 16.dp, 16.dp)
                        .height(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp)
                            .padding(4.dp)
                            .background(MaterialTheme.colors.secondary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (item + 1).toString(),
                            color = MaterialTheme.colors.primary,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true))

                    Box(
                        modifier = Modifier
                            .height(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "От:",
                            color = MaterialTheme.colors.onSecondary,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = {
                        selectedTimeH.value = (localTimes[item].start / 60).toString()
                        selectedTimeM.value = (localTimes[item].start % 60).toString()
                        selectedLesson.value = item
                        selectedPart.value = 0
                        },
                    modifier = Modifier
                        .height(36.dp)
                        .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                    ) {
                        Text("${getTime(localTimes[item].start / 60)}:${getTime(localTimes[item].start % 60)}")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .height(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "До:",
                            color = MaterialTheme.colors.onSecondary,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = {
                        selectedTimeH.value = (localTimes[item].end / 60).toString()
                        selectedTimeM.value = (localTimes[item].end % 60).toString()

                        selectedLesson.value = item
                        selectedPart.value = 1
                    },
                        modifier = Modifier
                            .height(36.dp)
                            .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                    ) {
                        Text("${getTime(localTimes[item].end / 60)}:${getTime(localTimes[item].end % 60)}")
                    }

                }
            }
        }

        Spacer(modifier = Modifier
            .fillMaxSize()
            .weight(1f, true))
    }
}

fun getTime(value: Int): String {
    return if(value < 10) return "0$value" else "$value"
}

@Composable
@Preview
fun TimeSettingsPreivew() {

}