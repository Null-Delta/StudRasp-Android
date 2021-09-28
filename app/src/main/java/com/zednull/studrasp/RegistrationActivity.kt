package com.zednull.studrasp

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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kittinunf.fuel.Fuel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.studrasp.components.InputEditText
import com.zednull.studrasp.structure.mainDomain
import com.zednull.studrasp.structure.requestStruct
import com.zednull.studrasp.ui.theme.TimeTableTheme


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
                            Log.i("test", result.get())
                            val request: requestStruct = Gson().fromJson(result.get(),
                                requestStruct::class.java)
                            if(request.error.code != 0) {
                                dialogErrorText.value = request.error.message
                                isErrorShow.value = true
                            } else {
                                Log.i("test", "finish")

                                val data = Intent()
                                data.putExtra("login", request.login!!)
                                data.putExtra("session", request.session!!)
                                data.putExtra("email", request.email!!)
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
                            var request: requestStruct = Gson().fromJson(result.get(),
                                requestStruct::class.java)
                            if(request.error.code != 0) {
                                dialogErrorText.value = request.error.message
                                isErrorShow.value = true
                            } else {
                                val data = Intent()
                                data.putExtra("login", login.value)
                                data.putExtra("session", request.session!!)
                                data.putExtra("email", request.email!!)
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

        InputEditText(
            value = login.value,
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(42.dp)
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
            onValueChange = {
                login.value = it
            },
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
        InputEditText(
            visualTransformation = PasswordVisualTransformation(),
            value = password.value,
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(42.dp)
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
            onValueChange = {
                password.value = it
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                KeyboardCapitalization.None,
                false,
                KeyboardType.Password,
                ImeAction.Default
            ),
        )

        Text(
            text = errorText.value,
            color = Color.Red,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

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
    } else if (!emailExt.matches(email.lowercase()) && email.count() != 0) {
        error.value = "Введена некорректная почта"
        return true
    } else if (!passExt.matches(pass) && pass.count() != 0) {
        error.value = "Введен некорректный пароль. Пароль должен быть длиной минимум 8 символов, содержать заглавные и прописные буквы и цифры"
        return true
    } else {
        error.value = ""
    }

    return false
}

val emailExt = """([a-z0-9.])+@([a-z0-9.])+\.([a-z0-9]){2,}""".toRegex()
val passExt = """(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[0-9a-zA-Z!@#$-_%^&*]{8,}""".toRegex()


@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    TimeTableTheme{
    }
}