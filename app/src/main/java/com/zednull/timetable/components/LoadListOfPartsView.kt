package com.zednull.timetable.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.httpPost
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zednull.timetable.components.ui.MyTimeTableState
import com.zednull.timetable.structure.mainDomain
import com.zednull.timetable.structure.requestStruct
import com.zednull.timetable.ui.theme.TimeTableTheme

@Composable
fun DrawVariant()
{
    val isPressed = remember { mutableStateOf(false) }
    Row (
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 9.dp)
            .fillMaxWidth()
            .height(40.dp)
            .background(
                    MaterialTheme.colors.secondary,
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
                            //выбор названия
                            Log.i("ss", "click!")
                        }
                        isPressed.value = false
                    },
                )
            }
    )
    {

        Text(

            text = "Имя",
            fontSize = 16.sp,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(28.dp, 8.dp, 6.dp, 0.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true)
        )

        TextButton(modifier = Modifier
            .width(40.dp)
            .padding(0.dp, 0.dp, 0.dp, 0.dp),
            onClick = {
                // удаление названия
                Log.i("ss", "delet")
            }
        )
        {
            Text(
                modifier = Modifier
                    .width(20.dp)
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
                text = "М",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary
            )
        }
    }
}


@Composable
fun LoadListOfPartsView(navigation: NavHostController, loadTable: MutableState<String>, typeOfPart: Int) {
    var systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    var barColor = MaterialTheme.colors.background

    SideEffect {
        systemController.setNavigationBarColor(
            barColor,useDarkIcons
        )
        systemController.setStatusBarColor(
            barColor,useDarkIcons
        )
    }

    var rezult = remember { mutableStateOf("") }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), true, null, false)

    ) {
        Row(
            Modifier
                .padding(16.dp, 16.dp, 16.dp, 10.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    loadTable.value = "ses"
                    navigation.popBackStack()
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
            text =
            when (typeOfPart) {
                1 -> "Дисциплины"
                2 -> "Преподаватели"
                3 -> "Аудитории"
                4 -> "Типы пар"
                else -> "error"
            },
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 22.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )


        repeat(3)
        {
            DrawVariant()
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true)
        )


        Row()
        {

            InputEditText(
                value = rezult.value, onValueChange = {
                    rezult.value = it.filter { "1234567890".contains(it) }
                },
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
                keyboardOptions = KeyboardOptions(
                    KeyboardCapitalization.None,
                    false,
                    KeyboardType.Number,
                    ImeAction.Default
                ),
                maxLines = 1,
                placeHolderString = "Код"
            )

            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                onClick = {
                    //loadTable.value = rezult.value!!
                },
                modifier = Modifier
                    .padding(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary,
                    disabledBackgroundColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colors.onSecondary
                ),
            ) {
                Text(
                    text = "Вот это",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        }

    }


}



@Preview(showBackground = true)
@Composable
fun previewLoadListOfPartsView() {
    var rezult = remember { mutableStateOf("") }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), true, null, false)

    ) {
        Row(
            Modifier
                .padding(16.dp, 16.dp, 16.dp, 10.dp)
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
            text = "Преподаватели",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 22.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )


        repeat(3)
        {
            DrawVariant()
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true)
        )


        Row(
        ) {

            InputEditText(
                value = rezult.value, onValueChange = {
                    rezult.value = it.filter { "1234567890".contains(it) }
                },
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
                keyboardOptions = KeyboardOptions(
                    KeyboardCapitalization.None,
                    false,
                    KeyboardType.Number,
                    ImeAction.Default
                ),
                maxLines = 1,
                placeHolderString = "Код"
            )

            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                onClick = {
                    //loadTable.value = rezult.value!!
                },
                modifier = Modifier
                    .padding(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary,
                    disabledBackgroundColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colors.onSecondary
                ),
            ) {
                Text(
                    text = "Вот это",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        }
    }

/*var rezult = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputEditText(value = rezult.value, onValueChange = {
                rezult.value = it.filter { "1234567890".contains(it) }
            },
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .weight(1f, true)
                    .height(42.dp)
                    .padding(8.dp, 0.dp, 8.dp, 0.dp),
                keyboardOptions = KeyboardOptions(
                    KeyboardCapitalization.None,
                    false,
                    KeyboardType.Number,
                    ImeAction.Default
                ),
                maxLines = 1,
                placeHolderString = "Код"
            )

            Spacer(modifier = Modifier.width(8.dp))
            TextButton(onClick = {
                loadTable.value = rezult.value!!
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
            ) {
                Text(text = "Вот это",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier
            .fillMaxSize()
            .weight(1f, true))
    }

 */