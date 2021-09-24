package com.zednull.timetable.components.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.navigation.compose.rememberNavController
import com.github.kittinunf.fuel.Fuel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.timetable.structure.mainDomain
import com.zednull.timetable.structure.requestStruct
import com.zednull.timetable.structure.user
import com.zednull.timetable.ui.theme.TimeTableTheme
import kotlinx.coroutines.delay
import java.util.*


fun printDifference(startDate: Date, endDate: Date): Int {
    //milliseconds
    var different = endDate.time - startDate.time
    println("startDate : $startDate")
    println("endDate : $endDate")
    println("different : $different")
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val elapsedDays = different / daysInMilli
    different = different % daysInMilli
    val elapsedHours = different / hoursInMilli
    different = different % hoursInMilli
    val elapsedMinutes = different / minutesInMilli
    different = different % minutesInMilli
    val elapsedSeconds = different / secondsInMilli
    if (elapsedDays != 0.toLong() || elapsedHours != 0.toLong() || elapsedMinutes != 0.toLong())
        return 60
    else
        return elapsedSeconds.toInt()
}



@Composable
fun EmailAddView(navigation: NavHostController, user: MutableState<user>) {
    val systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val barColor = MaterialTheme.colors.background

    val context = LocalContext.current
    var isAccepted = remember { mutableStateOf(0)}
    Fuel.post("https://$mainDomain/main.php", listOf(
        "action" to "check_account_confirmation",
        "login" to user.value.login,
        "session" to user.value.session))
        .responseString { request, response, result ->
            var request: requestStruct = Gson().fromJson(result.get(),
                requestStruct::class.java)
            if (request.error.code == 10)
            {
                isAccepted.value = 1
            }
            else if (request.error.code == 0)
            {
                isAccepted.value = 2
            }
            else
            {
                //error
            }

        }

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
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .padding(16.dp, 16.dp, 16.dp, 13.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
            )
        }

        Text(
            text = "Почта",
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 31.sp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = user.value.email,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary
        )

        if (isAccepted.value == 1) {
            Text(
                text = "Ваша почта не подтверждена, проверьте свой почтовй ящик на наличие письма.",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = "Если же письмо не пришло, то нажмите на кнопку ниже, чтобы отправить письмо повторно.",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(16.dp, 0.dp, 16.dp, 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.primary
            )

            var timer = remember { mutableStateOf(-1)};

            LaunchedEffect(key1 = timer.value) {
                if (timer.value == -1 && Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        .getString("timeToSend", ""), Date()::class.java) != null)
                {
                    timer.value = 60 -
                        printDifference(Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                            .getString("timeToSend", ""), Date()::class.java), Date())
                }
                else if (timer.value > 0) {
                    delay(1000)
                    timer.value--
                }
                else
                {
                    timer.value = 0;
                }
            }

            TextButton(
                onClick = {

                          //отправление письма подтверждения
                    Fuel.post("https://$mainDomain/main.php", listOf(
                        "action" to "send_confirmation_email",
                        "login" to user.value.login,
                        "session" to user.value.session))
                        .responseString { request, response, result ->
                            var request: requestStruct = Gson().fromJson(result.get(),
                                requestStruct::class.java)
                            if (request.error.code == 0)
                            {
                                timer.value = 60
                                var editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                                editor.putString("timeToSend", Gson().toJson(Date()))
                                editor.apply()
                            }
                            else
                            {
                                //error
                            }

                        }
                },
                modifier = Modifier
                    .padding(16.dp, 0.dp, 16.dp, 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                enabled = timer.value == 0
                    //.background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
            ) {
                Text(
                    text = if (timer.value > 0) timer.value.toString() else "Отправить повторно",
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
            }

        }
        else if (isAccepted.value == 2)
        {
            Text(
                text = "Почта подтверждена",
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 0.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun EmailAddViewPreview() {
    TimeTableTheme {
        var context = LocalContext.current
        val user = remember {
            mutableStateOf(
                Gson().fromJson(
                    context.getSharedPreferences("preferences", Context.MODE_PRIVATE).getString(
                        "user", Gson().toJson(
                            user("", "", "")
                        )
                    ), user::class.java
                )
            )
        }

        val navController = rememberNavController()
        EmailAddView(navController, user)
    }
}
/*
else if ($action == check_account_confirmation && $login != null && $session != null)
{
    if ($mysqli->query("SELECT * FROM users WHERE login = '$login'")->num_rows == 0)
    {
        print("{\"error\":{\"code\":2,\"message\":\"$error_messages_2\"}}");
    }
    else if ($mysqli->query("SELECT * FROM users WHERE login = '$login' AND JSON_SEARCH(`sessions`, 'one', \"".hash('sha256', $session)."\") IS NOT NULL")->num_rows == 0)
    {
        print("{\"error\":{\"code\":4,\"message\":\"$error_messages_4\"}}");
    }
    else if ($mysqli->query("SELECT * FROM users WHERE login = '$login'")->fetch_array()["auth_code"] != "0")
    {
        print("{\"error\":{\"code\":10,\"message\":\"$error_messages_10\"}}");
    }
    else
    {
        print("{\"error\":{\"code\":0,\"message\":\"\"},\"session\":\"$session\",\"email\":\"".$mysqli->query("SELECT * FROM users WHERE login = '$login'")->fetch_array()["email"]."\"}");
    }
}
 */