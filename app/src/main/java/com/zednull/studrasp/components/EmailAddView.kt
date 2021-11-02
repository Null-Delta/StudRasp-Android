package com.zednull.studrasp.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.zednull.studrasp.R
import com.zednull.studrasp.structure.mainDomain
import com.zednull.studrasp.structure.requestStruct
import com.zednull.studrasp.structure.user
import com.zednull.studrasp.ui.theme.TimeTableTheme
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
    different %= daysInMilli
    val elapsedHours = different / hoursInMilli
    different %= hoursInMilli
    val elapsedMinutes = different / minutesInMilli
    different %= minutesInMilli
    val elapsedSeconds = different / secondsInMilli
    return if (elapsedDays != 0.toLong() || elapsedHours != 0.toLong() || elapsedMinutes != 0.toLong())
        60
    else
        elapsedSeconds.toInt()
}



@Composable
fun EmailAddView(navigation: NavHostController, user: MutableState<user>) {
    val systemController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val barColor = MaterialTheme.colors.background

    val context = LocalContext.current
    val isAccepted = remember { mutableStateOf(0)}
    Fuel.post("https://$mainDomain/main.php", listOf(
        "action" to "check_account_confirmation",
        "login" to user.value.login,
        "session" to user.value.session))
        .responseString { _, _, result ->
            val request: requestStruct = Gson().fromJson(result.get(),
                requestStruct::class.java)
            when (request.error.code) {
                10 -> {
                    isAccepted.value = 1
                }
                0 -> {
                    isAccepted.value = 2
                }
                else -> {
                    //error
                }
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
            IconButton(
                onClick = {
                    navigation.popBackStack()
                },
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_btn),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
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

            val timer = remember { mutableStateOf(-1)}

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
                    timer.value = 0
                }
            }

            TextButton(
                onClick = {
                    Fuel.post("https://$mainDomain/main.php", listOf(
                        "action" to "send_confirmation_email",
                        "login" to user.value.login,
                        "session" to user.value.session))
                        .responseString { _, _, result ->
                            val request: requestStruct = Gson().fromJson(result.get(),
                                requestStruct::class.java)
                            if (request.error.code == 0)
                            {
                                timer.value = 60
                                val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
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
        val context = LocalContext.current
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