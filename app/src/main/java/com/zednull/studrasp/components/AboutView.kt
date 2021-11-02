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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
fun AboutView(navigation: NavHostController) {
    Text(text = "sada")
}

@Preview(showBackground = true)
@Composable
fun AboutView() {
    TimeTableTheme {
        val navController = rememberNavController()
        AboutView(navController)
    }
}