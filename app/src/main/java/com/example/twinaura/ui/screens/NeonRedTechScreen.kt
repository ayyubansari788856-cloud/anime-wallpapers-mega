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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
fun NeonRedTechHomeScreen(
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    onAppClick: ((LauncherApp) -> Unit)? = null
) {
    val context = LocalContext.current
    var isPlayingMusic by remember { mutableStateOf(true) }
    var selectedApp by remember { mutableStateOf<LauncherApp?>(null) }
    var currentQuestIndex by remember { mutableIntStateOf(0) }

    val quests = listOf(
        "Aura Sync: 98% [S-Rank Sovereign]",
        "Target Locked: Neo-Tokyo Sector 7",
        "Overclock Active: Core Temp 38°C"
    )

    // Glow pulse animation for neon red
    val infiniteTransition = rememberInfiniteTransition(label = "neon_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    val wave1 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(tween(450), RepeatMode.Reverse),
        label = "w1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(550), RepeatMode.Reverse),
        label = "w2"
    )
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(tween(380), RepeatMode.Reverse),
        label = "w3"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07070B))
    ) {
        // High-tech dark neon wallpaper background
        Image(
            painter = painterResource(id = R.drawable.neon_red_theme_1790890201591),
            contentDescription = "Dark Neon Red Tech Anime Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient dark scrim for high contrast readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC000000),
                            Color(0x990A0004),
                            Color(0xDD000000)
                        )
                    )
                )
        )

        // Cyber Grid Lines Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val step = 48.dp.toPx()
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color = Color(0x15FF1744),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 0.8f
                        )
                        y += step
                    }
                }
        )

        // Main Home Screen Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isCompact) 10.dp else 16.dp)
                .padding(top = if (isCompact) 28.dp else 36.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP SECTION: Sharp Glowing Digital Clock & Anime HUD Widgets
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp)
            ) {
                // 1. Sharp Glowing Clock Widget
                NeonClockWidget(pulseAlpha = pulseAlpha, isCompact = isCompact)

                // 2. Anime Aura / Hunter S-Rank Status Widget
                AnimeAuraWidget(
                    quest = quests[currentQuestIndex % quests.size],
                    onCycleQuest = {
                        currentQuestIndex++
                        Toast.makeText(context, "Quest updated: ${quests[currentQuestIndex % quests.size]}", Toast.LENGTH_SHORT).show()
                    },
                    pulseAlpha = pulseAlpha,
                    isCompact = isCompact
                )

                // 3. Cyberpunk Anime Music Player Widget (shown if not super compact)
                if (!isCompact) {
                    CyberMusicWidget(
                        isPlaying = isPlayingMusic,
                        onTogglePlay = { isPlayingMusic = !isPlayingMusic },
                        w1 = wave1,
                        w2 = wave2,
                        w3 = wave3
                    )
                }
            }

            // MIDDLE SECTION: Sleek Red Glowing Apps Grid
            val apps = ThemeRepository.neonApps.take(if (isCompact) 8 else 8)
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
                    NeonAppIcon(
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

            // BOTTOM SECTION: Floating Cyber Glass Dock
            NeonDock(
                isCompact = isCompact,
                onDockClick = { name ->
                    Toast.makeText(context, "Launching Cyber $name", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // App Launch Modal Dialog
        selectedApp?.let { app ->
            AlertDialog(
                onDismissRequest = { selectedApp = null },
                containerColor = Color(0xFF0F0B13),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CutCornerShape(6.dp))
                                .background(Color(0xFFFF1744))
                                .border(1.dp, Color(0xFFFF5252), CutCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getNeonIcon(app.iconName),
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "// ${app.name.uppercase()}",
                            color = Color(0xFFFF1744),
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 16.sp
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Category: ${app.category} | Protocol: SYS_RED_V4",
                            color = Color(0xFFAAAAAA),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Aesthetic: Sharp Neon Red Tech • Glowing Outlines • Anime Mecha HUD",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color(0xFFFF1744).copy(alpha = 0.5f))
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { selectedApp = null },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF1744))
                    ) {
                        Text("LAUNCH // EXECUTE", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { selectedApp = null },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                    ) {
                        Text("CLOSE", fontFamily = FontFamily.Monospace)
                    }
                }
            )
        }
    }
}

@Composable
fun NeonClockWidget(pulseAlpha: Float, isCompact: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(Color(0xFFFF1744), Color(0xFFFF5252).copy(alpha = 0.4f), Color(0xFFFF1744))
                ),
                CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
            ),
        shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC090A12))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isCompact) 10.dp else 16.dp, vertical = if (isCompact) 8.dp else 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF1744).copy(alpha = pulseAlpha))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NEO-TOKYO // SEC-09",
                        color = Color(0xFFFF5252),
                        fontSize = if (isCompact) 9.sp else 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "AURA_V2.4",
                    color = Color.Gray,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Large Sharp Glowing Clock
            Text(
                text = "21:42:08",
                color = Color(0xFFFF1744),
                fontSize = if (isCompact) 32.sp else 44.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0xFFFF003C),
                        offset = Offset(0f, 0f),
                        blurRadius = 24f * pulseAlpha
                    )
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "THU, OCT 01 • OVERCLOCK 4.8GHz",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "⚡ 88%",
                    color = Color(0xFFFF5252),
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AnimeAuraWidget(
    quest: String,
    onCycleQuest: () -> Unit,
    pulseAlpha: Float,
    isCompact: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCycleQuest() }
            .border(
                1.dp,
                Color(0xFFFF1744).copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xBB0D0D18))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = if (isCompact) 8.dp else 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Glowing Crimson Badge
            Box(
                modifier = Modifier
                    .size(if (isCompact) 32.dp else 40.dp)
                    .clip(CutCornerShape(8.dp))
                    .background(Color(0xFF20070B))
                    .border(1.2.dp, Color(0xFFFF1744), CutCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Aura",
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(if (isCompact) 18.dp else 24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ANIME AURA // S-RANK",
                        color = Color(0xFFFF5252),
                        fontSize = if (isCompact) 10.sp else 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "48,250 CP",
                        color = Color.White,
                        fontSize = if (isCompact) 10.sp else 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                LinearProgressIndicator(
                    progress = { 0.88f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isCompact) 4.dp else 6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFFF1744),
                    trackColor = Color(0xFF2A000A)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = quest,
                    color = Color.LightGray,
                    fontSize = if (isCompact) 9.sp else 10.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CyberMusicWidget(
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    w1: Float,
    w2: Float,
    w3: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFF1744).copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xAA080810))
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
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = "LOST IN PARADISE // SYNTH REDUX",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Anime Tech Cyberwave • 140 BPM",
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                }
            }

            // Animated Equalizer Visualizer & Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isPlaying) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.height(20.dp)
                    ) {
                        Box(modifier = Modifier.width(3.dp).height(w1.dp).background(Color(0xFFFF1744), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.width(3.dp).height(w2.dp).background(Color(0xFFFF5252), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.width(3.dp).height(w3.dp).background(Color(0xFFFF1744), RoundedCornerShape(1.dp)))
                    }
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF1744))
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NeonAppIcon(
    app: LauncherApp,
    isCompact: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .testTag("neon_app_${app.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            // Sleek red glowing container
            Box(
                modifier = Modifier
                    .size(if (isCompact) 48.dp else 56.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CutCornerShape(12.dp),
                        ambientColor = Color(0xFFFF1744),
                        spotColor = Color(0xFFFF1744)
                    )
                    .clip(CutCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF220008), Color(0xFF0F0B13), Color(0xFF1E0207))
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(Color(0xFFFF1744), Color(0xFFFF5252).copy(alpha = 0.3f), Color(0xFFFF003C))
                        ),
                        CutCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNeonIcon(app.iconName),
                    contentDescription = app.name,
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(if (isCompact) 22.dp else 26.dp)
                )
            }

            // Notification Badge
            app.badge?.let { badge ->
                Box(
                    modifier = Modifier
                        .padding(top = 1.dp, end = 1.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFF1744))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.Black,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = app.name,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = if (isCompact) 9.sp else 10.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NeonDock(
    isCompact: Boolean,
    onDockClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xEE090A12))
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(Color(0xFFFF1744).copy(alpha = 0.2f), Color(0xFFFF1744), Color(0xFFFF1744).copy(alpha = 0.2f))
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = if (isCompact) 6.dp else 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val dockIcons = listOf(
                Triple("Terminal", Icons.Default.Terminal, "terminal"),
                Triple("Chat", Icons.Default.ChatBubble, "chat"),
                Triple("Apps", Icons.Default.GridView, "drawer"),
                Triple("Camera", Icons.Default.CameraAlt, "camera")
            )

            dockIcons.forEach { (name, icon, tag) ->
                Box(
                    modifier = Modifier
                        .size(if (isCompact) 36.dp else 44.dp)
                        .clip(CutCornerShape(10.dp))
                        .background(Color(0xFF1E070C))
                        .border(1.dp, Color(0xFFFF1744).copy(alpha = 0.6f), CutCornerShape(10.dp))
                        .clickable { onDockClick(name) }
                        .testTag("neon_dock_$tag"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = Color(0xFFFF1744),
                        modifier = Modifier.size(if (isCompact) 18.dp else 22.dp)
                    )
                }
            }
        }
    }
}

fun getNeonIcon(name: String): ImageVector {
    return when (name) {
        "phone" -> Icons.Default.Terminal
        "chat" -> Icons.Default.ChatBubble
        "camera" -> Icons.Default.CameraAlt
        "game" -> Icons.Default.SportsEsports
        "stream" -> Icons.Default.Tv
        "music" -> Icons.Default.MusicNote
        "web" -> Icons.Default.Language
        "map" -> Icons.Default.Map
        "lock" -> Icons.Default.Lock
        "settings" -> Icons.Default.Settings
        "monitor" -> Icons.Default.Sensors
        else -> Icons.Default.Memory
    }
}
