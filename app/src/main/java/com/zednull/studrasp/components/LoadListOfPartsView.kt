package com.zednull.studrasp.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.studrasp.R
import com.zednull.studrasp.ui.theme.TimeTableTheme





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
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(0.dp, 16.dp, 0.dp, 16.dp)
                    .fillMaxWidth()
        ) },
        text = {
            Column(
                modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
            ) {
                InputEditText(
                    value = "",
                    onValueChange = {

                    },
                    placeHolderString = "",
                    modifier = Modifier
                        .height(16.dp)
                        .padding(8.dp, 0.dp, 8.dp, 0.dp),
                    enabled = false
                )
                InputEditText(
                    value = inText.value,
                    onValueChange = {
                        inText.value = it
                    },
                    placeHolderString = "Введите название",
                    modifier = Modifier
                        .height(42.dp)
                        .background(MaterialTheme.colors.onPrimary, MaterialTheme.shapes.medium)
                        .padding(8.dp, 0.dp, 8.dp, 0.dp)
                )
            }
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
                    .padding(0.dp, 8.dp, 0.dp, 8.dp),
                enabled = inText.value != "",

            ) {
                Text("Добавить",
                    color = if(inText.value != "") MaterialTheme.colors.primary else MaterialTheme.colors.onSecondary,
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

@Composable
fun DrawVariant(
    namePart: String,
    loadTable: MutableState<String>,
    navigation: NavHostController,
    arrayOfParts: MutableState<MutableList<String>>,
    context: Context,
    typeOfPart: Int,
    onTap: () -> (Unit) = {},
    onDelete: () -> Unit = {}
)
{
    Row (
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 9.dp)
            .fillMaxWidth()
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

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .height(36.dp)
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
        ) {
            Text(
                text = namePart,
                fontSize = 16.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Left
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true)
        )

        IconButton(
            onClick = {
                onDelete()
            },
            modifier = Modifier
                .height(36.dp)
                .width(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
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

    var localArray = arrayOfParts.value.toMutableStateList()

    Column(

        modifier = Modifier
            .fillMaxSize()
            //.verticalScroll(rememberScrollState(), true, null, false)

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

        LazyColumn() {
            items(localArray) {v ->
                DrawVariant(v, loadTable, navigation, arrayOfParts, context, typeOfPart, {
                    loadTable.value = arrayOfParts.value[localArray.indexOfFirst {
                        it == v
                    }]
                    navigation.popBackStack()
                }, {
                    arrayOfParts.value.removeAll {
                        it == arrayOfParts.value[localArray.indexOfFirst {
                            it == v
                        }]
                    }
                    localArray.remove(v);
                    var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                    editor.putString("partsNum" + typeOfPart.toString(), Gson().toJson(arrayOfParts.value))
                    editor.apply()
                })
            }
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
        LoadListOfPartsView(navigation = navController, loadTable = nameDisp, typeOfPart = 1)
    }
}