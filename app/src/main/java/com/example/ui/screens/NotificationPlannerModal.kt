package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AuraCelestialPurple
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.StreakFlameColor
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

data class NotificationBlueprint(
    val id: String,
    val timing: String,
    val icon: ImageVector,
    val accentColor: Color,
    val title: String,
    val body: String,
    val categoryBadge: String
)

@Composable
fun NotificationPlannerModal(
    onDismiss: () -> Unit,
    onTestNotification: (title: String, body: String) -> Unit,
    onSaveSchedule: (morning: String, evening: String) -> Unit
) {
    var morningTime by remember { mutableStateOf("07:15 AM") }
    var eveningTime by remember { mutableStateOf("09:30 PM") }

    val notifications = listOf(
        NotificationBlueprint(
            id = "morning_kickstart",
            timing = "Subah $morningTime",
            icon = Icons.Default.WbSunny,
            accentColor = AuraSolarGold,
            categoryBadge = "🌅 Morning Kickstart",
            title = "☀️ Aapka Aura jag gaya hai, lekin use paani chahiye!",
            body = "2-minute ka hydration task complete karo aur apna 5-day streak bacha lo. +50 Sun Sparks waiting!"
        ),
        NotificationBlueprint(
            id = "evening_winddown",
            timing = "Shaam $eveningTime",
            icon = Icons.Default.NightsStay,
            accentColor = AuraCelestialPurple,
            categoryBadge = "🌙 Evening Wind-down",
            title = "🌙 Aaj ka din kaisa raha? 3 minute me wrap-up karein.",
            body = "Apne dimag ka bojh utaro, kal ka Top 1 goal chuno, aur apne Aura Spirit ko chain ki neend sone do."
        ),
        NotificationBlueprint(
            id = "streak_emergency",
            timing = "Raat 10:45 PM (Loss Aversion Trigger)",
            icon = Icons.Default.WarningAmber,
            accentColor = StreakFlameColor,
            categoryBadge = "🚨 Streak Emergency",
            title = "🚨 Oh no! 75-minute me aapka 5-day streak freeze ho jayega!",
            body = "Sirf 120 second ka evening reflection bacha hai. Tap karke apni mehnat barbad hone se bachao!"
        )
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderHighlight, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = AuraSolarGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Push Engine",
                                color = TextPrimaryDark,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SurfaceCard)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextSecondaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "Subah aur shaam user retention ke 3 high-converting notification hooks. Aap inhein test trigger karke preview kar sakte hain.",
                        color = TextSecondaryDark,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Notification Cards
                items(notifications.size) { index ->
                    val notif = notifications[index]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = notif.icon,
                                        contentDescription = null,
                                        tint = notif.accentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = notif.categoryBadge,
                                        color = notif.accentColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = notif.timing,
                                    color = TextSecondaryDark,
                                    fontSize = 10.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = notif.title,
                                color = TextPrimaryDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = notif.body,
                                color = TextSecondaryDark,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { onTestNotification(notif.title, notif.body) },
                                colors = ButtonDefaults.buttonColors(containerColor = notif.accentColor.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("test_notif_${notif.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    tint = notif.accentColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Send Test Alert Now",
                                    color = notif.accentColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Schedule times edit
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Daily Routine Timers",
                            color = TextPrimaryDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🌅 Morning Alarm:", color = TextSecondaryDark, fontSize = 12.sp)
                            Text("07:15 AM", color = AuraSolarGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🌙 Evening Wind-down:", color = TextSecondaryDark, fontSize = 12.sp)
                            Text("09:30 PM", color = AuraCelestialPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
