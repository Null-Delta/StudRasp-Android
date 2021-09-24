package com.zednull.timetable.components

import android.text.format.Time
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zednull.timetable.R
import com.zednull.timetable.components.ui.MyTimeTableState
import com.zednull.timetable.components.ui.SavedTables
import com.zednull.timetable.components.ui.globalTablesInfo
import com.zednull.timetable.components.ui.savedTimeTableInfo
import com.zednull.timetable.structure.TimeTableStructure
import com.zednull.timetable.ui.theme.TimeTableTheme


fun getTableName(tables: MutableState<SavedTables>): String {
    if(tables.value.selectedType == MyTimeTableState.local) {
        return tables.value.localTables[tables.value.selectedTable].name
    } else if(tables.value.isLoad(tables.value.globalTables[tables.value.selectedTable].id)) {
        return tables.value.globalSavedTables.first {
            it.id.toString() == tables.value.globalTables[tables.value.selectedTable].id
        }.table.name
    } else {
        return tables.value.globalTables[tables.value.selectedTable].name
    }
}
@Composable
fun EditorView(tables: MutableState<SavedTables>) {
    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)

        ) {
            IconButton(
                onClick = { /*TODO*/ },
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
                onClick = { /*TODO*/ },
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

        Column {
            //TimeTabl
        }
    }
}

@Composable
@Preview
fun EditorViewPreview() {
    var tables = remember { mutableStateOf(
        SavedTables(
            listOf<globalTablesInfo>().toMutableStateList(),
            listOf<TimeTableStructure>().toMutableStateList(),
            listOf<savedTimeTableInfo>().toMutableStateList()
        )
    )}
    TimeTableTheme {
        EditorView(tables = tables)
    }
}