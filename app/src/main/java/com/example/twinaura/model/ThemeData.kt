package com.example.twinaura.model

import androidx.compose.ui.graphics.Color
import com.example.R

enum class ThemeAesthetic(
    val title: String,
    val subtitle: String,
    val tag: String,
    val primaryColor: Color,
    val accentColor: Color,
    val surfaceColor: Color,
    val wallpaperRes: Int
) {
    NEON_RED_TECH(
        title = "Neon Red Cyber",
        subtitle = "Dark Tech & Anime Aesthetic",
        tag = "🔴 Boys Theme",
        primaryColor = Color(0xFFFF1744),
        accentColor = Color(0xFFFF5252),
        surfaceColor = Color(0xFF101018),
        wallpaperRes = R.drawable.neon_red_theme_1790890201591
    ),
    PASTEL_PINK_CUTE(
        title = "Pastel Pink Dream",
        subtitle = "Soft Cute & Cloud Aesthetic",
        tag = "🌸 Girls Theme",
        primaryColor = Color(0xFFFF8DA1),
        accentColor = Color(0xFFFFB6C1),
        surfaceColor = Color(0xFFFFF0F5),
        wallpaperRes = R.drawable.pastel_pink_theme_1790890214972
    )
}

enum class ComparisonMode(val label: String, val iconText: String) {
    SIDE_BY_SIDE("Dual Phones", "📱📱"),
    WIPE_SLIDER("Split Slider", "↔️"),
    POSTER_8K("8K Mockup", "🖼️")
}

data class LauncherApp(
    val id: String,
    val name: String,
    val category: String,
    val iconName: String,
    val badge: String? = null,
    val neonColor: Color,
    val pastelColor: Color
)

object ThemeRepository {
    val masterMockupRes = R.drawable.dual_theme_mockup_1790890187903

    val neonApps = listOf(
        LauncherApp("phone", "Terminal", "Comm", "phone", "3", Color(0xFFFF1744), Color(0xFFFF9BB2)),
        LauncherApp("chat", "ShadowMsg", "Social", "chat", "99+", Color(0xFFFF3D00), Color(0xFFFFB6C1)),
        LauncherApp("camera", "CyberOptic", "Media", "camera", "4K", Color(0xFFFF1744), Color(0xFFFFC0CB)),
        LauncherApp("game", "MechaZone", "Gaming", "game", "LIVE", Color(0xFFFF2A55), Color(0xFFFFB0BE)),
        LauncherApp("anime", "AnimeVoid", "Stream", "stream", "NEW", Color(0xFFFF003C), Color(0xFFFF94A8)),
        LauncherApp("music", "SynthWave", "Audio", "music", null, Color(0xFFFF3366), Color(0xFFFF8DA1)),
        LauncherApp("browser", "NetRunner", "System", "web", null, Color(0xFFFF1744), Color(0xFFFFAEC9)),
        LauncherApp("maps", "Radar3D", "Nav", "map", "GPS", Color(0xFFFF5252), Color(0xFFFFB3C6)),
        LauncherApp("vault", "SecuredKey", "Security", "lock", null, Color(0xFFFF0033), Color(0xFFFF99AA)),
        LauncherApp("settings", "CoreSys", "Settings", "settings", null, Color(0xFFFF1744), Color(0xFFFFB6C1)),
        LauncherApp("discord", "CyberGuild", "Social", "chat_bubble", "12", Color(0xFFFF2A55), Color(0xFFFF98AC)),
        LauncherApp("stats", "Telemetry", "Tools", "monitor", "HOT", Color(0xFFFF1744), Color(0xFFFFADC0))
    )

    val pastelApps = listOf(
        LauncherApp("phone", "Sweet Talk", "Comm", "phone", "♡", Color(0xFFFF1744), Color(0xFFFF85A1)),
        LauncherApp("chat", "Boba Chat", "Social", "chat", "5", Color(0xFFFF3D00), Color(0xFFFF9BB2)),
        LauncherApp("camera", "Pastel Cam", "Media", "camera", "✨", Color(0xFFFF1744), Color(0xFFFFB6C1)),
        LauncherApp("diary", "My Journal", "Personal", "edit_note", null, Color(0xFFFF2A55), Color(0xFFFFAEC9)),
        LauncherApp("cafe", "Bunny Cafe", "Lifestyle", "bakery", "🧁", Color(0xFFFF003C), Color(0xFFFF94A8)),
        LauncherApp("music", "Lofi Melodies", "Audio", "music", null, Color(0xFFFF3366), Color(0xFFFF8DA1)),
        LauncherApp("browser", "Cloud Safari", "System", "web", null, Color(0xFFFF1744), Color(0xFFFFB0BE)),
        LauncherApp("photos", "Sweet Memories", "Gallery", "photo", "♡", Color(0xFFFF5252), Color(0xFFFF99B3)),
        LauncherApp("notes", "Love Notes", "Productivity", "favorite", null, Color(0xFFFF0033), Color(0xFFFF85A1)),
        LauncherApp("settings", "Preferences", "Settings", "settings", null, Color(0xFFFF1744), Color(0xFFFFB6C1)),
        LauncherApp("planner", "Cute Routine", "Wellness", "calendar", "🌸", Color(0xFFFF2A55), Color(0xFFFF98AC)),
        LauncherApp("store", "Sticker Shop", "Shop", "shopping_bag", "NEW", Color(0xFFFF1744), Color(0xFFFFADC0))
    )
}
