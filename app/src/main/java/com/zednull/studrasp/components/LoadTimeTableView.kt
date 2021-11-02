package com.zednull.studrasp.components

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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kittinunf.fuel.Fuel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.studrasp.structure.TimeTableStructure
import com.zednull.studrasp.structure.mainDomain
import com.zednull.studrasp.structure.requestStruct
import com.zednull.studrasp.ui.theme.TimeTableTheme

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