package com.zednull.timetable.components.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.zednull.timetable.structure.user
import java.util.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zednull.timetable.LoadListOfPartsActivity
import com.zednull.timetable.components.SettingsView
import com.zednull.timetable.components.LoadListOfPartsView
import com.zednull.timetable.components.Navigation
import com.zednull.timetable.components.bottomBar





@Composable
fun PartEditorView(
    navigation: NavController,
    loadTable: MutableState<String>,
    dist: MutableState<String>,
    prep: MutableState<String>,
    room: MutableState<String>,
    typ: MutableState<String>) {
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





    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), true, null, false)

    ) {
        Row(
            Modifier
                .padding(16.dp, 16.dp, 16.dp, 8.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    loadTable.value = "close"!!
                },
            ) {
                Text(
                    text = "Отмена",
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
                    loadTable.value = "add"!!
                },
                enabled = (dist.value != "" && prep.value != "" && room.value != "" && typ.value != "")
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
            text = "Название дисциплины",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                navigation.navigate("edit1")
            },modifier = Modifier
                .padding(16.dp, 10.dp, 16.dp, 12.dp).fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = if (dist.value != "") dist.value else "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.primary

            )
        }

        Text(
            text = "Имя преподавателя",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                navigation.navigate("edit2")
            },modifier = Modifier
                .padding(16.dp, 10.dp, 16.dp, 12.dp)
                .fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = if (prep.value != "") prep.value else "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.primary

            )
        }

        Text(
            text = "Аудитория",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 4.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                navigation.navigate("edit3")
            },modifier = Modifier
                .padding(16.dp, 12.dp, 16.dp, 12.dp).fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = if (room.value != "") room.value else "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.primary

            )
        }

        Text(
            text = "Тип пары",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 4.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                navigation.navigate("edit4")
            },modifier = Modifier
                .padding(16.dp, 12.dp, 16.dp, 12.dp).fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = if (typ.value != "") typ.value else "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.primary

            )
        }

    }
}

@Preview (showBackground = true)
@Composable
fun PartEditorViewPreview() {


    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), true, null, false)

    ) {
        Row(
            Modifier
                .padding(16.dp, 16.dp, 16.dp, 8.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    //navigation.popBackStack()
                },
            ) {
                Text(
                    text = "Отмена",
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


        }

        Text(
            text = "Название дисциплины",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                //navigation.popBackStack()
            },modifier = Modifier
                .padding(16.dp, 10.dp, 16.dp, 12.dp).fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.primary

            )
        }

        Text(
            text = "Имя преподавателя",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                //navigation.popBackStack()
            },modifier = Modifier
                .padding(16.dp, 10.dp, 16.dp, 12.dp)
                .fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.primary

            )
        }

        Text(
            text = "Аудитория",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 4.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                //navigation.popBackStack()
            },modifier = Modifier
                .padding(16.dp, 12.dp, 16.dp, 12.dp).fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.primary

            )
        }

        Text(
            text = "Тип пары",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp, 4.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )

        TextButton(
            onClick = {
                //navigation.popBackStack()
            },modifier = Modifier
                .padding(16.dp, 12.dp, 16.dp, 12.dp).fillMaxWidth().align(Alignment.Start)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )

        ) {
            Text(
                text = "Выбрать",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.primary

            )
        }

    }
}