package com.zednull.timetable.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zednull.timetable.AccountActivity
import com.zednull.timetable.R
import com.zednull.timetable.ui.theme.TimeTableTheme

@Composable
fun SettingsView() {
    var context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.appicon),
            contentDescription = null,
        modifier = Modifier
            .padding(0.dp,32.dp,0.dp,0.dp)
            .height(128.dp)
            .width(128.dp)
            .verticalScroll(ScrollState(0), true, null, false)
        )

        Text(
            text = "StudRasp",
            fontSize = 24.sp,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
        )

        Text(
            text = "v0.9.6 (beta)",
            fontSize = 16.sp,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(0.dp,0.dp,0.dp,32.dp)
        )

        TextButton(onClick = {
            context.startActivity(Intent(context, AccountActivity::class.java))
        },
        modifier = Modifier
            .padding(16.dp,0.dp,16.dp,32.dp)
            .background(MaterialTheme.colors.onPrimary, MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .height(42.dp)
        ) {
            Text(
                text = "Аккаунт",
                fontSize = 14.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
        }

        TextButton(onClick = {

        },
            modifier = Modifier
                .padding(16.dp,0.dp,16.dp,32.dp)
                .background(MaterialTheme.colors.onPrimary, MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(42.dp)

        ) {
            Text(
                text = "О приложении",
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


@Preview(showBackground = true)
@Composable
fun SettingsViewPreview() {
    TimeTableTheme {
        SettingsView()
    }
}