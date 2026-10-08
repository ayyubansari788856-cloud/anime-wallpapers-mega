package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfile
import com.example.ui.theme.AuraCelestialPurple
import com.example.ui.theme.AuraCoinGold
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.StreakFlameColor
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen

@Composable
fun AuraTopBar(
    userProfile: UserProfile?,
    onOpenNotifications: () -> Unit,
    onSecretAdminTrigger: () -> Unit,
    modifier: Modifier = Modifier
) {
    val streak = userProfile?.currentStreak ?: 0
    val coins = userProfile?.auraCoins ?: 0
    val level = userProfile?.level ?: 1
    val sunEarned = userProfile?.sunSparkEarnedToday ?: false
    val moonEarned = userProfile?.moonSparkEarnedToday ?: false

    // Secret Tap Detector: 5 taps on the app title unlocks the Secret Owner PIN Dialog!
    var tapCount by remember { mutableIntStateOf(0) }
    var lastTapTimestamp by remember { mutableLongStateOf(0L) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title & Dual Sparks status with SECRET 5-TAP GESTURE for Owner
        Column(
            modifier = Modifier
                .clickable {
                    val now = System.currentTimeMillis()
                    if (now - lastTapTimestamp > 2500) {
                        tapCount = 1
                    } else {
                        tapCount += 1
                        if (tapCount >= 5) {
                            tapCount = 0
                            onSecretAdminTrigger()
                        }
                    }
                    lastTapTimestamp = now
                }
                .testTag("app_title_secret_tap_target")
        ) {
            Text(
                text = "AuraQuest",
                color = TextPrimaryDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                // Sun Spark Icon
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Sun Spark",
                    tint = if (sunEarned) AuraSolarGold else TextSecondaryDark.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (sunEarned) "Sun ✓" else "Sun ⏳",
                    color = if (sunEarned) AuraSolarGold else TextSecondaryDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Moon Spark Icon
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "Moon Spark",
                    tint = if (moonEarned) AuraCelestialPurple else TextSecondaryDark.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (moonEarned) "Moon ✓" else "Moon ⏳",
                    color = if (moonEarned) AuraCelestialPurple else TextSecondaryDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Standard Gamified Stats Badges (Normal Users only see their game progress!)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Player Level Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Level",
                    tint = XpEmeraldGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Lv.$level",
                    color = XpEmeraldGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Streak Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = StreakFlameColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$streak d",
                    color = TextPrimaryDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Aura Coins Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Aura Coins",
                    tint = AuraCoinGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$coins",
                    color = AuraCoinGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Notification Reminders Modal Button
            IconButton(
                onClick = onOpenNotifications,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
                    .testTag("notification_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Reminder Alerts",
                    tint = AuraSolarGold,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
