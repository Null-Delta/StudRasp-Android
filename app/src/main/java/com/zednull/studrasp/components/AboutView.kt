package com.zednull.studrasp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zednull.studrasp.R
import com.zednull.studrasp.ui.theme.TimeTableTheme

@Composable
fun AboutView(navController: NavController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .verticalScroll(ScrollState(0), true, null, false)

    ) {
        Row(modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 16.dp)
            .fillMaxWidth()
            .height(42.dp)
        ) {
            Text(
                text = "О Приложении",
                fontSize = 32.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,

                )
        }
        Text(
            text = "В очередной раз увидев, как деканат вывешивает новое распечатанное расписание на доску, мы поняли, что так жить больше нельзя.",
            fontSize = 16.sp,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 16.dp)
                .fillMaxWidth()
            )

        Text(
            text = "Это стало причиной существования данной программы.",
            fontSize = 16.sp,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 16.dp)
                .fillMaxWidth()
        )
        Row(modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 16.dp)
            .fillMaxWidth()
        ) {
            Text(
                text = "Разработчики",
                fontSize = 32.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,

                )
        }
        Row(modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 16.dp)
            .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.rust),
                contentDescription = null,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
                    .height(68.dp)
                    .width(68.dp)
                    .clip(CircleShape))
            Column (modifier = Modifier
                .fillMaxWidth())
            {
                Text(
                    text = "Хахук Рустам",
                    fontSize = 20.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    )
                Text(
                    text = "zed.null@icloud.com",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    )
                Text(
                    text = "Разработка клиента на IOS,",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                    )
                Text(
                    text = "Разработка клиента на Android,",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                )
                Text(
                    text = "Дизайн приложения",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                )
            }
        }
        Row(modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 16.dp)
            .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ya),
                contentDescription = null,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
                    .height(68.dp)
                    .width(68.dp)
                    .verticalScroll(ScrollState(0), true, null, false)
            )
            Column (modifier = Modifier
                .fillMaxWidth())
            {
                Text(
                    text = "Гиренко Даниил",
                    fontSize = 20.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                )
                Text(
                    text = "iamgirya@yandex.ru",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                )
                Text(
                    text = "Разработка клиента на Android,",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                )
                Text(
                    text = "Разработка сервера",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                )
            }
        }
        Row(modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 16.dp)
            .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.serg),
                contentDescription = null,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
                    .height(68.dp)
                    .width(68.dp).clip(CircleShape)
            )
            Column (modifier = Modifier
                .fillMaxWidth())
            {
                Text(
                    text = "Прозоров Максим",
                    fontSize = 20.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                )
                Text(
                    text = "Разработка сервера",
                    fontSize = 16.sp,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutViewPreview() {
    TimeTableTheme{
        val navController = rememberNavController()
        AboutView(navController)
    }
}