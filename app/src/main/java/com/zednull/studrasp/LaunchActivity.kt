package com.zednull.studrasp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalMaterialApi
class LaunchActivity : Activity() {
    @OptIn(ExperimentalPagerApi::class)
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(baseContext, MainActivity::class.java))
        finish()
    }
}