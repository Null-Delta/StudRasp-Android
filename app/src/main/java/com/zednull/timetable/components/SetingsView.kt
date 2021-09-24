package com.zednull.timetable.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zednull.timetable.AccountActivity
import com.zednull.timetable.R
import com.zednull.timetable.components.ui.*
import com.zednull.timetable.structure.TimeTableStructure
import com.zednull.timetable.structure.emptyTimeTable
import com.zednull.timetable.structure.user
import com.zednull.timetable.ui.theme.TimeTableTheme

@Composable
fun SettingsView(navController: NavController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.appicon),
            contentDescription = null,
        modifier = Modifier
            .padding(0.dp, 32.dp, 0.dp, 0.dp)
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
            navController.navigate("account", )
        },
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 32.dp)
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
                .padding(16.dp, 0.dp, 16.dp, 32.dp)
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

@Composable
fun SettingsNavigation(paddingValues: PaddingValues) {
    val navController = rememberNavController()

    var context = LocalContext.current

    val user = remember { mutableStateOf(
        Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE).getString("user", Gson().toJson(
            user("", "")
        )), com.zednull.timetable.structure.user::class.java)
    ) }

    var tables = remember { mutableStateOf( SavedTables (
        ( Gson().fromJson(
            context.getSharedPreferences(
                "preferences",
                Context.MODE_PRIVATE
            ).getString(
                "globalTables",
                Gson().toJson(List(0) { globalTablesInfo("", "", "") })
            ), object : TypeToken<SnapshotStateList<globalTablesInfo>>() { }.type
        )),
        ( Gson().fromJson(
            context.getSharedPreferences(
                "preferences",
                Context.MODE_PRIVATE
            ).getString(
                "localTables",
                Gson().toJson(List(0) { emptyTimeTable })
            ), object : TypeToken<SnapshotStateList<TimeTableStructure>>() { }.type
        )),
        (Gson().fromJson(
            context.getSharedPreferences(
                "preferences",
                Context.MODE_PRIVATE
            ).getString(
                "globalSaved",
                Gson().toJson(List(0) { savedTimeTableInfo(-1, emptyTimeTable, emptyTimeTable) })
            ), object : TypeToken<SnapshotStateList<savedTimeTableInfo>>() { }.type
        )
    )) ) }

    NavHost(
        navController,
        startDestination = "settings",
    modifier = Modifier.padding(paddingValues)
    ) {
        composable("settings") {
            SettingsView(navController)
        }
        composable("account") {
            AccountView(navController, user)
        }
        composable("myTimeTable") {
            MyTimeTableView(navController, user, tables)
        }
        composable("editor") {
            EditorView(navController, tables)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsViewPreview() {
    TimeTableTheme {
        //SettingsView()
    }
}