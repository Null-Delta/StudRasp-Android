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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.zednull.timetable.LoadListOfPartsActivity


enum class MyTimeTableState {
    global, local, changed
}


@Composable
fun DrawTable(state: MyTimeTableState)
{
    val isPressed = remember { mutableStateOf(false) }
    Row (
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 9.dp)
            .fillMaxWidth()
            .height(40.dp)
            .background(
                if (state == MyTimeTableState.global || state == MyTimeTableState.local)
                    MaterialTheme.colors.secondary
                else
                    MaterialTheme.colors.secondaryVariant,
                MaterialTheme.shapes.medium
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        val pressStartTime = System.currentTimeMillis()
                        isPressed.value = true
                        this.tryAwaitRelease()
                        val pressEndTime = System.currentTimeMillis()
                        val totalPressTime = pressEndTime - pressStartTime
                        if (totalPressTime < 200) {
                            //переход к расписанию
                            Log.i("ss", "click!")
                        }
                        isPressed.value = false
                    },
                )
            }
    )
    {

            Text(

                text = "Расписание",
                fontSize = 16.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                color = if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(8.dp, 8.dp, 6.dp, 0.dp)
            )

        Text(

            text = "1818",
            fontSize = 16.sp,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            color =  if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.onSecondary else Color(0xFFD79515),// закончились варианты цветов
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true)
        )

        if (state == MyTimeTableState.changed)
        {
            TextButton(modifier = Modifier
                .width(30.dp)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),
                onClick = {
                    // в редактор
                }
            )
            {
                Text(
                    modifier = Modifier
                        .width(20.dp)
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                    text = "V",
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
                )
            }
        }


            var expan = remember { mutableStateOf(false) }
            Box(modifier = Modifier
                .width(50.dp)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),) {
                /*
                IconButton(onClick = { expan.value = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                }
                 */
                TextButton(onClick = { expan.value = true }) {
                    Text(
                        text = "...",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
                    )
                }
                DropdownMenu(
                    modifier = Modifier.background(
                        if (state == MyTimeTableState.global || state == MyTimeTableState.local)
                            MaterialTheme.colors.secondary
                        else
                            MaterialTheme.colors.secondaryVariant,
                        MaterialTheme.shapes.medium
                    ),
                    expanded = expan.value,
                    onDismissRequest = { expan.value = false }
                ) {
                    DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                        Text("Удалить")
                    }
                    if (state != MyTimeTableState.global)
                        DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                            Text("Опубликовать")
                    }
                    DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
                        Text("Сделать текущим")
                    }
                }
            }
/*
            Text(
                text = "...",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = if (state == MyTimeTableState.global || state == MyTimeTableState.local) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
                )
*/
    }
}

@Composable
fun MyTimeTableView(navigation: NavHostController) {
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

        // временное решение
    val testname = remember { mutableStateOf("") }

    val context = LocalContext.current
    val loadRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null && result.data != null) {
            testname.value = "" + result.data!!.getStringExtra("name")
        }
    }
        //временное решение

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
                        // в редактор
                        // временное решение
                        var data = Intent(context, LoadListOfPartsActivity::class.java)
                        data.putExtra("type",1)
                        loadRequest.launch(data)
                        // временное решение
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


            DrawTable(MyTimeTableState.global)
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


            DrawTable(MyTimeTableState.local)
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


            DrawTable(MyTimeTableState.changed)
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


            DrawTable(MyTimeTableState.global)
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


            DrawTable(MyTimeTableState.local)
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


            DrawTable(MyTimeTableState.changed)
        }
    }
}