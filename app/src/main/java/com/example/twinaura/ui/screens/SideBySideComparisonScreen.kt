package com.example.twinaura.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.twinaura.model.ComparisonMode
import com.example.twinaura.model.ThemeAesthetic
import com.example.twinaura.ui.components.ModernPhoneFrame

@Composable
fun SideBySideComparisonScreen(
    onNavigateToNeonFull: () -> Unit,
    onNavigateToPastelFull: () -> Unit,
    onNavigateToSpecs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedMode by remember { mutableStateOf(ComparisonMode.SIDE_BY_SIDE) }
    var splitFraction by remember { mutableFloatStateOf(0.5f) }
    val animatedSplitFraction by animateFloatAsState(targetValue = splitFraction, label = "split_frac")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .verticalScroll(rememberScrollState())
    ) {
        // TOP CONTROLS & COMPARISON SELECTOR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SIDE-BY-SIDE MOCKUP",
                        color = Color(0xFFFF3366),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Dual Aesthetic Comparison",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Specs quick button
                OutlinedButton(
                    onClick = onNavigateToSpecs,
                    modifier = Modifier.height(34.dp).testTag("specs_quick_btn"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333A4D)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Specs", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mode Toggle Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ComparisonMode.values().forEach { mode ->
                    val isSelected = selectedMode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFF1E2333) else Color(0xFF12141C))
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFFFF3366) else Color(0xFF262C3D),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedMode = mode }
                            .padding(vertical = 8.dp)
                            .testTag("mode_tab_${mode.name}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = mode.iconText, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = mode.label,
                                color = if (isSelected) Color.White else Color.Gray,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // MAIN SHOWCASE AREA ACCORDING TO SELECTED MODE
        when (selectedMode) {
            ComparisonMode.SIDE_BY_SIDE -> {
                DualPhonesView(
                    onNeonClick = onNavigateToNeonFull,
                    onPastelClick = onNavigateToPastelFull
                )
            }
            ComparisonMode.WIPE_SLIDER -> {
                WipeSliderView(
                    splitFraction = animatedSplitFraction,
                    onFractionChange = { splitFraction = it.coerceIn(0.08f, 0.92f) },
                    onPreset = { splitFraction = it }
                )
            }
            ComparisonMode.POSTER_8K -> {
                HighResPosterMockupView(
                    onSaveWallpaper = { themeName ->
                        Toast.makeText(context, "$themeName Wallpaper ready for home screen!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // THEME COMPARISON MATRIX & CALLOUTS
        ThemeComparisonMatrix(
            onNeonClick = onNavigateToNeonFull,
            onPastelClick = onNavigateToPastelFull
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * 1. DUAL PHONES VIEW: Two borderless phone frames side-by-side
 */
@Composable
fun DualPhonesView(
    onNeonClick: () -> Unit,
    onPastelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // Tag row
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Neon Red Badge
            Row(
                modifier = Modifier
                    .clip(CutCornerShape(6.dp))
                    .background(Color(0xFF22030A))
                    .border(1.dp, Color(0xFFFF1744), CutCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFFF1744)))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "NEON RED CYBER",
                    color = Color(0xFFFF1744),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Pastel Pink Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF0F5))
                    .border(1.dp, Color(0xFFFFB6C1), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "♡", color = Color(0xFFE91E63), fontSize = 10.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PASTEL PINK CUTE",
                    color = Color(0xFFD81B60),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Side-by-side phone mockups container
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(490.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // LEFT PHONE: Neon Red Tech
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onNeonClick() }
            ) {
                ModernPhoneFrame(
                    aesthetic = ThemeAesthetic.NEON_RED_TECH,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 24.dp,
                    showStatusBar = true
                ) {
                    NeonRedTechHomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        isCompact = true
                    )
                }

                // Fullscreen overlay button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xCC000000))
                        .border(1.dp, Color(0xFFFF1744), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fullscreen, contentDescription = null, tint = Color(0xFFFF1744), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Tap to Explore", color = Color.White, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            // RIGHT PHONE: Pastel Pink Cute
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onPastelClick() }
            ) {
                ModernPhoneFrame(
                    aesthetic = ThemeAesthetic.PASTEL_PINK_CUTE,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 24.dp,
                    showStatusBar = true
                ) {
                    PastelPinkCuteHomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        isCompact = true
                    )
                }

                // Fullscreen overlay button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xEEFFFFFF))
                        .border(1.dp, Color(0xFFFFB6C1), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fullscreen, contentDescription = null, tint = Color(0xFFE91E63), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Tap to Explore", color = Color(0xFF880E4F), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * 2. WIPE SLIDER VIEW: Single phone with draggable split divider
 */
@Composable
fun WipeSliderView(
    splitFraction: Float,
    onFractionChange: (Float) -> Unit,
    onPreset: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Drag the center divider left/right to compare themes",
            color = Color.LightGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Preset buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("25% Red" to 0.25f, "50 / 50" to 0.5f, "75% Red" to 0.75f).forEach { (label, frac) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E2333))
                        .clickable { onPreset(frac) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Wipe phone frame
        Box(
            modifier = Modifier
                .width(280.dp)
                .height(520.dp)
                .shadow(16.dp, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .background(Color.Black)
                .border(3.dp, Color(0xFF2B3245), RoundedCornerShape(32.dp))
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalWidthPx = constraints.maxWidth.toFloat()

                // Both screens rendered in a stacked container with clipRect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            // Right side (Pastel Pink)
                            clipRect(
                                left = size.width * splitFraction,
                                top = 0f,
                                right = size.width,
                                bottom = size.height,
                                clipOp = ClipOp.Intersect
                            ) {
                                this@drawWithContent.drawContent()
                            }
                        }
                ) {
                    PastelPinkCuteHomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        isCompact = true
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            // Left side (Neon Red Tech)
                            clipRect(
                                left = 0f,
                                top = 0f,
                                right = size.width * splitFraction,
                                bottom = size.height,
                                clipOp = ClipOp.Intersect
                            ) {
                                this@drawWithContent.drawContent()
                            }
                        }
                ) {
                    NeonRedTechHomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        isCompact = true
                    )
                }

                // Vertical Divider Line & Draggable Handle
                val dividerX = (maxWidth * splitFraction)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.5.dp)
                        .align(Alignment.CenterStart)
                        .padding(start = dividerX - 1.25.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFFF1744), Color.White, Color(0xFFFFB6C1))
                            )
                        )
                )

                // Draggable Center Knob
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.CenterStart)
                        .padding(start = dividerX - 21.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF161A26))
                        .border(2.dp, Color.White, CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val newFrac = splitFraction + (dragAmount.x / totalWidthPx)
                                onFractionChange(newFrac)
                            }
                        }
                        .testTag("wipe_slider_knob"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "↔️", fontSize = 16.sp)
                }
            }
        }
    }
}

/**
 * 3. HIGH-RES POSTER MOCKUP VIEW: 8K generated master artwork
 */
@Composable
fun HighResPosterMockupView(
    onSaveWallpaper: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF333B50), RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF10121B))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✨", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "8K Master Showcase Render",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF26324D))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("ULTRA HD", color = Color(0xFF64B5F6), fontSize = 9.sp, fontWeight = FontWeight.Black)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // The Generated 16:9 Mockup Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dual_theme_mockup_1790890187903),
                        contentDescription = "Side-by-side comparison mockup of dark neon-red tech aesthetic and soft pastel-pink cute aesthetic",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons to download/apply wallpaper
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSaveWallpaper("Dark Neon-Red Tech") },
                        modifier = Modifier.weight(1f).testTag("save_neon_wallpaper"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Wallpaper, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Get Neon Wall", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onSaveWallpaper("Soft Pastel-Pink Cute") },
                        modifier = Modifier.weight(1f).testTag("save_pastel_wallpaper"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8DA1)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Wallpaper, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Get Pastel Wall", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * 4. THEME COMPARISON MATRIX: Design tokens breakdown
 */
@Composable
fun ThemeComparisonMatrix(
    onNeonClick: () -> Unit,
    onPastelClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, Color(0xFF262C3E), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11141F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "DESIGN ARCHITECTURE COMPARISON",
                color = Color(0xFFFF5252),
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(10.dp))

            val comparisons = listOf(
                Triple("Design Aesthetic", "Dark Neon-Red Cyber Anime", "Soft Pastel-Pink Kawaii Cloud"),
                Triple("Primary Palette", "#FF1744 Crimson & Carbon Black", "#FFB6C1 Soft Blossom & Peach"),
                Triple("Clock Widget", "Sharp Monospace + Glowing HUD", "Curved Handwritten Script + Sparkles"),
                Triple("Special Widget", "Shadow Hunter S-Rank Aura Bar", "Heart-Shaped Affirmation & Mood"),
                Triple("Icon Geometry", "Chamfered 45° Outlined Glow", "Marshmallow Pebble Squircle"),
                Triple("Music Player", "Waveform Equalizer + 140BPM", "Spinning Pastel Vinyl + Heart Like")
            )

            comparisons.forEachIndexed { index, (feature, left, right) ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text(
                        text = feature,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Left
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1E0910))
                                .border(0.8.dp, Color(0xFFFF1744).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(text = left, color = Color(0xFFFF8A9E), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                        }
                        // Right
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF281720))
                                .border(0.8.dp, Color(0xFFFFB6C1).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(text = right, color = Color(0xFFFFC0CB), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
