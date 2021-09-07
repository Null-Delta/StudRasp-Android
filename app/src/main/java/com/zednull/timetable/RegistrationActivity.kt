package com.zednull.timetable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zednull.timetable.components.InputEditText
import com.zednull.timetable.ui.theme.TimeTableTheme


class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RegistrationView(this, false)
                }
            }
        }
    }
}

@Composable
fun RegistrationView(activity: RegistrationActivity, isLogining: Boolean) {
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
    var login = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            Modifier.padding(16.dp)
        ) {
            TextButton(onClick = {
                activity.finish()
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

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))

            TextButton(onClick = {

            },
            ) {
                Text(
                    text = if (isLogining) "Войти" else "Регистрация",
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        }

        Text(
            text = "Логин",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxWidth()
        )

        InputEditText(value = login.value, onValueChange = {
            login.value = it
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
        )

        Spacer(modifier = Modifier.fillMaxSize().weight(1f,true))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    TimeTableTheme {
    }
}