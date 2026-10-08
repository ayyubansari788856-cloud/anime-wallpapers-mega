package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.InterstitialAdHelper
import com.example.ui.components.StartAppConfig
import com.example.wallpaper.data.WallpaperRepository
import com.example.wallpaper.ui.AnimeWallpaperScreen

class MainActivity : ComponentActivity() {
    private val wallpaperRepository by lazy { WallpaperRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Start.io Ads with Anime Wallpapers App ID 208180681
        StartAppConfig.initialize(this, StartAppConfig.DEFAULT_APP_ID)
        InterstitialAdHelper.loadAd(this)

        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF0F111A)
            ) {
                AnimeWallpaperScreen(repository = wallpaperRepository)
            }
        }
    }
}
