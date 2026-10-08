package com.example.twinaura.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twinaura.model.ThemeAesthetic

@Composable
fun ModernPhoneFrame(
    aesthetic: ThemeAesthetic,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 32.dp,
    showStatusBar: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val isNeon = aesthetic == ThemeAesthetic.NEON_RED_TECH
    val frameBorderColor = if (isNeon) Color(0xFFFF1744).copy(alpha = 0.45f) else Color(0xFFFFB6C1).copy(alpha = 0.6f)
    val outerBezelColor = if (isNeon) Color(0xFF07070B) else Color(0xFF2C2428)
    val statusContentColor = if (isNeon) Color(0xFFFF3366) else Color(0xFFFF69B4)

    Box(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(cornerRadius), ambientColor = frameBorderColor, spotColor = frameBorderColor)
            .clip(RoundedCornerShape(cornerRadius))
            .background(outerBezelColor)
            .border(2.5.dp, frameBorderColor, RoundedCornerShape(cornerRadius))
            .padding(4.dp) // Bezel thickness
    ) {
        // Inner Display Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRadius - 4.dp))
                .background(if (isNeon) Color(0xFF000000) else Color(0xFFFFF0F5))
        ) {
            // Main Phone Content
            content()

            // Punch Hole / Dynamic Island & Status Bar
            if (showStatusBar) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Clock
                        Text(
                            text = if (isNeon) "21:42" else "9:42 ♡",
                            color = statusContentColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Center: Punch hole camera capsule
                        Box(
                            modifier = Modifier
                                .height(14.dp)
                                .width(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Black.copy(alpha = 0.95f))
                                .border(0.5.dp, frameBorderColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isNeon) Color(0xFFFF1744).copy(alpha = 0.5f) else Color(0xFFFFC0CB).copy(alpha = 0.5f))
                            )
                        }

                        // Right: Telemetry icons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = if (isNeon) "5G+" else "5G ✨",
                                color = statusContentColor,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = "WiFi",
                                tint = statusContentColor,
                                modifier = Modifier.size(10.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.BatteryFull,
                                contentDescription = "Battery",
                                tint = statusContentColor,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Gesture Navigation Pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp)
                    .width(72.dp)
                    .height(3.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isNeon) Brush.horizontalGradient(
                            listOf(Color(0xFFFF1744).copy(alpha = 0.3f), Color(0xFFFF1744), Color(0xFFFF1744).copy(alpha = 0.3f))
                        ) else Brush.horizontalGradient(
                            listOf(Color(0xFFFFB6C1).copy(alpha = 0.4f), Color(0xFFFF69B4), Color(0xFFFFB6C1).copy(alpha = 0.4f))
                        )
                    )
            )
        }
    }
}
