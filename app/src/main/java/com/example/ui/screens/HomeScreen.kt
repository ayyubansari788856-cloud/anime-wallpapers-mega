package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.MicroTask
import com.example.data.local.UserProfile
import com.example.ui.theme.AuraCelestialPurple
import com.example.ui.theme.AuraCoinGold
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.DawnAccent
import com.example.ui.theme.DuskAccent
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen

@Composable
fun HomeScreen(
    userProfile: UserProfile?,
    tasks: List<MicroTask>,
    selectedRealm: String,
    onSelectRealm: (String) -> Unit,
    onStartTask: (MicroTask) -> Unit,
    onOpenAddCustom: () -> Unit,
    onClaimMystery: (isMorning: Boolean) -> Unit
) {
    val filteredTasks = when (selectedRealm) {
        "DAWN" -> tasks.filter { it.category == "DAWN" || it.category == "ANYTIME" }
        "DUSK" -> tasks.filter { it.category == "DUSK" || it.category == "ANYTIME" }
        else -> tasks
    }

    val isDawn = selectedRealm == "DAWN"
    val heroDrawable = if (isDawn) {
        R.drawable.img_aura_spirit_dawn_1790769699172
    } else {
        R.drawable.img_aura_spirit_dusk_1790769710650
    }

    val xp = userProfile?.totalXp ?: 0
    val level = userProfile?.level ?: 1
    val currentLevelXp = xp % 250
    val progress = currentLevelXp.toFloat() / 250f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Realm Switcher Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceDark)
                    .border(1.dp, BorderHighlight, RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RealmTabItem(
                    title = "☀️ Dawn (Subah)",
                    isSelected = selectedRealm == "DAWN",
                    activeColor = DawnAccent,
                    onClick = { onSelectRealm("DAWN") },
                    modifier = Modifier.weight(1f)
                )
                RealmTabItem(
                    title = "🌙 Dusk (Shaam)",
                    isSelected = selectedRealm == "DUSK",
                    activeColor = DuskAccent,
                    onClick = { onSelectRealm("DUSK") },
                    modifier = Modifier.weight(1f)
                )
                RealmTabItem(
                    title = "🌟 All",
                    isSelected = selectedRealm == "ALL",
                    activeColor = AuraSolarGold,
                    onClick = { onSelectRealm("ALL") },
                    modifier = Modifier.weight(0.7f)
                )
            }
        }

        // Hero Banner with Animated Spirit Companion
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = heroDrawable),
                        contentDescription = "Aura Realm Spirit",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, BackgroundDark.copy(alpha = 0.88f))
                                )
                            )
                    )

                    // Overlay Content: Spirit Companion Level & Mood
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = userProfile?.currentAvatar ?: "Zen Ember",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDawn) DawnAccent else DuskAccent)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "LVL $level",
                                    color = BackgroundDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isDawn) "Mood: Radiant & Energized ⚡" else "Mood: Serene & Reflective 🌙",
                            color = TextSecondaryDark,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // XP Progress Bar
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            LinearProgressIndicator(
                                progress = { progress },
                                color = if (isDawn) AuraSolarGold else AuraCelestialPurple,
                                trackColor = SurfaceCard,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$currentLevelXp/250 XP",
                                color = TextSecondaryDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Daily Mystery Chest / Rest Capsule Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClaimMystery(isDawn) }
                    .testTag("claim_mystery_reward_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isDawn) DawnAccent.copy(alpha = 0.2f) else DuskAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = if (isDawn) DawnAccent else DuskAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isDawn) "☀️ Morning Mystery Chest" else "🌙 Evening Rest Pearl",
                                color = TextPrimaryDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Roz kholne par +50 Coins & XP Booster!",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = { onClaimMystery(isDawn) },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDawn) DawnAccent else DuskAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Claim",
                            color = BackgroundDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Section Title: Today's Micro-Rituals
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isDawn) "Morning Micro-Tasks" else "Evening Micro-Tasks",
                        color = TextPrimaryDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "2-5 minute zero-friction habits",
                        color = TextSecondaryDark,
                        fontSize = 12.sp
                    )
                }

                OutlinedButton(
                    onClick = onOpenAddCustom,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("add_custom_task_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = AuraSolarGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Task", color = AuraSolarGold, fontSize = 12.sp)
                }
            }
        }

        // List of MicroTasks
        items(filteredTasks, key = { it.id }) { task ->
            MicroTaskCard(
                task = task,
                onStart = { onStartTask(task) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun RealmTabItem(
    title: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.25f) else Color.Transparent)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) activeColor else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun MicroTaskCard(
    task: MicroTask,
    onStart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("task_card_${task.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = task.title,
                        color = if (task.isCompleted) TextSecondaryDark else TextPrimaryDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = XpEmeraldGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = task.subtitle,
                    color = TextSecondaryDark,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Tags: Duration + Rewards
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceDark)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = TextSecondaryDark,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${task.durationSeconds / 60}m",
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = "+${task.xpReward} XP",
                        color = XpEmeraldGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "+${task.coinReward} Coins",
                        color = AuraCoinGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Start Button
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (task.isCompleted) SurfaceDark else AuraSolarGold
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("start_task_${task.id}")
            ) {
                if (task.isCompleted) {
                    Text("Redo", color = TextSecondaryDark, fontSize = 12.sp)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = BackgroundDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Start", color = BackgroundDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
