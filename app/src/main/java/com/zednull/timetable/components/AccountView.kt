package com.zednull.timetable.components.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.timetable.AccountActivity
import com.zednull.timetable.RegistrationActivity
import com.zednull.timetable.structure.ServerTimeTable
import com.zednull.timetable.structure.emptyTimeTable
import com.zednull.timetable.structure.user
import com.zednull.timetable.ui.theme.TimeTableTheme

@Composable
fun AccountView(activity: AccountActivity) {
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

    var user = remember { mutableStateOf(
        Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE).getString("user", Gson().toJson(
            user("", "")
        )), user::class.java)
    ) }

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

            if(user.value.session != "") {
                TextButton(onClick = {

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
                    context.startActivity(Intent(context, RegistrationActivity::class.java))
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
    }
}

@Preview (showBackground = true)
@Composable
fun AccountViewPreview() {
    TimeTableTheme {

    }
}