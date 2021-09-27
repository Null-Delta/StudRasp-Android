package com.zednull.studrasp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zednull.studrasp.ui.theme.TimeTableTheme
import com.zednull.studrasp.components.*
import com.zednull.studrasp.components.ui.PartEditorView


class LoadListOfPartsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTableTheme {
                val paraIsReady = remember {
                    mutableStateOf("") // должен вернуть все переменные, выбранные пользователем
                }

                val nameDisp = remember { mutableStateOf(intent.extras!!.getString("name", "")) }
                val namePrep = remember { mutableStateOf(intent.extras!!.getString("teacherName", "")) }
                val nameRoom = remember { mutableStateOf(intent.extras!!.getString("audience", "")) }
                val nameType = remember { mutableStateOf(intent.extras!!.getString("type", "")) }

                LaunchedEffect(key1 = paraIsReady.value) {
                    if (paraIsReady.value == "close")
                    {
                        finish()
                    }
                    else if(paraIsReady.value == "add") {
                        val data = Intent()
                        data.putExtra("name", nameDisp.value)
                        data.putExtra("teacherName", namePrep.value)
                        data.putExtra("audience", nameRoom.value)
                        data.putExtra("type", nameType.value)

                        setResult(1, data)
                        finish()
                    }
                    else
                    {
                        Log.e("LoadListOfPartsActivity","Unknown return")
                    }
                }


                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController,
                        startDestination = "base",
                    ) {
                        composable("base") {
                            PartEditorView(navController, paraIsReady, nameDisp , namePrep, nameRoom, nameType)
                        }
                        composable("edit1") {
                            LoadListOfPartsView(navController, nameDisp,1)
                        }
                        composable("edit2") {
                            LoadListOfPartsView(navController, namePrep,2)
                        }
                        composable("edit3") {
                            LoadListOfPartsView(navController, nameRoom,3)
                        }
                        composable("edit4") {
                            LoadListOfPartsView(navController, nameType,4)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadListOfPartsActivityPreview() {
    TimeTableTheme {
        // LoadTimeTableView()
    }
}