package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyHistory
import com.example.data.local.GratitudeEntry
import com.example.data.local.UserProfile
import com.example.ui.theme.AuraCelestialPurple
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.StreakFlameColor
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen

@Composable
fun StatsScreen(
    userProfile: UserProfile?,
    history: List<DailyHistory>,
    gratitudeEntries: List<GratitudeEntry>
) {
    val streak = userProfile?.currentStreak ?: 5
    val bestStreak = userProfile?.bestStreak ?: 12
    val totalXp = userProfile?.totalXp ?: 680

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Top Overview Cards
        item {
            Text(
                text = "Habit Consistency & Insights",
                color = TextPrimaryDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricCard(
                    title = "Current Streak",
                    value = "$streak Days",
                    icon = Icons.Default.LocalFireDepartment,
                    accentColor = StreakFlameColor,
                    modifier = Modifier.weight(1f)
                )

                StatMetricCard(
                    title = "Best Record",
                    value = "$bestStreak Days",
                    icon = Icons.Default.AutoAwesome,
                    accentColor = AuraSolarGold,
                    modifier = Modifier.weight(1f)
                )

                StatMetricCard(
                    title = "Total XP",
                    value = "$totalXp",
                    icon = Icons.Default.Star,
                    accentColor = XpEmeraldGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section: 28-Day Consistency Heatmap Grid
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = AuraSolarGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Consistency Heatmap",
                                color = TextPrimaryDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "92% Consistency",
                            color = XpEmeraldGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4 rows of 7 days grid
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (row in 0 until 4) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                for (col in 0 until 7) {
                                    val dayIndex = (row * 7) + col
                                    val isFilled = dayIndex < 22
                                    val isSunAndMoon = dayIndex % 3 == 0

                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                if (isFilled) {
                                                    if (isSunAndMoon) AuraSolarGold else XpEmeraldGreen.copy(alpha = 0.7f)
                                                } else {
                                                    SurfaceDark
                                                }
                                            )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Missed", color = TextSecondaryDark, fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(SurfaceDark))
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(XpEmeraldGreen.copy(alpha = 0.7f)))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("1 Spark", color = TextSecondaryDark, fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(AuraSolarGold))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Both Sparks", color = TextSecondaryDark, fontSize = 10.sp)
                    }
                }
            }
        }

        // Section: Past Reflections / Brain Dump Notes
        item {
            Text(
                text = "Gratitude & Reflection Log",
                color = TextPrimaryDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (gratitudeEntries.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Abhi tak koi reflection save nahi hua hai.",
                            color = TextSecondaryDark,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Evening task 'Daily Brain Dump' complete karke shuru karein!",
                            color = AuraCelestialPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(gratitudeEntries) { entry ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = entry.dateStr,
                                color = AuraCelestialPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            val moodEmoji = when (entry.moodRating) {
                                5 -> "😄 Great"
                                4 -> "😊 Good"
                                3 -> "😌 Calm"
                                2 -> "😐 Okay"
                                else -> "😴 Tired"
                            }
                            Text(
                                text = moodEmoji,
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = entry.text,
                            color = TextPrimaryDark,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun StatMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = TextPrimaryDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = title,
                color = TextSecondaryDark,
                fontSize = 10.sp
            )
        }
    }
}
