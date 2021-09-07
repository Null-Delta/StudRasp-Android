package com.zednull.timetable

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.timetable.components.InputEditText
import com.zednull.timetable.structure.mainDomain
import com.zednull.timetable.structure.requestStruct
import com.zednull.timetable.ui.theme.TimeTableTheme


class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RegistrationView(this, intent!!.getBooleanExtra("isLogining", false))
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

    val login = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorText = remember { mutableStateOf("") }

    var isErrorShow = remember { mutableStateOf(false) }
    var dialogErrorText = remember { mutableStateOf("") }

    if(isErrorShow.value) {
        AlertDialog(
            onDismissRequest = { isErrorShow.value = false },
            title = { Text(text = "Ошибка") },
            text = {
                Text(text = dialogErrorText.value)
            },
            confirmButton = {
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
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 16.dp)
    ) {
        Row() {
            TextButton(onClick = {
                activity.finish()
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

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))

            TextButton(onClick = {
                if(isLogining) {
                    Fuel.post("https://$mainDomain/main.php", listOf(
                        "action" to "authorization",
                        "login" to login.value,
                        "password" to password.value))
                        .responseString { _, _ , result ->
                            val request: requestStruct = jacksonObjectMapper().readValue(result.get(),
                                requestStruct::class.java)
                            if(request.error.code != 0) {
                                dialogErrorText.value = request.error.message
                                isErrorShow.value = true
                            } else {
                                val data = Intent()
                                data.putExtra("login", request.login!!)
                                data.putExtra("session", request.session!!)
                                activity.setResult(10, data)
                                activity.finish()
                            }
                        }
                } else {
                    Fuel.post("https://$mainDomain/main.php", listOf(
                        "action" to "registration",
                        "login" to login.value,
                        "email" to email.value,
                        "password" to password.value))
                        .responseString { request, response, result ->
                            Log.i("a",result.get())// response
                            var request: requestStruct = jacksonObjectMapper().readValue(result.get(),
                                requestStruct::class.java)
                            if(request.error.code != 0) {
                                dialogErrorText.value = request.error.message
                                isErrorShow.value = true
                            } else {
                                val data = Intent()
                                data.putExtra("login", login.value)
                                data.putExtra("session", request.session!!)
                                activity.setResult(10, data)
                                activity.finish()
                            }
                        }
                }
            },
                enabled = canRegister(login.value, email.value, password.value, errorText, isLogining),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.background,
                MaterialTheme.colors.primary, MaterialTheme.colors.background,
                MaterialTheme.colors.onSecondary)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 32.dp, 0.dp, 8.dp)
        )

        InputEditText(value = login.value, onValueChange = {
            login.value = it
        },
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(42.dp)
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                KeyboardCapitalization.None,
                false,
                KeyboardType.Text,
                ImeAction.Default
            ),
        )

        if(!isLogining) {
            Text(
                text = "Почта",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp, 0.dp, 8.dp)
            )

            InputEditText(value = email.value, onValueChange = {
                email.value = it
            },
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(8.dp, 0.dp, 8.dp, 0.dp),
                keyboardOptions = KeyboardOptions(
                    KeyboardCapitalization.None,
                    false,
                    KeyboardType.Email,
                    ImeAction.Default
                ),
                maxLines = 1,
            )
        }

        Text(
            text = "Пароль",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp, 0.dp, 8.dp)
        )

        InputEditText(value = password.value, onValueChange = {
            password.value = it
        },
            modifier = Modifier
                .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(42.dp)
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
            keyboardOptions = KeyboardOptions(
                KeyboardCapitalization.None,
                false,
                KeyboardType.Password,
                ImeAction.Default
            ),
            maxLines = 1,
        )

        Text(text = errorText.value)

        Spacer(modifier = Modifier
            .fillMaxSize()
            .weight(1f, true))
    }
}

fun canRegister(log: String, email: String, pass: String, error: MutableState<String>, isLog: Boolean): Boolean {
    return !checkErrors(log,email,pass,error, isLog) && log.count() != 0 && if(isLog) true else email.count() != 0 && pass.count() != 0
}

fun checkErrors(log: String, email: String, pass: String, error: MutableState<String>, isLogining: Boolean): Boolean {
    if(log.count() < 3 && log.count() != 0) {
        error.value = "Логин должен быть длиной больше 2х символов"
        return true
    } else if (!emailExt.matches(email) && email.count() != 0) {
        error.value = "Введена некорректная почта"
        return true
    } else if ((false//!passExt1.matches(pass) //||
         ) && pass.count() != 0) {
        error.value = "Введен некорректный пароль. Пароль должен быть длиной минимум 8 символов, сродержать заглавные, прописные буквы, цифры и спец. символ (!.-?#@)"
        return true
    } else {
        error.value = ""
    }

    return false
}

val emailExt = """([a-z0-9.])+@([a-z0-9.])+\.([a-z0-9]){2,}""".toRegex()
val passExt1 = """(.*([a-z])+)(.*([A-Z])+)(.*([0-9])+)(.*([.,()!@#%^&*])+)""".toRegex()
val passExt2 = """(.*([A-Z])+)""".toRegex()
val passExt3 = """(.*([0-9])+)""".toRegex()
val passExt4 = """(.*([.,()!@#%^&*])+)""".toRegex()
val passExt5 = """([a-zA-Z0-9.,()!@#%^&*]){8,}""".toRegex()


@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    TimeTableTheme {
    }
}