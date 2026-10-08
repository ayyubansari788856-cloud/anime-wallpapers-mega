package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.MicroTask
import com.example.ui.theme.AuraCelestialPurple
import com.example.ui.theme.AuraCoinGold
import com.example.ui.theme.AuraNeonCyan
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen
import kotlinx.coroutines.delay

@Composable
fun MicroTaskPlayerModal(
    task: MicroTask,
    onClose: () -> Unit,
    onComplete: (taskId: Long, xp: Int, coins: Int) -> Unit,
    onSaveGratitude: (text: String, mood: Int) -> Unit
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark),
            color = BackgroundDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header with Close and Task Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (task.category == "DAWN") "☀️ DAWN RITUAL" else "🌙 DUSK RITUAL",
                            color = if (task.category == "DAWN") AuraSolarGold else AuraCelestialPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = task.title,
                            color = TextPrimaryDark,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceCard)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Player",
                            tint = TextSecondaryDark
                        )
                    }
                }

                // Interactive Content based on taskType
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    when (task.taskType) {
                        "WATER" -> WaterDrinkingView(
                            onFinish = { onComplete(task.id, task.xpReward, task.coinReward) }
                        )
                        "BREATHING" -> BreathingCircleView(
                            durationSec = task.durationSeconds,
                            onFinish = { onComplete(task.id, task.xpReward, task.coinReward) }
                        )
                        "JOURNAL", "INTENTION" -> ReflectionJournalView(
                            isJournal = task.taskType == "JOURNAL",
                            onSave = { text, mood ->
                                onSaveGratitude(text, mood)
                                onComplete(task.id, task.xpReward, task.coinReward)
                            }
                        )
                        else -> GenericFocusTimerView(
                            totalSeconds = task.durationSeconds,
                            onFinish = { onComplete(task.id, task.xpReward, task.coinReward) }
                        )
                    }
                }

                // Reward Footer Tag
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCard)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "XP",
                            tint = XpEmeraldGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${task.xpReward} XP",
                            color = XpEmeraldGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = AuraCoinGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${task.coinReward} Coins",
                            color = AuraCoinGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

// 1. Water Drinking Interactive View
@Composable
private fun WaterDrinkingView(onFinish: () -> Unit) {
    var waterMl by remember { mutableIntStateOf(250) }
    val targetMl = 500

    val fillFraction by animateFloatAsState(
        targetValue = (waterMl.toFloat() / targetMl).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "waterFill"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Water Glass representation
        Box(
            modifier = Modifier
                .size(width = 140.dp, height = 220.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp, topStart = 8.dp, topEnd = 8.dp))
                .border(3.dp, AuraNeonCyan.copy(alpha = 0.6f), RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp, topStart = 8.dp, topEnd = 8.dp))
                .background(SurfaceDark),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Water fill layer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((220 * fillFraction).dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(AuraNeonCyan.copy(alpha = 0.8f), Color(0xFF0077B6))
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.LocalDrink,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "$waterMl ml",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Drink 1-2 glasses to kickstart your metabolism",
            color = TextSecondaryDark,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { waterMl = (waterMl + 250).coerceAtMost(1000) },
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceCard),
                modifier = Modifier.testTag("drink_water_button")
            ) {
                Text("+250 ml", color = AuraNeonCyan, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(containerColor = AuraSolarGold),
                modifier = Modifier.testTag("finish_water_button")
            ) {
                Text("Hydrated! Claim", color = BackgroundDark, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 2. 4-7-8 Breathing Circle View
@Composable
private fun BreathingCircleView(durationSec: Int, onFinish: () -> Unit) {
    // Phases: "Inhale (4s)", "Hold (7s)", "Exhale (8s)"
    var phase by remember { mutableStateOf("Inhale") }
    var phaseSecondsRemaining by remember { mutableIntStateOf(4) }
    var cycleCount by remember { mutableIntStateOf(1) }
    val maxCycles = 3

    LaunchedEffect(Unit) {
        while (cycleCount <= maxCycles) {
            // Inhale 4s
            phase = "Inhale through Nose"
            for (i in 4 downTo 1) {
                phaseSecondsRemaining = i
                delay(1000)
            }
            // Hold 7s
            phase = "Hold Calmly"
            for (i in 7 downTo 1) {
                phaseSecondsRemaining = i
                delay(1000)
            }
            // Exhale 8s
            phase = "Exhale slowly through Mouth"
            for (i in 8 downTo 1) {
                phaseSecondsRemaining = i
                delay(1000)
            }
            cycleCount++
        }
    }

    val targetScale = when {
        phase.startsWith("Inhale") -> 1.35f
        phase.startsWith("Hold") -> 1.35f
        else -> 0.85f
    }

    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(
            durationMillis = if (phase.startsWith("Inhale")) 4000 else if (phase.startsWith("Hold")) 500 else 8000,
            easing = FastOutSlowInEasing
        ),
        label = "breathingScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(240.dp)
        ) {
            // Outer glowing ring
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .scale(animatedScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                AuraCelestialPurple.copy(alpha = 0.5f),
                                AuraSolarGold.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Inner circle
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(animatedScale)
                    .clip(CircleShape)
                    .background(AuraCelestialPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$phaseSecondsRemaining",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = phase,
            color = AuraSolarGold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Cycle ${cycleCount.coerceAtMost(maxCycles)} of $maxCycles",
            color = TextSecondaryDark,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinish,
            colors = ButtonDefaults.buttonColors(containerColor = AuraSolarGold),
            modifier = Modifier.testTag("finish_breathing_button")
        ) {
            Text("Complete Ritual", color = BackgroundDark, fontWeight = FontWeight.Bold)
        }
    }
}

// 3. Reflection & Intentions Journal View
@Composable
private fun ReflectionJournalView(
    isJournal: Boolean,
    onSave: (text: String, mood: Int) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var selectedMood by remember { mutableIntStateOf(5) }
    val moods = listOf(
        Pair(5, "😄 Great"),
        Pair(4, "😊 Good"),
        Pair(3, "😌 Calm"),
        Pair(2, "😐 Okay"),
        Pair(1, "😴 Tired")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isJournal)
                "Aaj ka din kaisa raha? Ek achhi cheez ya gratitude note likhein:"
            else
                "Aaj ke din ka sabse zaroori focus ya intention kya hai?",
            color = TextPrimaryDark,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mood Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            moods.forEach { (rating, label) ->
                val isSelected = selectedMood == rating
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) AuraCelestialPurple else SurfaceCard)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = label.take(2),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = label.drop(3),
                        color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            placeholder = {
                Text(
                    if (isJournal)
                        "Maine aaj ek achha kaam kiya / mujhe shaanti mili..."
                    else
                        "Mera #1 Goal: Project complete karna..."
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimaryDark,
                unfocusedTextColor = TextPrimaryDark,
                focusedBorderColor = AuraSolarGold,
                unfocusedBorderColor = BorderHighlight
            ),
            minLines = 4,
            maxLines = 6,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("reflection_text_input")
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val finalNote = if (textInput.isBlank()) "Mindfulness completed." else textInput
                onSave(finalNote, selectedMood)
            },
            colors = ButtonDefaults.buttonColors(containerColor = AuraSolarGold),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag("save_journal_button")
        ) {
            Text("Save & Claim Rewards", color = BackgroundDark, fontWeight = FontWeight.Bold)
        }
    }
}

// 4. Generic 2-3 Min Focus Countdown View
@Composable
private fun GenericFocusTimerView(
    totalSeconds: Int,
    onFinish: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(totalSeconds) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isRunning, secondsLeft) {
        if (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    val progress = if (totalSeconds > 0) (secondsLeft.toFloat() / totalSeconds) else 0f
    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        ) {
            // Ring background
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape)
                    .border(6.dp, SurfaceCard, CircleShape)
            )

            // Timer display
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeFormatted,
                    color = TextPrimaryDark,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (isRunning) "Deep Focus" else "Paused",
                    color = TextSecondaryDark,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(
                onClick = { isRunning = !isRunning },
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Toggle Timer",
                    tint = AuraSolarGold,
                    modifier = Modifier.size(28.dp)
                )
            }

            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(containerColor = AuraSolarGold),
                modifier = Modifier
                    .height(54.dp)
                    .testTag("finish_focus_button")
            ) {
                Text("Finish Session", color = BackgroundDark, fontWeight = FontWeight.Bold)
            }
        }
    }
}
