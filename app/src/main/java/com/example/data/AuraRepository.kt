package com.example.data

import com.example.data.local.AuraDao
import com.example.data.local.CashTransaction
import com.example.data.local.CreatorVoucher
import com.example.data.local.DailyHistory
import com.example.data.local.GratitudeEntry
import com.example.data.local.MicroTask
import com.example.data.local.OwnerAdminVault
import com.example.data.local.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class AuraRepository(private val dao: AuraDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val allTasks: Flow<List<MicroTask>> = dao.getAllTasks()
    val recentHistory: Flow<List<DailyHistory>> = dao.getRecentHistory()
    val gratitudeEntries: Flow<List<GratitudeEntry>> = dao.getGratitudeEntries()
    val cashTransactions: Flow<List<CashTransaction>> = dao.getCashTransactions()
    val ownerVault: Flow<OwnerAdminVault?> = dao.getOwnerVault()
    val creatorVouchers: Flow<List<CreatorVoucher>> = dao.getCreatorVouchers()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    suspend fun initializeIfNeeded() {
        val profile = dao.getUserProfile().firstOrNull()
        val todayStr = dateFormat.format(Date())

        if (profile == null) {
            val initialProfile = UserProfile(
                id = 1,
                name = "Aura Seeker",
                currentStreak = 4,
                bestStreak = 12,
                sunSparkEarnedToday = false,
                moonSparkEarnedToday = false,
                auraCoins = 240,
                totalXp = 540,
                level = 2,
                streakFreezes = 2,
                currentAvatar = "Zen Ember",
                morningNotificationTime = "07:15 AM",
                eveningNotificationTime = "09:30 PM",
                lastActiveDate = todayStr,
                walletCashBalance = 45.0,
                availableScratchCards = 1,
                referralCode = "AURA88",
                isChallengeActive = true,
                challengeDaysCompleted = 4,
                challengeTargetDays = 7,
                challengeStakeAmount = 49.0,
                isProMember = false
            )
            dao.insertOrUpdateProfile(initialProfile)

            // Seed default tasks
            val defaultTasks = listOf(
                MicroTask(
                    title = "Hydrate & Awaken",
                    subtitle = "Drink 1 glass of water to wake up your body",
                    category = "DAWN",
                    durationSeconds = 90,
                    taskType = "WATER",
                    xpReward = 40,
                    coinReward = 15
                ),
                MicroTask(
                    title = "Top 3 Daily Intentions",
                    subtitle = "Set 3 high-impact goals for today",
                    category = "DAWN",
                    durationSeconds = 120,
                    taskType = "INTENTION",
                    xpReward = 50,
                    coinReward = 20
                ),
                MicroTask(
                    title = "4-7-8 Box Breathing",
                    subtitle = "3-minute calm morning breathwork session",
                    category = "DAWN",
                    durationSeconds = 180,
                    taskType = "BREATHING",
                    xpReward = 45,
                    coinReward = 15
                ),
                MicroTask(
                    title = "Daily Brain Dump & Gratitude",
                    subtitle = "Reflect on one positive event today",
                    category = "DUSK",
                    durationSeconds = 120,
                    taskType = "JOURNAL",
                    xpReward = 50,
                    coinReward = 20
                ),
                MicroTask(
                    title = "Tomorrow's Anchor",
                    subtitle = "Note tomorrow's #1 morning priority",
                    category = "DUSK",
                    durationSeconds = 90,
                    taskType = "INTENTION",
                    xpReward = 40,
                    coinReward = 15
                ),
                MicroTask(
                    title = "Screen Detach Pledge",
                    subtitle = "Commit to disconnecting 30m before bed",
                    category = "DUSK",
                    durationSeconds = 180,
                    taskType = "TIMER",
                    xpReward = 45,
                    coinReward = 15
                )
            )
            dao.insertTasks(defaultTasks)

            // Seed past history
            val pastDates = listOf(
                DailyHistory(dateStr = "2026-09-26", tasksCompleted = 4, sunSparkEarned = true, moonSparkEarned = true, xpEarned = 180),
                DailyHistory(dateStr = "2026-09-27", tasksCompleted = 3, sunSparkEarned = true, moonSparkEarned = true, xpEarned = 140),
                DailyHistory(dateStr = "2026-09-28", tasksCompleted = 5, sunSparkEarned = true, moonSparkEarned = true, xpEarned = 210),
                DailyHistory(dateStr = "2026-09-29", tasksCompleted = 4, sunSparkEarned = true, moonSparkEarned = true, xpEarned = 175)
            )
            pastDates.forEach { dao.insertDailyHistory(it) }
        }

        // Initialize Owner Vault if not present
        val vault = dao.getOwnerVault().firstOrNull()
        if (vault == null) {
            val initialVault = OwnerAdminVault(
                id = 1,
                totalRevenue = 1840.0,
                availableBalance = 1840.0,
                coinSalesCount = 18,
                proMembersCount = 4,
                lifelineSalesCount = 12,
                affiliateEarnings = 420.0,
                adRevenueEarnings = 260.0
            )
            dao.insertOrUpdateVault(initialVault)

            val initialVoucher = CreatorVoucher(
                voucherType = "AMAZON_PAY",
                voucherCode = "AMZN-9284-AURA-7193",
                amount = 250.0,
                dateCreated = todayStr,
                isRedeemed = false
            )
            dao.insertCreatorVoucher(initialVoucher)
        }
    }

    suspend fun completeTask(taskId: Long, xpEarned: Int, coinsEarned: Int) {
        dao.setTaskCompleted(taskId, true)
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val tasks = dao.getAllTasks().firstOrNull() ?: emptyList()

        val updatedTask = tasks.find { it.id == taskId } ?: return
        var newSunSpark = profile.sunSparkEarnedToday
        var newMoonSpark = profile.moonSparkEarnedToday

        if (updatedTask.category == "DAWN") newSunSpark = true
        if (updatedTask.category == "DUSK") newMoonSpark = true

        val newTotalXp = profile.totalXp + xpEarned
        val newLevel = calculateLevel(newTotalXp)
        val newCoins = profile.auraCoins + coinsEarned

        var currentStreak = profile.currentStreak
        if (newSunSpark && newMoonSpark && (!profile.sunSparkEarnedToday || !profile.moonSparkEarnedToday)) {
            currentStreak += 1
        }
        val bestStreak = maxOf(currentStreak, profile.bestStreak)

        val updatedProfile = profile.copy(
            sunSparkEarnedToday = newSunSpark,
            moonSparkEarnedToday = newMoonSpark,
            totalXp = newTotalXp,
            level = newLevel,
            auraCoins = newCoins,
            currentStreak = currentStreak,
            bestStreak = bestStreak
        )
        dao.insertOrUpdateProfile(updatedProfile)

        val todayStr = dateFormat.format(Date())
        val completedCount = tasks.count { it.isCompleted || it.id == taskId }
        dao.insertDailyHistory(
            DailyHistory(
                dateStr = todayStr,
                tasksCompleted = completedCount,
                sunSparkEarned = newSunSpark,
                moonSparkEarned = newMoonSpark,
                xpEarned = newTotalXp
            )
        )
    }

    suspend fun fulfillPurchase(itemTitle: String, amount: Double) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        when {
            itemTitle.contains("500", ignoreCase = true) || amount == 29.0 -> {
                dao.insertOrUpdateProfile(profile.copy(auraCoins = profile.auraCoins + 500))
            }
            itemTitle.contains("1,500", ignoreCase = true) || itemTitle.contains("1500", ignoreCase = true) || amount == 69.0 -> {
                dao.insertOrUpdateProfile(profile.copy(auraCoins = profile.auraCoins + 1500))
            }
            itemTitle.contains("5,000", ignoreCase = true) || itemTitle.contains("5000", ignoreCase = true) || amount == 199.0 -> {
                dao.insertOrUpdateProfile(profile.copy(auraCoins = profile.auraCoins + 5000))
            }
            itemTitle.contains("Pro", ignoreCase = true) || amount == 149.0 -> {
                dao.insertOrUpdateProfile(profile.copy(isProMember = true, streakFreezes = profile.streakFreezes + 5))
            }
            itemTitle.contains("Lifeline", ignoreCase = true) || amount == 19.0 -> {
                dao.insertOrUpdateProfile(profile.copy(streakFreezes = profile.streakFreezes + 1))
            }
            else -> {
                dao.insertOrUpdateProfile(profile.copy(auraCoins = profile.auraCoins + (amount * 15).toInt()))
            }
        }
    }

    suspend fun recordOwnerSale(itemTitle: String, amount: Double) {
        val vault = dao.getOwnerVault().firstOrNull() ?: return
        val newTotal = vault.totalRevenue + amount
        val newBalance = vault.availableBalance + amount
        val updated = when {
            itemTitle.contains("Coin", ignoreCase = true) -> vault.copy(
                totalRevenue = newTotal,
                availableBalance = newBalance,
                coinSalesCount = vault.coinSalesCount + 1
            )
            itemTitle.contains("Pro", ignoreCase = true) -> vault.copy(
                totalRevenue = newTotal,
                availableBalance = newBalance,
                proMembersCount = vault.proMembersCount + 1
            )
            else -> vault.copy(
                totalRevenue = newTotal,
                availableBalance = newBalance,
                lifelineSalesCount = vault.lifelineSalesCount + 1
            )
        }
        dao.insertOrUpdateVault(updated)
        fulfillPurchase(itemTitle, amount)
    }

    suspend fun generateCreatorVoucher(type: String, amount: Double): CreatorVoucher? {
        val vault = dao.getOwnerVault().firstOrNull() ?: return null
        if (vault.availableBalance < amount) return null

        val newBalance = vault.availableBalance - amount
        dao.insertOrUpdateVault(vault.copy(availableBalance = newBalance))

        val randomSuffix = Random.nextInt(1000, 9999)
        val randomPrefix = Random.nextInt(1000, 9999)
        val prefix = when (type) {
            "AMAZON_PAY" -> "AMZN"
            "GOOGLE_PLAY" -> "GPLAY"
            "FLIPKART" -> "FLIP"
            else -> "MOBR"
        }
        val code = "$prefix-$randomPrefix-AURA-$randomSuffix"

        val todayStr = dateFormat.format(Date())
        val voucher = CreatorVoucher(
            voucherType = type,
            voucherCode = code,
            amount = amount,
            dateCreated = todayStr,
            isRedeemed = false
        )
        val id = dao.insertCreatorVoucher(voucher)
        return voucher.copy(id = id)
    }

    suspend fun addCustomTask(title: String, subtitle: String, category: String, durationMin: Int) {
        val task = MicroTask(
            title = title,
            subtitle = subtitle,
            category = category,
            durationSeconds = durationMin * 60,
            taskType = "TIMER",
            xpReward = 30 + (durationMin * 5),
            coinReward = 15,
            isCompleted = false,
            isCustom = true
        )
        dao.insertTask(task)
    }

    suspend fun saveGratitudeEntry(text: String, mood: Int) {
        val todayStr = dateFormat.format(Date())
        dao.insertGratitudeEntry(
            GratitudeEntry(
                dateStr = todayStr,
                text = text,
                moodRating = mood
            )
        )
    }

    suspend fun purchaseShopItem(cost: Int, itemType: String, itemValue: String): Boolean {
        val profile = dao.getUserProfile().firstOrNull() ?: return false
        if (profile.auraCoins < cost) return false

        val newCoins = profile.auraCoins - cost
        val updated = when (itemType) {
            "STREAK_FREEZE" -> profile.copy(auraCoins = newCoins, streakFreezes = profile.streakFreezes + 1)
            "AVATAR" -> profile.copy(auraCoins = newCoins, currentAvatar = itemValue)
            "XP_BOOST" -> profile.copy(auraCoins = newCoins, totalXp = profile.totalXp + 150, level = calculateLevel(profile.totalXp + 150))
            else -> profile.copy(auraCoins = newCoins)
        }
        dao.insertOrUpdateProfile(updated)
        return true
    }

    suspend fun rewardAdBonus(): Int {
        val profile = dao.getUserProfile().firstOrNull() ?: return 0
        val bonusCoins = 100
        val bonusXp = 50
        val newXp = profile.totalXp + bonusXp
        dao.insertOrUpdateProfile(
            profile.copy(
                auraCoins = profile.auraCoins + bonusCoins,
                totalXp = newXp,
                level = calculateLevel(newXp)
            )
        )
        return bonusCoins
    }

    suspend fun claimMysteryReward(isMorning: Boolean): Pair<Int, String> {
        val profile = dao.getUserProfile().firstOrNull() ?: return Pair(0, "")
        val rewardCoins = if (isMorning) 60 else 45
        val rewardName = if (isMorning) "Morning Solar Chest" else "Evening Lunar Pearl"
        val updated = profile.copy(
            auraCoins = profile.auraCoins + rewardCoins,
            totalXp = profile.totalXp + 30,
            level = calculateLevel(profile.totalXp + 30)
        )
        dao.insertOrUpdateProfile(updated)
        return Pair(rewardCoins, rewardName)
    }

    suspend fun updateReminderTimes(morningTime: String, eveningTime: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        dao.insertOrUpdateProfile(
            profile.copy(
                morningNotificationTime = morningTime,
                eveningNotificationTime = eveningTime
            )
        )
    }

    private fun calculateLevel(xp: Int): Int {
        return (xp / 250) + 1
    }
}
