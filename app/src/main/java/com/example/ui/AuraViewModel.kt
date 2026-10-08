package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AuraRepository
import com.example.data.local.AuraDatabase
import com.example.data.local.CashTransaction
import com.example.data.local.CreatorVoucher
import com.example.data.local.DailyHistory
import com.example.data.local.GratitudeEntry
import com.example.data.local.MicroTask
import com.example.data.local.OwnerAdminVault
import com.example.data.local.UserProfile
import com.example.ui.components.InterstitialAdHelper
import com.example.ui.components.StartAppConfig
import com.example.util.LinkPaymentHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class AuraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuraRepository
    private val vibrator = application.getSystemService(Vibrator::class.java)

    val userProfile: StateFlow<UserProfile?>
    val allTasks: StateFlow<List<MicroTask>>
    val recentHistory: StateFlow<List<DailyHistory>>
    val gratitudeEntries: StateFlow<List<GratitudeEntry>>
    val cashTransactions: StateFlow<List<CashTransaction>>
    val ownerVault: StateFlow<OwnerAdminVault?>
    val creatorVouchers: StateFlow<List<CreatorVoucher>>

    // Owner Payment Destinations
    private val _ownerUpiId = MutableStateFlow("vajid2948@upi")
    val ownerUpiId: StateFlow<String> = _ownerUpiId.asStateFlow()

    private val _ownerWebPaymentUrl = MutableStateFlow("https://pages.razorpay.com/auraquest")
    val ownerWebPaymentUrl: StateFlow<String> = _ownerWebPaymentUrl.asStateFlow()

    private val _ownerStartAppId = MutableStateFlow(StartAppConfig.getAppId(application))
    val ownerStartAppId: StateFlow<String> = _ownerStartAppId.asStateFlow()

    private val _showUpiSettingsDialog = MutableStateFlow(false)
    val showUpiSettingsDialog: StateFlow<Boolean> = _showUpiSettingsDialog.asStateFlow()

    // Payment Chooser Modal State: Pair(itemTitle, amountRs)
    private val _pendingPaymentItem = MutableStateFlow<Pair<String, Double>?>(null)
    val pendingPaymentItem: StateFlow<Pair<String, Double>?> = _pendingPaymentItem.asStateFlow()

    // Realm: "DAWN" (Morning), "DUSK" (Evening), or "ALL"
    private val _selectedRealm = MutableStateFlow(determineDefaultRealm())
    val selectedRealm: StateFlow<String> = _selectedRealm.asStateFlow()

    // Navigation: 0=Quest, 1=Arena, 2=Gear/Links, 3=Armory, 4=Insights, 5=Owner Dashboard
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _activeTask = MutableStateFlow<MicroTask?>(null)
    val activeTask: StateFlow<MicroTask?> = _activeTask.asStateFlow()

    private val _showAddCustomDialog = MutableStateFlow(false)
    val showAddCustomDialog: StateFlow<Boolean> = _showAddCustomDialog.asStateFlow()

    private val _showNotificationModal = MutableStateFlow(false)
    val showNotificationModal: StateFlow<Boolean> = _showNotificationModal.asStateFlow()

    private val _mysteryRewardResult = MutableStateFlow<Pair<Int, String>?>(null)
    val mysteryRewardResult: StateFlow<Pair<Int, String>?> = _mysteryRewardResult.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    // Clan Boss Dynamic State
    private val _clanBossHealth = MutableStateFlow(3250) // Max 5000
    val clanBossHealth: StateFlow<Int> = _clanBossHealth.asStateFlow()

    init {
        val database = AuraDatabase.getDatabase(application)
        repository = AuraRepository(database.auraDao())

        userProfile = repository.userProfile.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        allTasks = repository.allTasks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        recentHistory = repository.recentHistory.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        gratitudeEntries = repository.gratitudeEntries.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        cashTransactions = repository.cashTransactions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        ownerVault = repository.ownerVault.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        creatorVouchers = repository.creatorVouchers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.initializeIfNeeded()
        }
    }

    private fun determineDefaultRealm(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return if (hour in 5..14) "DAWN" else "DUSK"
    }

    fun setSelectedRealm(realm: String) {
        _selectedRealm.value = realm
        triggerHaptic(50)
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun setOwnerPaymentConfig(newUpi: String, newWebUrl: String, newStartAppId: String = "209978529") {
        _ownerUpiId.value = newUpi
        _ownerWebPaymentUrl.value = newWebUrl
        _ownerStartAppId.value = newStartAppId
        StartAppConfig.saveAppId(getApplication(), newStartAppId)
        _showUpiSettingsDialog.value = false
        _snackBarMessage.value = "Settings Saved! Start.io ID: $newStartAppId | UPI: $newUpi"
        triggerHaptic(80)
    }

    fun setShowUpiSettings(show: Boolean) {
        _showUpiSettingsDialog.value = show
    }

    fun openPaymentChooser(itemTitle: String, amountRs: Double) {
        _pendingPaymentItem.value = Pair(itemTitle, amountRs)
        triggerHaptic(60)
    }

    fun dismissPaymentChooser() {
        _pendingPaymentItem.value = null
    }

    fun payViaUpi(context: Context, amount: Double, note: String) {
        triggerHaptic(100)
        LinkPaymentHelper.launchUpiPayment(
            context = context,
            upiId = _ownerUpiId.value,
            payeeName = "AuraQuest",
            amount = amount,
            note = note
        )
        viewModelScope.launch {
            repository.recordOwnerSale(note, amount)
        }
        _pendingPaymentItem.value = null
        _snackBarMessage.value = "Opening UPI app to transfer ₹${amount.toInt()} directly to owner!"
    }

    fun payViaCardWeb(context: Context, amount: Double, itemTitle: String) {
        triggerHaptic(100)
        LinkPaymentHelper.launchCardWebPayment(
            context = context,
            webPaymentUrl = _ownerWebPaymentUrl.value,
            amount = amount,
            itemTitle = itemTitle
        )
        viewModelScope.launch {
            repository.recordOwnerSale(itemTitle, amount)
        }
        _pendingPaymentItem.value = null
        _snackBarMessage.value = "Opening Secure Web Payment page for ATM Card / Netbanking!"
    }

    fun redeemVoucherWithoutBank(type: String, amount: Double) {
        viewModelScope.launch {
            val voucher = repository.generateCreatorVoucher(type, amount)
            if (voucher != null) {
                triggerHaptic(150)
                _snackBarMessage.value = "Badhaai! ${voucher.voucherType} Voucher Code (${voucher.voucherCode}) generate ho gaya! Bina bank ke turant use karein!"
            } else {
                _snackBarMessage.value = "Insufficient balance in Owner Vault to redeem ₹${amount.toInt()}!"
            }
        }
    }

    fun simulateIncomingSale(itemTitle: String, amount: Double) {
        viewModelScope.launch {
            repository.recordOwnerSale(itemTitle, amount)
            triggerHaptic(100)
            _snackBarMessage.value = "New User Purchase: ₹${amount.toInt()} added to Owner Vault!"
        }
    }

    fun openLink(context: Context, url: String) {
        triggerHaptic(50)
        LinkPaymentHelper.openExternalLink(context, url)
    }

    fun openTaskPlayer(task: MicroTask) {
        _activeTask.value = task
        triggerHaptic(60)
    }

    fun closeTaskPlayer() {
        _activeTask.value = null
    }

    fun completeTask(taskId: Long, xp: Int, coins: Int) {
        viewModelScope.launch {
            repository.completeTask(taskId, xp, coins)
            triggerHaptic(120)
            _clanBossHealth.value = maxOf(0, _clanBossHealth.value - (xp * 3))
            _snackBarMessage.value = "Awesome! +$xp XP and +$coins Aura Coins earned!"
            _activeTask.value = null
        }
    }

    fun setShowAddCustomDialog(show: Boolean) {
        _showAddCustomDialog.value = show
    }

    fun setShowNotificationModal(show: Boolean) {
        _showNotificationModal.value = show
    }

    fun addCustomTask(title: String, subtitle: String, category: String, durationMin: Int) {
        viewModelScope.launch {
            repository.addCustomTask(title, subtitle, category, durationMin)
            _showAddCustomDialog.value = false
            _snackBarMessage.value = "New custom micro-task added to $category realm!"
            triggerHaptic(50)
        }
    }

    fun saveGratitude(text: String, mood: Int) {
        viewModelScope.launch {
            repository.saveGratitudeEntry(text, mood)
            _snackBarMessage.value = "Gratitude recorded. Mind calm +1!"
        }
    }

    fun purchaseItem(cost: Int, itemType: String, itemValue: String) {
        viewModelScope.launch {
            val success = repository.purchaseShopItem(cost, itemType, itemValue)
            if (success) {
                triggerHaptic(100)
                _snackBarMessage.value = "Unlocked $itemValue successfully!"
            } else {
                _snackBarMessage.value = "Not enough Aura Coins! Complete tasks or get a Coin Pack."
            }
        }
    }

    fun watchRewardedAd() {
        viewModelScope.launch {
            val bonus = repository.rewardAdBonus()
            triggerHaptic(150)
            _snackBarMessage.value = "Rewarded Ad Bonus: +$bonus Aura Coins & +50 XP!"
        }
    }

    fun claimDailyMystery(isMorning: Boolean) {
        viewModelScope.launch {
            val (coins, name) = repository.claimMysteryReward(isMorning)
            _mysteryRewardResult.value = Pair(coins, name)
            triggerHaptic(150)
        }
    }

    fun dismissMysteryDialog() {
        _mysteryRewardResult.value = null
    }

    fun clearSnackBar() {
        _snackBarMessage.value = null
    }

    fun nudgeBuddy(buddyName: String) {
        triggerHaptic(80)
        _snackBarMessage.value = "Nudge sent to $buddyName! 'Keep the streak alive bro!'"
    }

    fun updateReminderTimes(morning: String, evening: String) {
        viewModelScope.launch {
            repository.updateReminderTimes(morning, evening)
            _snackBarMessage.value = "Reminders set: Morning ($morning) & Evening ($evening)"
        }
    }

    fun triggerHaptic(durationMs: Long = 60) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) { }
    }
}
