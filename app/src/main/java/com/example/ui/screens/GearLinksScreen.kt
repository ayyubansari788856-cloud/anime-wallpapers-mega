package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen

data class AffiliateProduct(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: String,
    val category: String,
    val icon: ImageVector,
    val accentColor: Color,
    val targetUrl: String
)

data class MonetizedGuide(
    val id: String,
    val title: String,
    val format: String,
    val description: String,
    val targetUrl: String
)

@Composable
fun GearLinksScreen(
    userProfile: UserProfile?,
    onOpenUrl: (url: String) -> Unit,
    onPayUpi: (amount: Double, note: String) -> Unit,
    onOpenUpiSettings: () -> Unit
) {
    val ownerUpi = "vajid2948@upi"

    val products = listOf(
        AffiliateProduct(
            id = "smart_bottle",
            title = "Smart Temp Water Bottle",
            subtitle = "LED display hydration reminder bottle",
            price = "₹499",
            category = "🌅 Morning Routine",
            icon = Icons.Default.LocalDrink,
            accentColor = AuraNeonCyan,
            targetUrl = "https://www.amazon.in/dp/B08XYZ1234?tag=auraquest-21"
        ),
        AffiliateProduct(
            id = "blue_glasses",
            title = "Sleep & Blue-Cut Glasses",
            subtitle = "Zero glare for screen detach ritual",
            price = "₹349",
            category = "🌙 Evening Routine",
            icon = Icons.Default.Visibility,
            accentColor = AuraCelestialPurple,
            targetUrl = "https://www.amazon.in/dp/B09ABC5678?tag=auraquest-21"
        ),
        AffiliateProduct(
            id = "habit_journal",
            title = "5-Minute Gratitude Journal",
            subtitle = "Guided daily morning & night prompts",
            price = "₹299",
            category = "📝 Reflection",
            icon = Icons.Default.MenuBook,
            accentColor = AuraSolarGold,
            targetUrl = "https://www.amazon.in/dp/B07KLM9012?tag=auraquest-21"
        ),
        AffiliateProduct(
            id = "zen_audio",
            title = "Sleep & Meditation Headphones",
            subtitle = "Ultra-soft headband for box breathing",
            price = "₹699",
            category = "🧘 Mind Focus",
            icon = Icons.Default.Headphones,
            accentColor = XpEmeraldGreen,
            targetUrl = "https://www.amazon.in/dp/B08QWE3456?tag=auraquest-21"
        )
    )

    val guides = listOf(
        MonetizedGuide(
            id = "guide_21",
            title = "21-Day Habit Master Blueprint",
            format = "PDF E-Book (Free)",
            description = "Duolingo + Atomic Habits framework summary",
            targetUrl = "https://shrinkearn.com/aura_blueprint"
        ),
        MonetizedGuide(
            id = "audio_pack",
            title = "432Hz Zen Awakening & Sleep Tracks",
            format = "Audio MP3 Pack (Free)",
            description = "Calm morning motivation and deep night rest sounds",
            targetUrl = "https://shrinkearn.com/aura_soundpack"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Direct UPI Creator Support Banner
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("direct_upi_creator_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF240046), Color(0xFF3C096C), Color(0xFF10002B))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Paid,
                                    contentDescription = null,
                                    tint = AuraSolarGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Direct UPI Link Payment",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = onOpenUpiSettings,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceDark)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "UPI Settings",
                                    tint = AuraSolarGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Play Store ke bina 100% paisa seedha aapke UPI ($ownerUpi) me jayega via GPay/PhonePe/Paytm!",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Support Amounts
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Pair(29.0, "₹29"),
                                Pair(49.0, "₹49"),
                                Pair(99.0, "₹99"),
                                Pair(199.0, "₹199")
                            ).forEach { (amt, label) ->
                                Button(
                                    onClick = { onPayUpi(amt, "AuraQuest Direct Support") },
                                    colors = ButtonDefaults.buttonColors(containerColor = AuraSolarGold),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("quick_upi_$label")
                                ) {
                                    Text(label, color = BackgroundDark, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Affiliate Product Links
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Curated Habit Gear (Affiliate Links)",
                        color = TextPrimaryDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(XpEmeraldGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Earn Commission", color = XpEmeraldGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(
                    text = "User link par click karke khareedega to seedha commission aapke affiliate account me aayega",
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        items(products) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenUrl(item.targetUrl) }
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
                                .background(item.accentColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = item.accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SurfaceDark)
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(item.category, color = item.accentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                color = TextPrimaryDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = item.subtitle,
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onOpenUrl(item.targetUrl) },
                        colors = ButtonDefaults.buttonColors(containerColor = item.accentColor),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Buy ${item.price}", color = BackgroundDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = BackgroundDark, modifier = Modifier.size(12.dp))
                        }
                    }
                }
            }
        }

        // Section: Monetized Shortener Download Links
        item {
            Column {
                Text(
                    text = "Monetized Guide & Audio Links",
                    color = TextPrimaryDark,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Per click/download par ad shortener se direct payout",
                    color = TextSecondaryDark,
                    fontSize = 11.sp
                )
            }
        }

        items(guides) { guide ->
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
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = guide.title,
                                color = TextPrimaryDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = guide.format,
                                color = AuraSolarGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = guide.description,
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { onOpenUrl(guide.targetUrl) },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = AuraSolarGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Get Link", color = AuraSolarGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
