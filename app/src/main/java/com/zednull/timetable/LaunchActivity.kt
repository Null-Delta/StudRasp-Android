package com.zednull.timetable

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi

class LaunchActivity : Activity() {
    @OptIn(ExperimentalPagerApi::class)
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//
//            val systemController = rememberSystemUiController()
//            val useDarkIcons = MaterialTheme.colors.isLight
//            val barColor = MaterialTheme.colors.background
//
//            SideEffect {
//                systemController.setNavigationBarColor(
//                    barColor,useDarkIcons
//                )
//                systemController.setStatusBarColor(
//                    barColor,useDarkIcons
//                )
//            }
//        }

        startActivity(Intent(baseContext, MainActivity::class.java))
        finish()
    }
}