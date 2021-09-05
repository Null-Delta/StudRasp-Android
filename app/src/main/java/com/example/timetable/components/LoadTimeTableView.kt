package com.example.timetable.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.timetable.structure.ServerTimeTable
import com.example.timetable.structure.requestStruct
import com.example.timetable.ui.theme.TimeTableTheme
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoadTimeTableView(loadTable: MutableState<ServerTimeTable>) {
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
    
    if(isErrorShow.value) {
        AlertDialog(onDismissRequest = { isErrorShow.value = false },
            title = { Text( text = "Ошибка")},
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
            InputEditText(value = code.value, onValueChange = {
                code.value = it.filter { "1234567890".contains(it) }
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
                val queue = Volley.newRequestQueue(context)
                val url = "http://hytale-main.ru/main.php?action=table_q&index=${code.value}"
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        Log.i("json", response)
                        var request: requestStruct = jacksonObjectMapper().readValue(response,requestStruct::class.java)
                        if(request.error.code != 0) {
                            errorText.value = request.error.message
                            isErrorShow.value = true
                        } else {
                            loadTable.value = request.timetable!!
                        }
                    },
                    {
                        errorText.value = "Не удалось получить ответ от сервера. Проверьте подключение к интернету и попробуйте снова."
                        isErrorShow.value = true
                    })

                queue.add(stringRequest)

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
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
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