package com.zednull.timetable.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.timetable.ui.theme.TimeTableTheme





@Composable
fun PartInput(
    isPressed: MutableState<Boolean>,
    arrayOfParts: MutableState<MutableList<String>>,
    context: Context,
    typeOfPart: Int
)
{
    val inText = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { !(isPressed.value) },
        title = {
            Text(
                text = "Новая запись",
                fontSize = 20.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(0.dp, 16.dp, 0.dp, 16.dp)
                    .fillMaxWidth()
        ) },
        text = {
            TextField(
                value = inText.value,
                onValueChange = {
                    inText.value = it
                },
                label = { Text("Введите название") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // добавление нового элемента в память
                    arrayOfParts.value.add(inText.value)
                    var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                    editor.putString("partsNum" + typeOfPart.toString(), Gson().toJson(arrayOfParts.value))
                    editor.apply()
                    isPressed.value = false
                },
                        modifier = Modifier
                            .background(MaterialTheme.colors.secondary, RectangleShape)
                            .padding(0.dp, 8.dp, 0.dp, 8.dp)
                            .width(146.dp),

                enabled = inText.value != "",

            ) {
                Text("Добавить", color = MaterialTheme.colors.onBackground,
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isPressed.value = false
                },
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary, RectangleShape)
                    .padding(0.dp, 8.dp, 0.dp, 8.dp)
                    .width(146.dp)
            ) {
                Text("Отмена", color = MaterialTheme.colors.onBackground,
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        },
    )

}

@Composable
fun DrawVariant(
    namePart: String,
    loadTable: MutableState<String>,
    navigation: NavHostController,
    arrayOfParts: MutableState<MutableList<String>>,
    context: Context,
    typeOfPart: Int
)
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
                            loadTable.value = namePart
                            navigation.popBackStack()
                        }
                        isPressed.value = false
                    },
                )
            }
    )
    {

        Text(

            text = namePart,
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
                if (loadTable.value == namePart)
                    loadTable.value = ""
                arrayOfParts.value = arrayOfParts.value.filter { it != namePart }.toMutableList()
                var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                editor.putString("partsNum" + typeOfPart.toString(), Gson().toJson(arrayOfParts.value))
                editor.apply()

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
    var isInputting = remember { mutableStateOf(false) }

    SideEffect {
        systemController.setNavigationBarColor(
            barColor,useDarkIcons
        )
        systemController.setStatusBarColor(
            barColor,useDarkIcons
        )
    }

    val context = LocalContext.current
     // считываем нынешний список
    val arrayOfParts = remember {
        mutableStateOf(
            Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                .getString("partsNum" + typeOfPart.toString(), ""), mutableListOf<String>()::class.java)
        )
    }
    if (arrayOfParts.value == null)
        arrayOfParts.value = mutableListOf<String>()

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

        for (part in arrayOfParts.value)
        {
            DrawVariant(part, loadTable, navigation, arrayOfParts, context, typeOfPart)
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true)
        )


        if (isInputting.value)
        {
            PartInput(isInputting, arrayOfParts, context, typeOfPart)
        }

        TextButton(
            onClick = {
                isInputting.value = true;
            },
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 32.dp)
                .background(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.background,
                disabledBackgroundColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colors.onSecondary
            ),
        ) {
            Text(
                text = "Добавить",
                fontSize = 20.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium
            )
        }

    }


}



@Preview(showBackground = true)
@Composable
fun previewLoadListOfPartsView() {
    TimeTableTheme {
        val nameDisp = remember { mutableStateOf("") }
        val navController = rememberNavController()
        //LoadListOfPartsView(navigation = navController, loadTable = nameDisp, typeOfPart = 1)
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