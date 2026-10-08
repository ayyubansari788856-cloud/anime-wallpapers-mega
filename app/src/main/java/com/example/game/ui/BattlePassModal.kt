package com.example.game.ui

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

data class BattlePassTier(
    val tier: Int,
    val freeReward: String,
    val premiumReward: String,
    val isCompleted: Boolean = false
)

val sampleTiers = listOf(
    BattlePassTier(1, "5,000 Coins", "Cyberpunk Neon Helmet", isCompleted = true),
    BattlePassTier(2, "50 Gems", "Titanium Turbo Parts (+15 KM/H)", isCompleted = true),
    BattlePassTier(3, "10,000 Coins", "Hellfire Exhaust Plume FX", isCompleted = false),
    BattlePassTier(4, "100 Gems", "Custom Matte Black Carbon Fairings", isCompleted = false),
    BattlePassTier(5, "25,000 Coins", "★ GHOST RIDER CHOPPER UNLOCK ★", isCompleted = false)
)

@Composable
fun BattlePassModal(
    onDismiss: () -> Unit,
    onBuyPremiumPass: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xDD000000))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFF101726),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF00E5FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("HIGHWAY LEGENDS PASS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("SEASON 1 • 24 DAYS REMAINING", fontSize = 10.sp, color = Color(0xFFFFD600), fontWeight = FontWeight.Bold)
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar
                    Text("Season Progress: Tier 2 of 40", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { 0.25f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF00E5FF),
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(230.dp)
                    ) {
                        items(sampleTiers) { tier ->
                            Surface(
                                color = Color(0xFF161F33),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(if (tier.isCompleted) Color(0xFF00E676) else Color(0xFF1F2937)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${tier.tier}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Free: ${tier.freeReward}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                                        Text("⭐ Premium: ${tier.premiumReward}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onBuyPremiumPass,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("buy_pass_button")
                    ) {
                        Text("UNLOCK PREMIUM PASS ($4.99)", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
