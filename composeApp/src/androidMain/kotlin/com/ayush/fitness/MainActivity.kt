package com.ayush.fitness

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.core.logger.Logger
import android.util.Log
import androidx.compose.runtime.remember
import data.local.createDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(
                prefs = remember {
                    createDataStore(applicationContext)
                }
            )
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}
//


abstract class AndroidLogger : Logger() {
    fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }
}

