package com.zednull.studrasp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.zednull.studrasp.R
import com.zednull.studrasp.dayOfWeek
import com.zednull.studrasp.ui.theme.TimeTableTheme
import java.util.*


fun getTableName(tables: MutableState<SavedTables>): String {
    return if(tables.value.selectedType == MyTimeTableState.local) {
        tables.value.localTables[tables.value.selectedTable].name
    } else {
        tables.value.globalSavedTables.first {
            it.id.toString() == tables.value.globalTables[tables.value.selectedTable].id
        }.table.name
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun EditorView(navigation: NavHostController, tables: MutableState<SavedTables>, paddingValues: PaddingValues) {
    
    val selectedTable = remember { mutableStateOf(tables.value.selectedTable()) }
    val selectedDay = rememberPagerState(pageCount = 7,0,0f,7,false)
    val date = Date()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val selectedLesson = remember { mutableStateOf(-1) }
    val selectedWeek = remember { mutableStateOf(-1) }
    val wasSheetClose = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = wasSheetClose.value) {
        if(wasSheetClose.value) {
            sheetState.hide()
            wasSheetClose.value = false
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column {
                TextButton(
                    onClick = {
                              tables.value.selectedTable().days[selectedDay.currentPage].changeLessons(date,selectedWeek.value) {
                                  it.removeAll { l ->
                                      l.lessonNumber == selectedLesson.value
                                  }
                              }
                        wasSheetClose.value = true
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Удалить"
                    )
                }

                TextButton(
                    onClick = {
                        wasSheetClose.value = true
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Отмена"
                    )
                }
            }
        },
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = Color(0,0,0,128)
    ) {
        Box {
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)

                ) {
                    IconButton(
                        onClick = {
                            tables.value.saveArray(tables.value.selectedType, context)
                            navigation.popBackStack()
                        },
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_close_24),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f))

                    Text(
                        text = getTableName(tables),
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .height(36.dp)
                    )

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f))


                    IconButton(
                        onClick = {
                            navigation.navigate("editor_settings")
                        },
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }

                TimeTable(
                    activeTable = selectedTable,
                    date = date,
                    isEditing = true,
                    dayIndex = date.dayOfWeek(),
                    pagerState = selectedDay,
                    sheetState,
                    selectedLesson,
                    selectedWeek
                )

            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, true)
                    .background(Color.Transparent))
                WeekView(date,  selectedDay)
            }
        }
    }
}

@Composable
@Preview
fun EditorViewPreview() {
//    var tables = remember { mutableStateOf(
//        SavedTables(
//            listOf<globalTablesInfo>().toMutableStateList(),
//            listOf<TimeTableStructure>().toMutableStateList(),
//            listOf<savedTimeTableInfo>().toMutableStateList()
//        )
//    )}
    TimeTableTheme {
        //EditorView(tables = tables,)
    }
}