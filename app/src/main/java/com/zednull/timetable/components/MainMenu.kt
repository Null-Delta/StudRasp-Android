package com.zednull.timetable.components

//import androidx.navigation.NavController
//import androidx.navigation.NavHost
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
import android.content.Context
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
import com.zednull.timetable.R
import com.zednull.timetable.structure.emptyTimeTable
import com.zednull.timetable.ui.theme.TimeTableTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.zednull.timetable.components.ui.AccountView
import com.zednull.timetable.structure.TimeTableStructure
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*


@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun MainMenu(date: Date, selectedDay: MutableState<Int>) {
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

    val navController = rememberNavController()

    val savedTimeTable = remember {
        mutableStateOf(
            Gson().fromJson(context.getSharedPreferences("preferences", Context.MODE_PRIVATE).getString("timetable", Gson().toJson(
                emptyTimeTable
            )), TimeTableStructure::class.java)
        )
    }

    Scaffold(
        bottomBar = { bottomBar(navController = navController, menu = menu) }
    ) {
        Navigation(navController = navController, date, savedTimeTable, selectedDay, it)
    }
}

@ExperimentalPagerApi
@OptIn(InternalCoroutinesApi::class)
@Composable
fun Navigation(navController: NavHostController, date: Date, table: MutableState<TimeTableStructure>, day: MutableState<Int>, paddingValues: PaddingValues) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues = paddingValues)
            ) {
                TimeTableView(date, table, day, paddingValues)
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, true)
                        .background(Color.Transparent))
                    WeekView(date,  day)
                }
            }
        }
        composable("settings") {
            SettingsNavigation(paddingValues)
        }

        composable("account") {
            AccountView(navController)
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
                    menu.value = 0
                    navController.navigate("home") {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
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
                    menu.value = 1
                    navController.navigate("settings") {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
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

@InternalCoroutinesApi
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    val state = remember { mutableStateOf(2) }
    TimeTableTheme {
        MainMenu(Date(), state)
    }
}