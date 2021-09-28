package com.zednull.studrasp.components

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zednull.studrasp.R
import com.zednull.studrasp.structure.emptyTimeTable
import com.zednull.studrasp.ui.theme.TimeTableTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.studrasp.AddTableActivity
import com.zednull.studrasp.structure.TimeTableStructure
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*


@ExperimentalMaterialApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun MainMenu(date: Date, selectedDay: MutableState<Int>, loadCode: String) {
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
    val menu = remember { mutableStateOf(0) }
    var code = remember { mutableStateOf(loadCode) }


    val navController = rememberNavController()

    val savedTimeTable = remember {
        mutableStateOf(
            Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE).getString("timetable", Gson().toJson(
                emptyTimeTable
            )), TimeTableStructure::class.java)
        )
    }

    val loadRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null) {
            if(result.resultCode == 1) {
                context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = context.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                editor.putString("timetable", result.data!!.getStringExtra("timetable"))
                editor.apply()

                savedTimeTable.value = Gson().fromJson(result.data!!.getStringExtra("timetable"), TimeTableStructure::class.java)
                Log.i("test", (savedTimeTable.value.TableID.toString()))
            }
        }
    }

    LaunchedEffect(key1 = code.value) {
        if(code.value != "") {
            var intent = Intent(context, AddTableActivity::class.java)
            intent.putExtra("code", code.value)
            loadRequest.launch(intent)
            code.value = ""
        }
    }

    Scaffold(
        bottomBar = { bottomBar(navController = navController, menu = menu) }
    ) {
        Navigation(navController = navController, date, savedTimeTable, selectedDay, it, code)
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@OptIn(InternalCoroutinesApi::class)
@Composable
fun Navigation(navController: NavHostController, date: Date, table: MutableState<TimeTableStructure>, day: MutableState<Int>, paddingValues: PaddingValues, code: MutableState<String>) {
    NavHost(navController, startDestination = "home") {
        composable("home") {

            val pagerState = rememberPagerState(
                pageCount = 7,
                day.value,0f,8,false
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues)
            ) {
                TimeTableView(date, table, pagerState, paddingValues)
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, true)
                        .background(Color.Transparent))
                    WeekView(date,  pagerState)
                }
            }
        }
        composable("settings") {
            SettingsNavigation(paddingValues, code)
        }
    }
}

@Composable
fun bottomBar(navController: NavController, menu: MutableState<Int>) {
    Box() {

        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,

            ) {
            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_homeicon), contentDescription = null) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSecondary,
                alwaysShowLabel = false,
                selected = menu.value == 0,
                onClick = {
                    if(menu.value != 0) {
                        menu.value = 0
                        navController.navigate("home") {
                            navController.enableOnBackPressed(false)
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(id = R.drawable.ic_categoryicon), contentDescription = null) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSecondary,
                alwaysShowLabel = false,
                selected = menu.value == 1,
                onClick = {
                    if (menu.value != 1) {
                        menu.value = 1
                        navController.navigate("settings")
                        {
                            navController.enableOnBackPressed(false)
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        Divider(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
        )
    }

}

@ExperimentalMaterialApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    val state = remember { mutableStateOf(2) }
    TimeTableTheme {
        MainMenu(Date(), state, "")
    }
}