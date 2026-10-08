package com.example.game.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.data.GameRepository
import com.example.game.data.OwnedBikeEntity
import com.example.game.data.PlayerProfileEntity
import com.example.game.model.BikeCatalog
import com.example.game.model.DrivingMission
import com.example.game.model.PlayerBikeState
import kotlinx.coroutines.launch

@Composable
fun GameMainScreen() {
    val context = LocalContext.current
    val repository = remember { GameRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    val profile by repository.profileFlow.collectAsState(initial = PlayerProfileEntity())
    val ownedBikes by repository.ownedBikesFlow.collectAsState(initial = emptyList())
    val levelProgress by repository.levelsProgressFlow.collectAsState(initial = emptyList())

    // Tabs: 0: Garage, 1: Missions, 2: Store
    var currentTab by remember { mutableIntStateOf(0) }
    var activeMission by remember { mutableStateOf<DrivingMission?>(null) }

    // Modals
    var showStarterPack by remember { mutableStateOf(false) }
    var showVipModal by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val activeSpec = BikeCatalog.getBikeById(profile.activeBikeId)
    val activeBikeState = ownedBikes.find { it.bikeId == profile.activeBikeId }?.let {
        PlayerBikeState(
            bikeId = it.bikeId,
            isUnlocked = it.isUnlocked,
            speedLevel = it.speedLevel,
            accelLevel = it.accelLevel,
            handlingLevel = it.handlingLevel,
            fuelLevel = it.nitroLevel,
            selectedColorHex = it.selectedColorHex
        )
    } ?: PlayerBikeState(bikeId = activeSpec.id, isUnlocked = true, selectedColorHex = activeSpec.defaultColorHex)

    // Back handling
    BackHandler(enabled = activeMission != null || currentTab != 0 || showStarterPack || showVipModal) {
        when {
            activeMission != null -> activeMission = null
            showStarterPack -> showStarterPack = false
            showVipModal -> showVipModal = false
            currentTab != 0 -> currentTab = 0
        }
    }

    if (activeMission != null) {
        // ACTIVE 3D DRIVING MISSION SCREEN
        MissionPlayScreen(
            mission = activeMission!!,
            bikeSpec = activeSpec,
            bikeState = activeBikeState,
            onCompleteMission = { grade, coins, rubies, dist ->
                coroutineScope.launch {
                    val finalCoins = if (profile.isVipMember) coins * 2 else coins
                    repository.recordMissionSuccess(activeMission!!.id, grade, finalCoins, rubies, dist)
                    snackbarHostState.showSnackbar("🏆 Mission Clear! Grade: $grade • +$finalCoins Coins!")
                }
                activeMission = null
                currentTab = 1 // return to missions
            },
            onExitMission = { activeMission = null }
        )
    } else {
        // MAIN HUB: GARAGE, MISSIONS LIST, STORE
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF1E293B),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        icon = { Icon(imageVector = Icons.Default.TwoWheeler, contentDescription = "Garage") },
                        label = { Text("Garage", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color(0xFFFFD600),
                            indicatorColor = Color(0xFFFFD600),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_garage")
                    )

                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        icon = { Icon(imageVector = Icons.Default.FormatListNumbered, contentDescription = "Missions") },
                        label = { Text("Missions", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color(0xFFFFD600),
                            indicatorColor = Color(0xFFFFD600),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_missions")
                    )

                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        icon = { Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = "Shop") },
                        label = { Text("Shop & VIP", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color(0xFFFFD600),
                            indicatorColor = Color(0xFFFFD600),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_shop")
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color(0xFF0F172A)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    0 -> GarageScreen(
                        profile = profile,
                        ownedBikes = ownedBikes,
                        onSelectBike = { bikeId ->
                            coroutineScope.launch {
                                repository.equipBike(bikeId)
                                snackbarHostState.showSnackbar("Equipped ${BikeCatalog.getBikeById(bikeId).name}!")
                            }
                        },
                        onUnlockBike = { bikeId, coins, gems ->
                            coroutineScope.launch {
                                val ok = repository.unlockBike(bikeId, coins, gems)
                                if (ok) snackbarHostState.showSnackbar("🎉 Unlocked ${BikeCatalog.getBikeById(bikeId).name}!")
                                else snackbarHostState.showSnackbar("Need more currency to unlock!")
                            }
                        },
                        onUpgradeStat = { bikeId, stat, cost ->
                            coroutineScope.launch {
                                val ok = repository.upgradeBikeStat(bikeId, stat, cost)
                                if (ok) snackbarHostState.showSnackbar("Upgraded $stat! ⚙️")
                                else snackbarHostState.showSnackbar("Need more coins!")
                            }
                        },
                        onCustomizeBike = { bikeId, color, _ ->
                            coroutineScope.launch {
                                repository.customizeBike(bikeId, color)
                            }
                        },
                        onStartDrive = {
                            // Launch Highway Speed Mission (or current level)
                            activeMission = drRidingMissions.find { it.id == profile.currentCareerLevel } ?: drRidingMissions.first()
                        },
                        onOpenShop = { currentTab = 2 },
                        onOpenMissions = { currentTab = 1 }
                    )

                    1 -> MissionsListScreen(
                        currentDriverLevel = profile.currentCareerLevel,
                        levelProgress = levelProgress,
                        onSelectMission = { mission ->
                            activeMission = mission
                        }
                    )

                    2 -> StoreScreen(
                        profile = profile,
                        onBuyCurrency = { coins, gems, label ->
                            coroutineScope.launch {
                                repository.addCurrency(coins, gems)
                                snackbarHostState.showSnackbar("Purchased $label! Credited instantly.")
                            }
                        },
                        onClaimStarterPack = {
                            coroutineScope.launch {
                                repository.claimStarterPack()
                                snackbarHostState.showSnackbar("⚡ Starter Pack Activated! Bari 250 Unlocked!")
                            }
                        },
                        onClaimVip = { isMonthly ->
                            coroutineScope.launch {
                                repository.claimVipPass(isMonthly)
                                snackbarHostState.showSnackbar("👑 VIP Pass Active! 2X Rewards & Hyperbike Unlocked!")
                            }
                        },
                        onWatchRewardedAd = { type ->
                            coroutineScope.launch {
                                if (type == "COINS") {
                                    repository.addCurrency(2000, 0)
                                    snackbarHostState.showSnackbar("🎁 +2,000 Coins credited from Video Ad!")
                                } else {
                                    repository.addCurrency(0, 15)
                                    snackbarHostState.showSnackbar("🎁 +15 Rubies credited from Video Ad!")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
