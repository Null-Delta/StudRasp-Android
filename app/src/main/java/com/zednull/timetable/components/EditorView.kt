package com.zednull.timetable.components

import android.text.format.Time
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.zednull.timetable.components.ui.SavedTables
import com.zednull.timetable.components.ui.globalTablesInfo
import com.zednull.timetable.components.ui.savedTimeTableInfo
import com.zednull.timetable.structure.TimeTableStructure
import com.zednull.timetable.ui.theme.TimeTableTheme

@Composable
fun EditorView(tables: MutableState<SavedTables>) {
    Column {
        Row {

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