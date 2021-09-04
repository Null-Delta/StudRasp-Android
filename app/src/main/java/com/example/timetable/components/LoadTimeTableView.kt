package com.example.timetable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetable.ui.theme.TimeTableTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoadTimeTableView() {
    var systemController = rememberSystemUiController()
    val useDarkIcons = !MaterialTheme.colors.isLight
    var barColor = MaterialTheme.colors.background

    var code = remember { mutableStateOf("") }

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
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = code.value, onValueChange = {
                code.value = it.filter { "1234567890".contains(it) }
            },
            modifier = Modifier
                .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(64.dp)
                .weight(1f, true),
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary,
                    disabledTextColor = MaterialTheme.colors.onSecondary,
                    backgroundColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    KeyboardCapitalization.None,
                    false,
                    KeyboardType.Number,
                    ImeAction.Default
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            TextButton(onClick = {

            },
                modifier = Modifier.height(64.dp)
            ) {
                Text(text = "Добавить",
                    fontSize = 20.sp,
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


@Preview(showBackground = true)
@Composable
fun previewLoadTimeTableView() {
    TimeTableTheme {
        LoadTimeTableView()
    }
}