package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.ui.GameMainScreen
import com.example.wallpaper.data.WallpaperRepository
import com.example.wallpaper.ui.AnimeWallpaperScreen

enum class AppDestination(val title: String, val subtitle: String, val badge: String) {
    ANIME_WALLPAPERS("Anime 4K", "Wallpapers Hub", "🎌 4K"),
    MOTO_GAME("Dr. Riding", "Bike Racing Game", "🏍️ Moto"),
    AURA_EARNING("Aura Quest", "Tasks & Earnings", "⚡ Rewards")
}

@Composable
fun MasterHubScreen() {
    val context = LocalContext.current
    val wallpaperRepository = remember { WallpaperRepository(context) }

    // 0: Anime Wallpapers, 1: Moto Game, 2: Aura Tasks & Earnings
    var activeAppIndex by remember { mutableIntStateOf(0) }

    BackHandler(enabled = activeAppIndex != 0) {
        activeAppIndex = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14))
    ) {
        // Universal App Switcher Top Bar
        Surface(
            color = Color(0xFF131722),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Selector Buttons
                AppDestination.values().forEachIndexed { index, app ->
                    val isSelected = activeAppIndex == index
                    val activeBg = when (index) {
                        0 -> Color(0xFFFF5376)
                        1 -> Color(0xFFFF9800)
                        else -> Color(0xFF00E676)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) activeBg else Color(0xFF1A1F2C))
                            .clickable { activeAppIndex = index }
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .testTag("app_switcher_tab_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = app.badge.take(2),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text(
                                    text = app.title,
                                    color = if (isSelected) Color.White else Color.LightGray,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active App Screen Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (activeAppIndex) {
                0 -> AnimeWallpaperScreen(repository = wallpaperRepository)
                1 -> GameMainScreen()
                2 -> MainScreen()
            }
        }
    }
}
