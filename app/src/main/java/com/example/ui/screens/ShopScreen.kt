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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfile
import com.example.ui.theme.AuraCelestialPurple
import com.example.ui.theme.AuraCoinGold
import com.example.ui.theme.AuraNeonCyan
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.StreakFrostColor
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen

data class ShopItem(
    val id: String,
    val title: String,
    val description: String,
    val cost: Int,
    val icon: ImageVector,
    val accentColor: Color,
    val itemType: String,
    val itemValue: String
)

data class CoinPack(
    val id: String,
    val title: String,
    val coins: Int,
    val priceRs: Int,
    val isBestValue: Boolean = false
)

@Composable
fun ShopScreen(
    userProfile: UserProfile?,
    onPurchase: (cost: Int, itemType: String, itemValue: String) -> Unit,
    onWatchAd: () -> Unit,
    onBuyCoinPack: (costRs: Int, coinsGiven: Int) -> Unit = { _, _ -> },
    onBuyProPass: (plan: String) -> Unit = {}
) {
    val coins = userProfile?.auraCoins ?: 0
    val isPro = userProfile?.isProMember ?: false
    val currentAvatar = userProfile?.currentAvatar ?: "Zen Ember"

    val coinPacks = listOf(
        CoinPack("pack_small", "Starter Pouch", 500, 29),
        CoinPack("pack_medium", "Aura Vault", 2000, 99, isBestValue = true),
        CoinPack("pack_large", "Celestial Chest", 5000, 199)
    )

    val powerUps = listOf(
        ShopItem(
            id = "freeze_shield",
            title = "Streak Freeze Shield",
            description = "1 din task miss hone par bhi streak nahi tootegi",
            cost = 50,
            icon = Icons.Default.AcUnit,
            accentColor = StreakFrostColor,
            itemType = "STREAK_FREEZE",
            itemValue = "Streak Freeze"
        ),
        ShopItem(
            id = "xp_boost",
            title = "2x XP Elixir",
            description = "Instant +150 XP bonus league rank boost ke liye",
            cost = 75,
            icon = Icons.Default.Bolt,
            accentColor = AuraSolarGold,
            itemType = "XP_BOOST",
            itemValue = "XP Boost"
        )
    )

    val avatars = listOf(
        ShopItem(
            id = "celestial_fox",
            title = "Celestial Spirit Fox",
            description = "Soothing indigo mystic companion",
            cost = 200,
            icon = Icons.Default.Diamond,
            accentColor = AuraCelestialPurple,
            itemType = "AVATAR",
            itemValue = "Celestial Fox"
        ),
        ShopItem(
            id = "solar_phoenix",
            title = "Solar Phoenix",
            description = "Warm golden wings of boundless dawn energy",
            cost = 350,
            icon = Icons.Default.Bolt,
            accentColor = AuraSolarGold,
            itemType = "AVATAR",
            itemValue = "Solar Phoenix"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Aura Armory & Shop",
                            color = TextPrimaryDark,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Gamified rewards & direct in-app purchases",
                            color = TextSecondaryDark,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceDark)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = AuraCoinGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$coins Coins",
                            color = AuraCoinGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section: Direct Coin Store (Bina Ads Ke Monetization & Purchase)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Buy Coins Directly (No Ads)",
                        color = TextPrimaryDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AuraSolarGold.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("100% Ad-Free", color = AuraSolarGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(
                    text = "Bina ad dekhe turant coins pane ke liye in-app pack chunein",
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        items(coinPacks.size) { index ->
            val pack = coinPacks[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (pack.isBestValue) 1.dp else 0.dp,
                        color = if (pack.isBestValue) AuraSolarGold else Color.Transparent,
                        shape = RoundedCornerShape(14.dp)
                    )
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
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(AuraCoinGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = AuraCoinGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = pack.title,
                                    color = TextPrimaryDark,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (pack.isBestValue) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(AuraSolarGold)
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("BEST VALUE", color = BackgroundDark, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                            Text(
                                text = "+${pack.coins} Aura Coins",
                                color = AuraCoinGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = { onBuyCoinPack(pack.priceRs, pack.coins) },
                        colors = ButtonDefaults.buttonColors(containerColor = AuraSolarGold),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("buy_coin_pack_${pack.id}")
                    ) {
                        Text("₹${pack.priceRs}", color = BackgroundDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Section: Streak Power-ups
        item {
            Text(
                text = "Streak Power-Ups",
                color = TextPrimaryDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(powerUps.size) { index ->
            val item = powerUps[index]
            ShopItemCard(
                item = item,
                coins = coins,
                onBuy = { onPurchase(item.cost, item.itemType, item.itemValue) }
            )
        }

        // Section: Avatar Skins
        item {
            Text(
                text = "Cosmetic Spirit Avatars",
                color = TextPrimaryDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(avatars.size) { index ->
            val item = avatars[index]
            val isCurrent = currentAvatar == item.itemValue
            ShopItemCard(
                item = item,
                coins = coins,
                isEquipped = isCurrent,
                onBuy = { onPurchase(item.cost, item.itemType, item.itemValue) }
            )
        }

        // Section: Aura Pro Subscription Banner (Freemium monetization)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("aura_pro_subscription_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(AuraCelestialPurple, Color(0xFF240046))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = AuraSolarGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = if (isPro) "AuraQuest PRO (Active ✓)" else "AuraQuest PRO",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "• 100% Ad-Free experience\n• 2x Real Cash reward multiplier\n• Unlimited Streak Freeze protection\n• Advanced 365-day consistency analytics",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "₹149/month or ₹999/year",
                                    color = AuraSolarGold,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Cancel anytime",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = { onBuyProPass("monthly") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPro) XpEmeraldGreen else AuraSolarGold
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("buy_pro_pass_button")
                            ) {
                                Text(
                                    text = if (isPro) "Active ✓" else "Get Pro",
                                    color = BackgroundDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Monetization: Rewarded Video Ad Card (Optional Free alternative)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AuraNeonCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .testTag("rewarded_ad_card")
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
                                .background(AuraNeonCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleFilled,
                                contentDescription = null,
                                tint = AuraNeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Optional: Free Ad Bonus",
                                color = TextPrimaryDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Watch ad if you don't want to buy packs",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = onWatchAd,
                        colors = ButtonDefaults.buttonColors(containerColor = AuraNeonCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("watch_ad_button")
                    ) {
                        Text("Watch", color = BackgroundDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
private fun ShopItemCard(
    item: ShopItem,
    coins: Int,
    isEquipped: Boolean = false,
    onBuy: () -> Unit
) {
    val canAfford = coins >= item.cost

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
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
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(item.accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = item.accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.title,
                        color = TextPrimaryDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.description,
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (isEquipped) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(XpEmeraldGreen.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = XpEmeraldGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Equipped", color = XpEmeraldGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onBuy,
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canAfford) AuraSolarGold else SurfaceDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("buy_item_${item.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = if (canAfford) BackgroundDark else TextSecondaryDark,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${item.cost}",
                            color = if (canAfford) BackgroundDark else TextSecondaryDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
