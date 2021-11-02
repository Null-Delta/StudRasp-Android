package com.zednull.studrasp.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zednull.studrasp.R
import com.zednull.studrasp.structure.TimeTableStructure
import com.zednull.studrasp.structure.emptyTimeTable
import com.zednull.studrasp.structure.user
import com.zednull.studrasp.ui.theme.TimeTableTheme

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
            text = "v1.0 (beta)",
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

@OptIn(ExperimentalPagerApi::class)
@ExperimentalMaterialApi
@Composable
fun SettingsNavigation(paddingValues: PaddingValues, code: MutableState<String>) {
    val navController = rememberNavController()

    var context = LocalContext.current

    val user = remember { mutableStateOf(
        Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE).getString("user", Gson().toJson(
            user("", "", "")
        )), com.zednull.studrasp.structure.user::class.java)
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
            MyTimeTableView(navController, user, tables, code)
        }
        composable("emailAdd") {
            EmailAddView(navController, user)
        }
        composable("editor") {
            EditorView(navController, tables, paddingValues)
        }
        composable("editor_settings") {
            TImeTableSettingsView(tables, navController)
        }
        composable("time_settings") {
            TimeSettingsView(navController, tables)
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