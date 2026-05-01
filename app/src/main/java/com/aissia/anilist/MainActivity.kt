package com.aissia.anilist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aissia.anilist.presentation.navigation.AppNavHost
import com.aissia.anilist.ui.theme.AnilistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnilistTheme {
                AppNavHost()
            }
        }
    }
}
