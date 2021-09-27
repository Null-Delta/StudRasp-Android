package com.zednull.studrasp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zednull.studrasp.ui.theme.TimeTableTheme

@Composable
fun TImeTableSettingsView(tables: MutableState<SavedTables>, navigation: NavHostController) {

    var name = remember { mutableStateOf(tables.value.selectedTable().name) }
    var firstWeek = remember { mutableStateOf(tables.value.selectedTable().firstWeek) }
    var secondWeek = remember { mutableStateOf(tables.value.selectedTable().secondWeek) }

    Column(
        modifier = Modifier.background(MaterialTheme.colors.background, RectangleShape)
    ) {
        Row(
            modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 32.dp)
        ){
            TextButton(onClick = {
                navigation.popBackStack()
            }) {
                Text(
                    text = "Отмена",
                    color = MaterialTheme.colors.primary,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true))

            TextButton(
                onClick = {
                    tables.value.selectedTable().name = name.value
                    tables.value.selectedTable().firstWeek = firstWeek.value
                    tables.value.selectedTable().secondWeek = secondWeek.value
                    navigation.popBackStack()
                },
                enabled = isCanSave(
                    name.value,
                    firstWeek.value,
                    secondWeek.value
                )
            ) {
                Text(
                    text = "Сохранить",
                    color = if(isCanSave(
                            name.value,
                            firstWeek.value,
                            secondWeek.value
                        )) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Text(
            text = "Настройки",
            color = MaterialTheme.colors.primary,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier.padding(16.dp,0.dp,16.dp,0.dp)
        )

        Text(
            text = "Название",
            color = MaterialTheme.colors.primary,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(16.dp,16.dp,16.dp,8.dp)
        )

        InputEditText(value = name.value, onValueChange = {
            name.value = it
        }, modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
            .padding(8.dp, 0.dp, 8.dp, 0.dp)

        )

        Text(
            text = "Первая неделя",
            color = MaterialTheme.colors.primary,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(16.dp,16.dp,16.dp,8.dp)
        )

        InputEditText(value = firstWeek.value, onValueChange = {
            firstWeek.value = it
        }, modifier = Modifier
            .fillMaxWidth()
            //.weight(1f, true)
            .height(42.dp)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
            .padding(8.dp, 0.dp, 8.dp, 0.dp)

        )

        Text(
            text = "Вторая неделя",
            color = MaterialTheme.colors.primary,
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(16.dp,16.dp,16.dp,8.dp)
        )

        InputEditText(value = secondWeek.value, onValueChange = {
            secondWeek.value = it
        }, modifier = Modifier
            .fillMaxWidth()
            //.weight(1f, true)
            .height(42.dp)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.medium)
            .padding(8.dp, 0.dp, 8.dp, 0.dp)

        )

        TextButton(
            onClick = {
                      navigation.navigate("time_settings")
            },
            modifier = Modifier
                .padding(16.dp,32.dp,16.dp,0.dp)
                .height(42.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary, MaterialTheme.shapes.medium)
        ) {
            Text(
                text = "Время пар",
                color = MaterialTheme.colors.background,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,

            )
        }

        Spacer(modifier = Modifier.fillMaxHeight().weight(1f,true))
    }
}

fun isCanSave(name: String, first: String, second: String): Boolean {
    return name.replace(" ", "").isNotEmpty()
}

@Composable
@Preview
fun TImeTableSettingsPreview() {
    TimeTableTheme {
        //var struct = remember { mutableStateOf(emptyTimeTable) }

        //struct.value.name = "test"
        //TImeTableSettingsView(struct)
    }
}