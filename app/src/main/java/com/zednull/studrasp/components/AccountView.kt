package com.zednull.studrasp.components

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.zednull.studrasp.RegistrationActivity
import com.zednull.studrasp.structure.user
import com.zednull.studrasp.ui.theme.TimeTableTheme

@Composable
fun AccountView(navigation: NavHostController, user: MutableState<user>) {
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

    val context = LocalContext.current


    val registration = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartActivityForResult()
        , onResult ={
            if(it.resultCode == 10) {
                user.value = user(it.data!!.getStringExtra("login")!!, it.data!!.getStringExtra("session")!!, it.data!!.getStringExtra("email")!!)
                val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                editor.putString("user", Gson().toJson(user.value))
                editor.apply()
            }
        }
    )

    val isExitDialog = remember { mutableStateOf(false) }

    if(isExitDialog.value) {
        AlertDialog(
            onDismissRequest = { isExitDialog.value = false },
            title = { Text(
                text = "Выход",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary
            ) },
            text = {
                Text(
                    text = "Вы уверены, что хотите выйти?",
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isExitDialog.value = false
                    }
                ) {
                    Text(
                        text = "Отмена",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primary
                    )
                }

                TextButton(
                    onClick = {
                        isExitDialog.value = false
                        user.value = user("","", "")
                        val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                        editor.putString("user", Gson().toJson(user.value))
                        editor.apply()
                    }
                ) {
                    Text(
                        text = "Выйти",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primary)
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier.background(MaterialTheme.colors.background, MaterialTheme.shapes.medium)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            Modifier.padding(16.dp)
        ) {
            TextButton(onClick = {
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

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))

            if(user.value.session != "") {
                TextButton(onClick = {
                    isExitDialog.value = true
                },
                ) {
                    Text(
                        text = "Выйти",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                }
            }
        }

        if(user.value.session == "") {
            Text(
                text = "Войдите или зарегистрируйте аккаунт, чтобы иметь возможность создавать и делиться расписаниями.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, true)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .height(52.dp)
            ) {
                TextButton(onClick = {
                    val intent = Intent(context, RegistrationActivity::class.java)
                    intent.putExtra("isLogining", true)
                    registration.launch(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary, MaterialTheme.colors.primary),

                ) {
                    Text(
                        text = "Вход",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = {
                    val intent = Intent(context, RegistrationActivity::class.java)
                    intent.putExtra("isLogining", false)
                    registration.launch(intent)
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary, MaterialTheme.colors.primary)

                ) {
                    Text(
                        text = "Регистрация",
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
        else {
            Text(
                text = user.value.login,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary
            )

            TextButton(onClick = {
                navigation.navigate("emailAdd" )
            },
                modifier = Modifier
                    .padding(16.dp,0.dp,16.dp,32.dp)
                    .background(MaterialTheme.colors.onPrimary, MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .height(42.dp)
            ) {
                Text(
                    text = "Почта",
                    fontSize = 14.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun AccountViewPreview() {
    TimeTableTheme {

    }
}