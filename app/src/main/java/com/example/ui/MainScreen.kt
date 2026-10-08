package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddCustomTaskDialog
import com.example.ui.components.AuraTopBar
import com.example.ui.components.MysteryRewardDialog
import com.example.ui.components.PaymentMethodChooserDialog
import com.example.ui.components.SecretAdminLoginDialog
import com.example.ui.components.StartAppBannerView
import com.example.ui.components.UpiSettingsDialog
import com.example.ui.screens.GearLinksScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MicroTaskPlayerModal
import com.example.ui.screens.NotificationPlannerModal
import com.example.ui.screens.OwnerDashboardScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.SocialScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.AuraNeonCyan
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen

@Composable
fun MainScreen(
    viewModel: AuraViewModel = viewModel()
) {
    val context = LocalContext.current

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val recentHistory by viewModel.recentHistory.collectAsStateWithLifecycle()
    val gratitudeEntries by viewModel.gratitudeEntries.collectAsStateWithLifecycle()
    val ownerVault by viewModel.ownerVault.collectAsStateWithLifecycle()
    val creatorVouchers by viewModel.creatorVouchers.collectAsStateWithLifecycle()

    val selectedRealm by viewModel.selectedRealm.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val activeTask by viewModel.activeTask.collectAsStateWithLifecycle()
    val showAddCustomDialog by viewModel.showAddCustomDialog.collectAsStateWithLifecycle()
    val showNotificationModal by viewModel.showNotificationModal.collectAsStateWithLifecycle()
    val showUpiSettingsDialog by viewModel.showUpiSettingsDialog.collectAsStateWithLifecycle()
    val ownerUpiId by viewModel.ownerUpiId.collectAsStateWithLifecycle()
    val ownerWebPaymentUrl by viewModel.ownerWebPaymentUrl.collectAsStateWithLifecycle()
    val ownerStartAppId by viewModel.ownerStartAppId.collectAsStateWithLifecycle()
    val pendingPayment by viewModel.pendingPaymentItem.collectAsStateWithLifecycle()
    val mysteryReward by viewModel.mysteryRewardResult.collectAsStateWithLifecycle()
    val snackBarMessage by viewModel.snackBarMessage.collectAsStateWithLifecycle()
    val clanBossHealth by viewModel.clanBossHealth.collectAsStateWithLifecycle()

    // Secret Admin Mode States (Invisible to normal users!)
    var showAdminPinDialog by remember { mutableStateOf(false) }
    var isOwnerDashboardUnlocked by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackBar()
        }
    }

    // BackHandler to handle custom state navigation
    BackHandler(enabled = isOwnerDashboardUnlocked || selectedTab != 0 || activeTask != null || showAddCustomDialog || showNotificationModal || showUpiSettingsDialog || pendingPayment != null || mysteryReward != null) {
        when {
            isOwnerDashboardUnlocked -> isOwnerDashboardUnlocked = false
            activeTask != null -> viewModel.closeTaskPlayer()
            showAddCustomDialog -> viewModel.setShowAddCustomDialog(false)
            showNotificationModal -> viewModel.setShowNotificationModal(false)
            showUpiSettingsDialog -> viewModel.setShowUpiSettings(false)
            pendingPayment != null -> viewModel.dismissPaymentChooser()
            mysteryReward != null -> viewModel.dismissMysteryDialog()
            selectedTab != 0 -> viewModel.setSelectedTab(0)
        }
    }

    if (isOwnerDashboardUnlocked) {
        // SECRET OWNER DASHBOARD (Visible ONLY to Owner after entering PIN)
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceDark)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { isOwnerDashboardUnlocked = false },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceCard)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimaryDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Secret Owner Vault 🔒",
                                color = TextPrimaryDark,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Confidential Creator Dashboard",
                                color = AuraSolarGold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = { isOwnerDashboardUnlocked = false },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = AuraSolarGold, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lock", color = AuraSolarGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            containerColor = BackgroundDark
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                OwnerDashboardScreen(
                    ownerVault = ownerVault,
                    vouchers = creatorVouchers,
                    startAppId = ownerStartAppId,
                    onOpenSettings = { viewModel.setShowUpiSettings(true) },
                    onRedeemVoucher = { type, amount -> viewModel.redeemVoucherWithoutBank(type, amount) },
                    onSimulateSale = { title, amount -> viewModel.simulateIncomingSale(title, amount) }
                )
            }
        }
    } else {
        // NORMAL USER APP (Clean, Gamified, Zero Admin Clues!)
        Scaffold(
            topBar = {
                AuraTopBar(
                    userProfile = userProfile,
                    onOpenNotifications = { viewModel.setShowNotificationModal(true) },
                    onSecretAdminTrigger = { showAdminPinDialog = true }
                )
            },
            bottomBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    StartAppBannerView(modifier = Modifier.fillMaxWidth())
                    NavigationBar(
                        containerColor = SurfaceDark,
                        tonalElevation = 4.dp
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { viewModel.setSelectedTab(0) },
                            icon = {
                                Icon(imageVector = Icons.Default.TaskAlt, contentDescription = "Quest")
                            },
                            label = { Text("Quest", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BackgroundDark,
                                selectedTextColor = AuraSolarGold,
                                indicatorColor = AuraSolarGold,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("tab_quest")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { viewModel.setSelectedTab(1) },
                            icon = {
                                Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = "Arena")
                            },
                            label = { Text("Arena", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BackgroundDark,
                                selectedTextColor = AuraSolarGold,
                                indicatorColor = AuraSolarGold,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("tab_arena")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { viewModel.setSelectedTab(2) },
                            icon = {
                                Icon(imageVector = Icons.Default.Link, contentDescription = "Habit Gear")
                            },
                            label = { Text("Gear", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BackgroundDark,
                                selectedTextColor = AuraNeonCyan,
                                indicatorColor = AuraNeonCyan,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("tab_gear")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 3,
                            onClick = { viewModel.setSelectedTab(3) },
                            icon = {
                                Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = "Armory")
                            },
                            label = { Text("Armory", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BackgroundDark,
                                selectedTextColor = AuraSolarGold,
                                indicatorColor = AuraSolarGold,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("tab_armory")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 4,
                            onClick = { viewModel.setSelectedTab(4) },
                            icon = {
                                Icon(imageVector = Icons.Default.BarChart, contentDescription = "Insights")
                            },
                            label = { Text("Insights", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BackgroundDark,
                                selectedTextColor = AuraSolarGold,
                                indicatorColor = AuraSolarGold,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("tab_insights")
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = BackgroundDark
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> HomeScreen(
                        userProfile = userProfile,
                        tasks = allTasks,
                        selectedRealm = selectedRealm,
                        onSelectRealm = { viewModel.setSelectedRealm(it) },
                        onStartTask = { viewModel.openTaskPlayer(it) },
                        onOpenAddCustom = { viewModel.setShowAddCustomDialog(true) },
                        onClaimMystery = { viewModel.claimDailyMystery(it) }
                    )
                    1 -> SocialScreen(
                        userProfile = userProfile,
                        clanBossHealth = clanBossHealth,
                        onNudgeBuddy = { viewModel.nudgeBuddy(it) }
                    )
                    2 -> GearLinksScreen(
                        userProfile = userProfile,
                        onOpenUrl = { url -> viewModel.openLink(context, url) },
                        onPayUpi = { amount, note -> viewModel.openPaymentChooser(note, amount) },
                        onOpenUpiSettings = { viewModel.setShowUpiSettings(true) }
                    )
                    3 -> ShopScreen(
                        userProfile = userProfile,
                        onPurchase = { cost, type, value -> viewModel.purchaseItem(cost, type, value) },
                        onWatchAd = { viewModel.watchRewardedAd() },
                        onBuyCoinPack = { costRs, coins -> viewModel.openPaymentChooser("Coin Pack ($coins Coins)", costRs.toDouble()) },
                        onBuyProPass = { plan -> viewModel.openPaymentChooser("AuraQuest Pro VIP", 149.0) }
                    )
                    4 -> StatsScreen(
                        userProfile = userProfile,
                        history = recentHistory,
                        gratitudeEntries = gratitudeEntries
                    )
                }
            }
        }
    }

    // Modal: Secret Admin PIN Login Dialog
    if (showAdminPinDialog) {
        SecretAdminLoginDialog(
            onDismiss = { showAdminPinDialog = false },
            onSuccess = {
                showAdminPinDialog = false
                isOwnerDashboardUnlocked = true
            }
        )
    }

    // Modal: Micro-Task Focused Session
    activeTask?.let { task ->
        MicroTaskPlayerModal(
            task = task,
            onClose = { viewModel.closeTaskPlayer() },
            onComplete = { taskId, xp, coins ->
                viewModel.completeTask(taskId, xp, coins)
            },
            onSaveGratitude = { text, mood ->
                viewModel.saveGratitude(text, mood)
            }
        )
    }

    // Modal: Add Custom Micro-Task
    if (showAddCustomDialog) {
        AddCustomTaskDialog(
            onDismiss = { viewModel.setShowAddCustomDialog(false) },
            onConfirm = { title, subtitle, category, durationMin ->
                viewModel.addCustomTask(title, subtitle, category, durationMin)
            }
        )
    }

    // Modal: Push Notification Planner & Triggers
    if (showNotificationModal) {
        NotificationPlannerModal(
            onDismiss = { viewModel.setShowNotificationModal(false) },
            onTestNotification = { title, body ->
                viewModel.triggerHaptic(100)
                viewModel.completeTask(-1, 0, 0)
            },
            onSaveSchedule = { morning, evening ->
                viewModel.updateReminderTimes(morning, evening)
            }
        )
    }

    // Modal: Payment Method Chooser (UPI vs Debit Card/Netbanking Web Link)
    pendingPayment?.let { (itemTitle, amountRs) ->
        PaymentMethodChooserDialog(
            itemTitle = itemTitle,
            amountRs = amountRs,
            onChooseUpi = {
                viewModel.payViaUpi(context, amountRs, itemTitle)
            },
            onChooseCardWeb = {
                viewModel.payViaCardWeb(context, amountRs, itemTitle)
            },
            onDismiss = { viewModel.dismissPaymentChooser() }
        )
    }

    // Modal: Owner Payment Settings Dialog (UPI + Web Card Page + Start.io Ads ID)
    if (showUpiSettingsDialog) {
        UpiSettingsDialog(
            currentUpi = ownerUpiId,
            currentWebUrl = ownerWebPaymentUrl,
            currentStartAppId = ownerStartAppId,
            onDismiss = { viewModel.setShowUpiSettings(false) },
            onSave = { newUpi, newWebUrl, newStartAppId ->
                viewModel.setOwnerPaymentConfig(newUpi, newWebUrl, newStartAppId)
            }
        )
    }

    // Modal: Daily Mystery Reward Celebration
    mysteryReward?.let { (coins, name) ->
        MysteryRewardDialog(
            rewardCoins = coins,
            rewardName = name,
            onDismiss = { viewModel.dismissMysteryDialog() }
        )
    }
}
