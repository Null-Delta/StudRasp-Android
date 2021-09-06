package com.zednull.timetable.components
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zednull.timetable.*
import com.zednull.timetable.ui.theme.TimeTableTheme
import java.util.*

@Composable
fun WeekView(date: Date,selected: MutableState<Int>) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .shadow(32.dp, MaterialTheme.shapes.large, true)
            .background(MaterialTheme.colors.background, MaterialTheme.shapes.large)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        repeat(7) {
            DayView(date = Date().firstDayOfWeek().addDays(it), it, selected, date.weekDayNum() - 1 == it, modifier = Modifier.weight(1f,true))
            if(it != 6) Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun DayView(date: Date,index: Int,selectedDay: MutableState<Int>, today: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .shadow(if (selectedDay.value == index) 4.dp else 0.dp, MaterialTheme.shapes.medium, true)
            .background(
                if (selectedDay.value == index) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                MaterialTheme.shapes.medium
            )
            .border(
                2.dp,
                if (selectedDay.value == index || today) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                MaterialTheme.shapes.medium
            )
            .width(42.dp)
            .height(52.dp)
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        TextButton(onClick = { selectedDay.value = index }) {
            Text(
                text = "${date.weekDayName()}\n${date.dayOfMounts()}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                color = if(selectedDay.value == index) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                fontSize = 14.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimeTableTheme {
        WeekView(Date(), mutableStateOf(0))
    }
}