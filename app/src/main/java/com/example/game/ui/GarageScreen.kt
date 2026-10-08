package com.example.game.ui

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.game.data.OwnedBikeEntity
import com.example.game.data.PlayerProfileEntity
import com.example.game.model.BikeCatalog

@Composable
fun GarageScreen(
    profile: PlayerProfileEntity,
    ownedBikes: List<OwnedBikeEntity>,
    onSelectBike: (String) -> Unit,
    onUnlockBike: (String, Int, Int) -> Unit,
    onUpgradeStat: (String, String, Int) -> Unit,
    onCustomizeBike: (String, Long?, Long?) -> Unit,
    onStartDrive: () -> Unit,
    onOpenShop: () -> Unit,
    onOpenMissions: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentSpec = BikeCatalog.allBikes[selectedIndex]
    val currentOwnedState = ownedBikes.find { it.bikeId == currentSpec.id }
        ?: OwnedBikeEntity(bikeId = currentSpec.id, isUnlocked = currentSpec.priceCoins == 0)

    val isEquipped = profile.activeBikeId == currentSpec.id
    val isUnlocked = currentOwnedState.isUnlocked

    val colorOptions = listOf(
        0xFFE50914, // Iconic Dr. Driving Red
        0xFFFAFAFA, // Pearl White
        0xFFFFD600, // Vibrant Yellow
        0xFF1E88E5, // Royal Blue
        0xFF212121  // Matte Black
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 1. TOP HEADER (Dr. Riding Title + Coins & Rubies)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Dr. Riding",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Urban Motorcycle Simulator",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                // Coins
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.clickable { onOpenShop() }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🪙", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${profile.coins}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
                    }
                }

                // Rubies / Gems
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.clickable { onOpenShop() }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("💎", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${profile.gems}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. DR. DRIVING 3D TURNTABLE SHOWROOM
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, Color(currentOwnedState.selectedColorHex), RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.dr_riding_garage_1790879832952),
                contentDescription = currentSpec.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Bottom gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xDD0F172A)),
                            startY = 60f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentSpec.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Color(currentOwnedState.selectedColorHex),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = currentSpec.bikeClass.displayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = currentSpec.description,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. VEHICLE CAROUSEL
        Text("SELECT MOTORCYCLE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(BikeCatalog.allBikes) { bike ->
                val isSelected = bike.id == currentSpec.id
                val owned = ownedBikes.find { it.bikeId == bike.id }?.isUnlocked ?: (bike.priceCoins == 0)

                Surface(
                    modifier = Modifier
                        .clickable {
                            selectedIndex = BikeCatalog.allBikes.indexOf(bike)
                            if (owned) {
                                onSelectBike(bike.id)
                            }
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFFFFD600) else Color(0xFF334155),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    color = if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.TwoWheeler,
                            contentDescription = bike.name,
                            tint = if (owned) Color(bike.defaultColorHex) else Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = bike.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFFFFD600) else Color.White
                        )
                        Text(
                            text = if (owned) "OWNED" else "${bike.priceCoins} 🪙",
                            fontSize = 10.sp,
                            color = if (owned) Color(0xFF00E676) else Color(0xFFFFD600)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. PERFORMANCE TUNING (Engine, Brakes, Steering, Fuel Economy)
        Surface(
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("PERFORMANCE TUNING", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD600))
                Spacer(modifier = Modifier.height(10.dp))

                // Engine Power
                StatUpgradeRow(
                    label = "Engine Power (${currentSpec.topSpeedKmh + (currentOwnedState.speedLevel - 1) * 12} km/h)",
                    level = currentOwnedState.speedLevel,
                    cost = 2500 * currentOwnedState.speedLevel,
                    playerCoins = profile.coins,
                    isUnlocked = isUnlocked,
                    onUpgrade = { onUpgradeStat(currentSpec.id, "SPEED", 2500 * currentOwnedState.speedLevel) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Brakes
                StatUpgradeRow(
                    label = "Disc Brakes (Stage ${currentOwnedState.accelLevel}/5)",
                    level = currentOwnedState.accelLevel,
                    cost = 2000 * currentOwnedState.accelLevel,
                    playerCoins = profile.coins,
                    isUnlocked = isUnlocked,
                    onUpgrade = { onUpgradeStat(currentSpec.id, "ACCEL", 2000 * currentOwnedState.accelLevel) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Steering
                StatUpgradeRow(
                    label = "Steering & Handling (Stage ${currentOwnedState.handlingLevel}/5)",
                    level = currentOwnedState.handlingLevel,
                    cost = 2200 * currentOwnedState.handlingLevel,
                    playerCoins = profile.coins,
                    isUnlocked = isUnlocked,
                    onUpgrade = { onUpgradeStat(currentSpec.id, "HANDLING", 2200 * currentOwnedState.handlingLevel) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fuel Efficiency
                StatUpgradeRow(
                    label = "Fuel Efficiency (Stage ${currentOwnedState.nitroLevel}/5)",
                    level = currentOwnedState.nitroLevel,
                    cost = 1800 * currentOwnedState.nitroLevel,
                    playerCoins = profile.coins,
                    isUnlocked = isUnlocked,
                    onUpgrade = { onUpgradeStat(currentSpec.id, "FUEL", 1800 * currentOwnedState.nitroLevel) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. COLOR CHOOSER
        if (isUnlocked) {
            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Bike Paint", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        colorOptions.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorHex))
                                    .border(
                                        width = if (currentOwnedState.selectedColorHex == colorHex) 2.5.dp else 1.dp,
                                        color = if (currentOwnedState.selectedColorHex == colorHex) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { onCustomizeBike(currentSpec.id, colorHex, null) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // 6. PRIMARY ACTIONS (UNLOCK OR DRIVE NOW)
        if (!isUnlocked) {
            Button(
                onClick = { onUnlockBike(currentSpec.id, currentSpec.priceCoins, currentSpec.priceGems) },
                enabled = profile.coins >= currentSpec.priceCoins && profile.gems >= currentSpec.priceGems,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("unlock_bike_button")
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "UNLOCK FOR ${currentSpec.priceCoins} COINS / ${currentSpec.priceGems} GEMS",
                    color = Color.Black,
                    fontWeight = FontWeight.Black
                )
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onOpenMissions,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f).height(54.dp).testTag("missions_button")
                ) {
                    Text("MISSIONS", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onStartDrive,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1.5f).height(54.dp).testTag("drive_now_button")
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("DRIVE NOW", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 15.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))
    }
}

@Composable
private fun StatUpgradeRow(
    label: String,
    level: Int,
    cost: Int,
    playerCoins: Int,
    isUnlocked: Boolean,
    onUpgrade: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 12.sp, color = Color.White)
            if (level < 5 && isUnlocked) {
                Button(
                    onClick = onUpgrade,
                    enabled = playerCoins >= cost,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(26.dp)
                ) {
                    Text("+1 ($cost 🪙)", fontSize = 10.sp, color = Color(0xFFFFD600), fontWeight = FontWeight.Bold)
                }
            } else if (level >= 5) {
                Text("MAX", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { (level / 5f).coerceIn(0.1f, 1.0f) },
            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
            color = Color(0xFFFFD600),
            trackColor = Color(0xFF334155)
        )
    }
}
