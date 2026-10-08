package com.example.game.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.engine.GearState
import kotlinx.coroutines.launch

@Composable
fun DrDrivingControls(
    modifier: Modifier = Modifier,
    steeringAngle: Float, // -60 to +60
    onSteeringChange: (Float) -> Unit,
    onSteeringRelease: () -> Unit,
    isGasPressed: Boolean,
    onGasPressed: (Boolean) -> Unit,
    isBrakePressed: Boolean,
    onBrakePressed: (Boolean) -> Unit,
    gearState: GearState,
    onGearChange: (GearState) -> Unit,
    isBlinkerLeft: Boolean,
    onToggleBlinkerLeft: () -> Unit,
    isBlinkerRight: Boolean,
    onToggleBlinkerRight: () -> Unit,
    onHonk: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val wheelAnim = remember { Animatable(steeringAngle) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // 1. LEFT SIDE: DR. DRIVING ROTATING STEERING WHEEL WITH CENTER HORN
        Box(
            modifier = Modifier
                .size(136.dp)
                .clip(CircleShape)
                .background(Color(0xFF18181B))
                .border(4.dp, Color(0xFF3F3F46), CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                onSteeringRelease()
                            }
                        },
                        onDragCancel = {
                            coroutineScope.launch {
                                onSteeringRelease()
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val deltaAngle = (dragAmount.x * 0.75f)
                            val newAngle = (steeringAngle + deltaAngle).coerceIn(-65f, 65f)
                            onSteeringChange(newAngle)
                        }
                    )
                }
                .testTag("steering_wheel"),
            contentAlignment = Alignment.Center
        ) {
            // Steering Spokes (Rotating)
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .rotate(steeringAngle)
            ) {
                // Horizontal crossbar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .align(Alignment.Center)
                        .background(Color(0xFF27272A))
                )
                // Center spoke
                Box(
                    modifier = Modifier
                        .width(14.dp)
                        .height(64.dp)
                        .align(Alignment.BottomCenter)
                        .background(Color(0xFF27272A))
                )
                // Top marker indicator
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.TopCenter)
                        .background(Color(0xFFFFD600), CircleShape)
                )
            }

            // Center Horn Button
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF27272A))
                    .border(2.dp, Color(0xFF71717A), CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onHonk()
                                tryAwaitRelease()
                            }
                        )
                    }
                    .testTag("horn_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Horn",
                    tint = Color(0xFFFFD600),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 2. CENTER: TURN SIGNALS & HAZARD (◀ ▶)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Left Turn Indicator
                Surface(
                    color = if (isBlinkerLeft) Color(0xFF00E676) else Color(0xFF27272A),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.5.dp, Color(0xFF52525B), CircleShape)
                        .pointerInput(Unit) {
                            detectTapGestures { onToggleBlinkerLeft() }
                        }
                        .testTag("blinker_left")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Left Blinker",
                            tint = if (isBlinkerLeft) Color.Black else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Right Turn Indicator
                Surface(
                    color = if (isBlinkerRight) Color(0xFF00E676) else Color(0xFF27272A),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.5.dp, Color(0xFF52525B), CircleShape)
                        .pointerInput(Unit) {
                            detectTapGestures { onToggleBlinkerRight() }
                        }
                        .testTag("blinker_right")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Right Blinker",
                            tint = if (isBlinkerRight) Color.Black else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // 3. RIGHT SIDE: GEAR SELECTOR [D, N, R] + BRAKE & GAS PEDALS
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Bottom) {
            // Gear Selector Lever [D, N, R]
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF18181B))
                    .border(2.dp, Color(0xFF3F3F46), RoundedCornerShape(12.dp))
                    .padding(3.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                GearButton("D", isSelected = gearState == GearState.DRIVE, activeColor = Color(0xFF00E676)) {
                    onGearChange(GearState.DRIVE)
                }
                GearButton("N", isSelected = gearState == GearState.NEUTRAL, activeColor = Color(0xFFFFD600)) {
                    onGearChange(GearState.NEUTRAL)
                }
                GearButton("R", isSelected = gearState == GearState.REVERSE, activeColor = Color(0xFFFF1744)) {
                    onGearChange(GearState.REVERSE)
                }
            }

            // Textured Brake Pedal
            Box(
                modifier = Modifier
                    .size(width = 62.dp, height = 75.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isBrakePressed) Color(0xFFFF1744) else Color(0xFF27272A)
                    )
                    .border(2.5.dp, Color(0xFF52525B), RoundedCornerShape(10.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onBrakePressed(true)
                                tryAwaitRelease()
                                onBrakePressed(false)
                            }
                        )
                    }
                    .testTag("brake_pedal"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BRAKE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Spacer(modifier = Modifier.height(3.dp))
                    // Texture treads
                    repeat(3) {
                        Box(modifier = Modifier.width(36.dp).height(2.5.dp).background(Color(0x66FFFFFF)))
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
            }

            // Tall Accelerator Gas Pedal
            Box(
                modifier = Modifier
                    .size(width = 54.dp, height = 115.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isGasPressed) Color(0xFF00E676) else Color(0xFF27272A)
                    )
                    .border(2.5.dp, Color(0xFF52525B), RoundedCornerShape(10.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onGasPressed(true)
                                tryAwaitRelease()
                                onGasPressed(false)
                            }
                        )
                    }
                    .testTag("gas_pedal"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("GAS", fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (isGasPressed) Color.Black else Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    // Texture treads
                    repeat(5) {
                        Box(modifier = Modifier.width(32.dp).height(2.5.dp).background(Color(0x66FFFFFF)))
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun GearButton(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) activeColor else Color.Transparent)
            .pointerInput(Unit) {
                detectTapGestures { onClick() }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}
