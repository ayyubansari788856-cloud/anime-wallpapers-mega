package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Aura Traveler",
    val currentStreak: Int = 5,
    val bestStreak: Int = 14,
    val sunSparkEarnedToday: Boolean = false,
    val moonSparkEarnedToday: Boolean = false,
    val auraCoins: Int = 320,
    val totalXp: Int = 680,
    val level: Int = 3,
    val streakFreezes: Int = 2,
    val currentAvatar: String = "Zen Ember",
    val morningNotificationTime: String = "07:15 AM",
    val eveningNotificationTime: String = "09:30 PM",
    val lastActiveDate: String = "",
    val walletCashBalance: Double = 45.0,
    val availableScratchCards: Int = 1,
    val referralCode: String = "AURA88",
    val isChallengeActive: Boolean = true,
    val challengeDaysCompleted: Int = 4,
    val challengeTargetDays: Int = 7,
    val challengeStakeAmount: Double = 49.0,
    val isProMember: Boolean = false
)

@Entity(tableName = "micro_tasks")
data class MicroTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subtitle: String,
    val category: String, // "DAWN", "DUSK", "ANYTIME"
    val durationSeconds: Int = 120, // default 2 minutes
    val taskType: String = "TIMER", // "WATER", "BREATHING", "INTENTION", "JOURNAL", "TIMER"
    val xpReward: Int = 35,
    val coinReward: Int = 15,
    val isCompleted: Boolean = false,
    val isCustom: Boolean = false
)

@Entity(tableName = "daily_history")
data class DailyHistory(
    @PrimaryKey val dateStr: String, // YYYY-MM-DD
    val tasksCompleted: Int = 0,
    val sunSparkEarned: Boolean = false,
    val moonSparkEarned: Boolean = false,
    val xpEarned: Int = 0
)

@Entity(tableName = "gratitude_entries")
data class GratitudeEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateStr: String,
    val timestamp: Long = System.currentTimeMillis(),
    val text: String,
    val moodRating: Int = 5 // 1 to 5
)

@Entity(tableName = "cash_transactions")
data class CashTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val isCredit: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val paymentMethod: String = "UPI",
    val status: String = "COMPLETED"
)

@Entity(tableName = "owner_admin_vault")
data class OwnerAdminVault(
    @PrimaryKey val id: Int = 1,
    val totalRevenue: Double = 1840.0,
    val availableBalance: Double = 1840.0,
    val coinSalesCount: Int = 18,
    val proMembersCount: Int = 4,
    val lifelineSalesCount: Int = 12,
    val affiliateEarnings: Double = 420.0,
    val adRevenueEarnings: Double = 260.0
)

@Entity(tableName = "creator_vouchers")
data class CreatorVoucher(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val voucherType: String, // "AMAZON_PAY", "GOOGLE_PLAY", "FLIPKART", "MOBILE_RECHARGE"
    val voucherCode: String,
    val amount: Double,
    val dateCreated: String,
    val isRedeemed: Boolean = false
)
