package com.example.game.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.data.PlayerProfileEntity

@Composable
fun StoreScreen(
    profile: PlayerProfileEntity,
    onBuyCurrency: (coins: Int, gems: Int, label: String) -> Unit,
    onClaimStarterPack: () -> Unit,
    onClaimVip: (Boolean) -> Unit,
    onWatchRewardedAd: (rewardType: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E17))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("GARAGE STORE & VIP", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF00E5FF))
                Text("Gems, Coins, VIP Passes & Battle Season", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(8.dp)) {
                    Text("🪙 ${profile.coins}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600), modifier = Modifier.padding(6.dp))
                }
                Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(8.dp)) {
                    Text("💎 ${profile.gems}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF), modifier = Modifier.padding(6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. $0.99 NEON STARTER PACK (90% OFF SPECIAL ANCHOR)
        Surface(
            color = Color(0xFF1E1408),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFD600)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚡ NEON STRIKE STARTER PACK", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD600))
                    Surface(color = Color(0xFFFF1744), shape = RoundedCornerShape(6.dp)) {
                        Text("90% OFF", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("• Instantly Unlock Kawa 400 Ninja Superbike\n• 50,000 Bonus Coins + 150 Gems\n• 5x Free Emergency Revives\n• Permanently Removes Forced Interstitial Ads", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onClaimStarterPack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(46.dp).testTag("buy_starter_pack_button")
                ) {
                    Text("BUY FOR $0.99 (ONE-TIME ONLY)", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. REWARDED ADS SECTION (FREE REWARDS)
        Text("FREE DAILY REWARD ADS", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Free Coins
            Surface(
                color = Color(0xFF111827),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onWatchRewardedAd("COINS") }
                    .testTag("reward_ad_coins")
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("+5,000 COINS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Watch Video", fontSize = 10.sp, color = Color(0xFF00E676))
                }
            }

            // Free Gems
            Surface(
                color = Color(0xFF111827),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onWatchRewardedAd("GEMS") }
                    .testTag("reward_ad_gems")
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("+25 GEMS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Watch Video", fontSize = 10.sp, color = Color(0xFF00E5FF))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. VIP SUBSCRIPTIONS (Weekly & Monthly)
        Text("VIP PRO RIDER PASS", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE040FB))
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Weekly VIP ($2.99)
            Surface(
                color = Color(0xFF1B1424),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE040FB)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("WEEKLY VIP", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFE040FB))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• 2x All Race Cash & XP\n• 30,000 Coins + 150 Gems\n• Unlock Yamaha R1 Demon\n• Ad-Free", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onClaimVip(false) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE040FB)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(36.dp)
                    ) {
                        Text("$2.99 / WEEK", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Monthly Pro ($9.99)
            Surface(
                color = Color(0xFF121B24),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF00E5FF)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("MONTHLY PRO", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFF00E5FF))
                        Surface(color = Color(0xFF00E5FF), shape = RoundedCornerShape(4.dp)) {
                            Text("BEST", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Everything in VIP\n• 100,000 Coins + 500 Gems\n• Unlock Cyber Light-Cycle\n• Golden Nameplate", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onClaimVip(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(36.dp)
                    ) {
                        Text("$9.99 / MONTH", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4. GEM & COIN STORE PACKS
        Text("GEM PACKS (HARD CURRENCY)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        val gemPacks = listOf(
            Triple("Handful of Gems", 100, "$1.99"),
            Triple("Pouch of Gems (+15%)", 300, "$4.99"),
            Triple("Box of Gems (Most Popular)", 750, "$9.99"),
            Triple("Chest of Gems (+25% Extra)", 1800, "$19.99"),
            Triple("Vault of Gems (Whale Pack)", 5500, "$49.99")
        )

        gemPacks.forEach { (name, gems, price) ->
            Surface(
                color = Color(0xFF111827),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💎", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("+$gems Gems instantly credited", fontSize = 10.sp, color = Color(0xFF00E5FF))
                        }
                    }

                    Button(
                        onClick = { onBuyCurrency(0, gems, name) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(price, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
