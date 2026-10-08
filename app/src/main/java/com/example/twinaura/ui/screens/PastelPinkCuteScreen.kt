package com.example.twinaura.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.twinaura.model.LauncherApp
import com.example.twinaura.model.ThemeRepository

@Composable
fun PastelPinkCuteHomeScreen(
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    onAppClick: ((LauncherApp) -> Unit)? = null
) {
    val context = LocalContext.current
    var isPlayingMusic by remember { mutableStateOf(true) }
    var selectedApp by remember { mutableStateOf<LauncherApp?>(null) }
    var activeMood by remember { mutableStateOf("✨ Sparkle") }
    var isHeartLiked by remember { mutableStateOf(true) }

    // Floating heart and sparkle animation
    val infiniteTransition = rememberInfiniteTransition(label = "pastel_charm")
    val sparkleAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle_spin"
    )

    val gentleBounce by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heart_pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F5))
    ) {
        // Soft pastel dreamy cloud wallpaper
        Image(
            painter = painterResource(id = R.drawable.pastel_pink_theme_1790890214972),
            contentDescription = "Pastel Pink Cute Dreamy Clouds Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Delicate milky gradient overlay for dreamy softness
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x77FFF0F5),
                            Color(0x33FFD1DC),
                            Color(0x88FFE4E1)
                        )
                    )
                )
        )

        // Main Home Screen Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isCompact) 10.dp else 16.dp)
                .padding(top = if (isCompact) 28.dp else 36.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP SECTION: Cute Handwritten Clock & Heart-Shaped Widgets
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp)
            ) {
                // 1. Cute Handwritten Font Clock Widget
                CuteHandwrittenClockWidget(
                    sparkleAngle = sparkleAngle,
                    isCompact = isCompact
                )

                // 2. Heart-Shaped Daily Mood & Sparkle Affirmation Widget
                HeartShapedAffirmationWidget(
                    activeMood = activeMood,
                    onSelectMood = { mood ->
                        activeMood = mood
                        Toast.makeText(context, "Mood set to $mood ♡", Toast.LENGTH_SHORT).show()
                    },
                    pulseScale = gentleBounce,
                    isCompact = isCompact
                )

                // 3. Cute Pastel Music Player (shown if not super compact)
                if (!isCompact) {
                    CutePastelMusicWidget(
                        isPlaying = isPlayingMusic,
                        isLiked = isHeartLiked,
                        onTogglePlay = { isPlayingMusic = !isPlayingMusic },
                        onToggleLike = { isHeartLiked = !isHeartLiked },
                        vinylAngle = sparkleAngle
                    )
                }
            }

            // MIDDLE SECTION: Pastel App Icons Grid
            val apps = ThemeRepository.pastelApps.take(if (isCompact) 8 else 8)
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(if (isCompact) 6.dp else 12.dp),
                verticalArrangement = Arrangement.spacedBy(if (isCompact) 10.dp else 14.dp)
            ) {
                items(apps) { app ->
                    PastelAppIcon(
                        app = app,
                        isCompact = isCompact,
                        onClick = {
                            if (onAppClick != null) {
                                onAppClick(app)
                            } else {
                                selectedApp = app
                            }
                        }
                    )
                }
            }

            // BOTTOM SECTION: Floating Pastel Frosted Glass Dock
            PastelDock(
                isCompact = isCompact,
                onDockClick = { name ->
                    Toast.makeText(context, "Opening $name ♡", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // App Launch Modal Dialog
        selectedApp?.let { app ->
            AlertDialog(
                onDismissRequest = { selectedApp = null },
                containerColor = Color(0xFFFFF5F7),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFFFB6C1), Color(0xFFFF8DA1))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getPastelIcon(app.iconName),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "${app.name} ✨",
                            color = Color(0xFFD81B60),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Cursive,
                            fontSize = 20.sp
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Category: ${app.category} • Soft Pastel Pink Aesthetic",
                            color = Color(0xFF886070),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Kawaii marshmallow icons, rounded heart widgets, and dreamy pastel cloud theme for your phone ♡",
                            color = Color(0xFF442B34),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFB6C1))
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { selectedApp = null },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFE91E63))
                    ) {
                        Text("Open App ♡", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { selectedApp = null },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun CuteHandwrittenClockWidget(sparkleAngle: Float, isCompact: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                Brush.linearGradient(
                    listOf(Color(0xFFFFFFFF), Color(0xFFFFB6C1), Color(0xFFFFD1DC))
                ),
                RoundedCornerShape(26.dp)
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xDDFDF6F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isCompact) 12.dp else 18.dp, vertical = if (isCompact) 10.dp else 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = "Cloud",
                        tint = Color(0xFFFF8DA1),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "have a dreamy day ♡",
                        color = Color(0xFFD81B60),
                        fontSize = if (isCompact) 11.sp else 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Sparkle",
                    tint = Color(0xFFFFB6C1),
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(sparkleAngle)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Handwritten-styled Soft Clock Display
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "09:42",
                    color = Color(0xFFC2185B),
                    fontSize = if (isCompact) 34.sp else 46.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.sp
                )
                Column {
                    Text(
                        text = "pm ♡",
                        color = Color(0xFFFF69B4),
                        fontSize = if (isCompact) 12.sp else 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "✨ sweet",
                        color = Color(0xFFAA7088),
                        fontSize = 10.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Thursday, Oct 1 • 23°C ☁️",
                    color = Color(0xFF7A5868),
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "♡ 92% Battery",
                    color = Color(0xFFE91E63),
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun HeartShapedAffirmationWidget(
    activeMood: String,
    onSelectMood: (String) -> Unit,
    pulseScale: Float,
    isCompact: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.2.dp,
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFB6C1), Color(0xFFFF8DA1), Color(0xFFFFD1DC))
                ),
                RoundedCornerShape(22.dp)
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xE6FFF3F6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = if (isCompact) 8.dp else 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Heart-shaped Icon Badge
                    Box(
                        modifier = Modifier
                            .size(if (isCompact) 32.dp else 36.dp)
                            .shadow(4.dp, CircleShape, ambientColor = Color(0xFFFF8DA1), spotColor = Color(0xFFFF8DA1))
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFF8DA1), Color(0xFFE91E63))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Heart",
                            tint = Color.White,
                            modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Daily Sparkle & Affirmation",
                            color = Color(0xFF880E4F),
                            fontSize = if (isCompact) 11.sp else 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "You are pure magic! Shine bright ♡",
                            color = Color(0xFFAD1457),
                            fontSize = if (isCompact) 9.sp else 10.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }

                Text(
                    text = "🌸",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mood Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val moods = listOf("🌸 Blossom", "✨ Sparkle", "🍓 Sweet", "☁️ Cozy")
                moods.forEach { mood ->
                    val isSelected = activeMood == mood
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Color(0xFFFF8DA1) else Color(0x66FFD1DC)
                            )
                            .border(
                                0.8.dp,
                                if (isSelected) Color(0xFFE91E63) else Color(0x33FFB6C1),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectMood(mood) }
                            .padding(vertical = if (isCompact) 4.dp else 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mood,
                            color = if (isSelected) Color.White else Color(0xFF880E4F),
                            fontSize = if (isCompact) 8.sp else 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CutePastelMusicWidget(
    isPlaying: Boolean,
    isLiked: Boolean,
    onTogglePlay: () -> Unit,
    onToggleLike: () -> Unit,
    vinylAngle: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFFB6C1).copy(alpha = 0.6f), RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xE6FFF5F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Spinning Pastel Vinyl Record
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC0CB))
                        .border(1.dp, Color(0xFFFF8DA1), CircleShape)
                        .rotate(if (isPlaying) vinylAngle else 0f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE91E63))
                    )
                }

                Column {
                    Text(
                        text = "Strawberry Milk Lofi 🍓",
                        color = Color(0xFF880E4F),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Chibi Cafe Beats • Dreamy Vibe",
                        color = Color(0xFFA37081),
                        fontSize = 9.sp
                    )
                }
            }

            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Heart Favorite Button
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isLiked) Color(0xFFFFD1DC) else Color(0x44FFB6C1))
                        .clickable { onToggleLike() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Play / Pause Button
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF8DA1))
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PastelAppIcon(
    app: LauncherApp,
    isCompact: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .testTag("pastel_app_${app.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            // Soft marshmallow rounded squircle
            Box(
                modifier = Modifier
                    .size(if (isCompact) 48.dp else 56.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = Color(0xFFFFB6C1),
                        spotColor = Color(0xFFFF8DA1)
                    )
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFFF0F5), Color(0xFFFFD1DC), Color(0xFFFFB6C1))
                        )
                    )
                    .border(
                        1.5.dp,
                        Color(0xFFFFFFFF).copy(alpha = 0.8f),
                        RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getPastelIcon(app.iconName),
                    contentDescription = app.name,
                    tint = Color(0xFFD81B60),
                    modifier = Modifier.size(if (isCompact) 22.dp else 26.dp)
                )
            }

            // Notification Badge
            app.badge?.let { badge ->
                Box(
                    modifier = Modifier
                        .padding(top = 1.dp, end = 1.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF4081))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = app.name,
            color = Color(0xFF4A2837),
            fontSize = if (isCompact) 9.sp else 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PastelDock(
    isCompact: Boolean,
    onDockClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xEEFFFFFF))
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFB6C1), Color(0xFFFFD1DC), Color(0xFFFFB6C1))
                ),
                RoundedCornerShape(24.dp)
            )
            .shadow(8.dp, RoundedCornerShape(24.dp), ambientColor = Color(0xFFFFB6C1), spotColor = Color(0xFFFFB6C1))
            .padding(horizontal = 16.dp, vertical = if (isCompact) 6.dp else 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val dockIcons = listOf(
                Triple("Call", Icons.Default.Call, "call"),
                Triple("Chat", Icons.Default.ChatBubble, "chat"),
                Triple("Blossom", Icons.Default.Spa, "drawer"),
                Triple("Cam", Icons.Default.CameraAlt, "camera")
            )

            dockIcons.forEach { (name, icon, tag) ->
                Box(
                    modifier = Modifier
                        .size(if (isCompact) 36.dp else 44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFFE4E1), Color(0xFFFFB6C1))
                            )
                        )
                        .border(1.dp, Color.White, CircleShape)
                        .clickable { onDockClick(name) }
                        .testTag("pastel_dock_$tag"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = Color(0xFFC2185B),
                        modifier = Modifier.size(if (isCompact) 18.dp else 22.dp)
                    )
                }
            }
        }
    }
}

fun getPastelIcon(name: String): ImageVector {
    return when (name) {
        "phone" -> Icons.Default.Call
        "chat" -> Icons.Default.ChatBubble
        "camera" -> Icons.Default.CameraAlt
        "edit_note" -> Icons.Default.EditNote
        "bakery" -> Icons.Default.Cake
        "music" -> Icons.Default.MusicNote
        "web" -> Icons.Default.Language
        "photo" -> Icons.Default.Image
        "favorite" -> Icons.Default.Favorite
        "settings" -> Icons.Default.Settings
        "calendar" -> Icons.Default.CalendarMonth
        "shopping_bag" -> Icons.Default.ShoppingBag
        else -> Icons.Default.Pets
    }
}
