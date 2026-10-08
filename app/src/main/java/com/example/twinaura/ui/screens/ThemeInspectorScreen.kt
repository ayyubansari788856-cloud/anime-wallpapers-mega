package com.example.twinaura.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twinaura.model.ThemeAesthetic

@Composable
fun ThemeInspectorScreen(
    onPreviewNeon: () -> Unit,
    onPreviewPastel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // Audio synthesizer for audio vibe test
    val toneGen = remember {
        try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HEADER
        Column {
            Text(
                text = "DESIGN TOKENS & SPECS",
                color = Color(0xFFFF5252),
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Style Architecture & Assets",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 1. COLOR PALETTES (Side-by-side swatch cards)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF11141F)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262C3E))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Palette, contentDescription = null, tint = Color(0xFFFF3366), modifier = Modifier.size(16.dp))
                    Text(
                        text = "Color Palette Tokens",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Neon Red Palette
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NEON RED CYBER",
                            color = Color(0xFFFF1744),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val neonSwatches = listOf(
                            Triple("Scarlet Red", "#FF1744", Color(0xFFFF1744)),
                            Triple("Crimson Core", "#D50032", Color(0xFFD50032)),
                            Triple("Carbon Void", "#07070B", Color(0xFF07070B)),
                            Triple("Electric Orange", "#FF3D00", Color(0xFFFF3D00))
                        )
                        neonSwatches.forEach { (name, hex, color) ->
                            SwatchRow(name = name, hex = hex, color = color) {
                                clipboardManager.setText(AnnotatedString(hex))
                                Toast.makeText(context, "Copied $hex", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    // Right Pastel Pink Palette
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PASTEL PINK CUTE",
                            color = Color(0xFFFF8DA1),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val pastelSwatches = listOf(
                            Triple("Blossom Pink", "#FFB6C1", Color(0xFFFFB6C1)),
                            Triple("Strawberry", "#FF8DA1", Color(0xFFFF8DA1)),
                            Triple("Marshmallow", "#FFF0F5", Color(0xFFFFF0F5)),
                            Triple("Deep Berry", "#C2185B", Color(0xFFC2185B))
                        )
                        pastelSwatches.forEach { (name, hex, color) ->
                            SwatchRow(name = name, hex = hex, color = color) {
                                clipboardManager.setText(AnnotatedString(hex))
                                Toast.makeText(context, "Copied $hex", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        // 2. INTERACTIVE AUDIO & VIBE TEST
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF11141F)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262C3E))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                    Text(
                        text = "Vibe & Sound Feedback Simulator",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap below to test synthesized audio feedback engineered for each theme:",
                    color = Color.Gray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cyber sound button
                    Button(
                        onClick = {
                            try {
                                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 120)
                            } catch (e: Exception) { }
                            Toast.makeText(context, "Played Cyber HUD Beep", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f).testTag("sound_cyber_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF20070D)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF1744)),
                        shape = CutCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFFF1744), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cyber Laser", color = Color(0xFFFF1744), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }

                    // Kawaii sound button
                    Button(
                        onClick = {
                            try {
                                toneGen?.startTone(ToneGenerator.TONE_DTMF_D, 180)
                            } catch (e: Exception) { }
                            Toast.makeText(context, "Played Kawaii Sparkle Chime ♡", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f).testTag("sound_kawaii_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1724)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB6C1)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFFB6C1), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pastel Chime", color = Color(0xFFFFB6C1), fontSize = 11.sp)
                    }
                }
            }
        }

        // 3. FULLSCREEN PREVIEW LAUNCHERS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF11141F)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262C3E))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                Text(
                    text = "Launch Standalone Home Screen",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Experience the full simulated borderless smartphone home screen for either aesthetic:",
                    color = Color.Gray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onPreviewNeon,
                        modifier = Modifier.weight(1f).testTag("launch_neon_preview"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Smartphone, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("🔴 Neon Red", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onPreviewPastel,
                        modifier = Modifier.weight(1f).testTag("launch_pastel_preview"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8DA1)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Smartphone, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("🌸 Pastel Pink", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SwatchRow(
    name: String,
    hex: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(0.5.dp, Color.White.copy(alpha = 0.4f), CircleShape)
            )
            Text(
                text = name,
                color = Color.LightGray,
                fontSize = 10.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = hex,
                color = Color.Gray,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.DarkGray, modifier = Modifier.size(10.dp))
        }
    }
}
