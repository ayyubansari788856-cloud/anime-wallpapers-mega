package com.example.game.data

import android.content.Context
import com.example.game.model.BikeCatalog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GameRepository(context: Context) {
    private val db = GameDatabase.getDatabase(context)
    private val dao = db.gameDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDefaultsIfEmpty()
        }
    }

    private suspend fun seedDefaultsIfEmpty() {
        if (dao.getProfileSync() == null) {
            dao.insertProfile(
                PlayerProfileEntity(
                    id = 1,
                    coins = 12000,
                    gems = 80,
                    highScore = 0,
                    totalDistanceMeters = 0,
                    totalOvertakes = 0,
                    currentCareerLevel = 1,
                    activeBikeId = "koma_100",
                    isVipMember = false,
                    hasAdFree = false,
                    freeRevivesAvailable = 3
                )
            )

            // Seed bikes
            val initialBikes = BikeCatalog.allBikes.mapIndexed { index, spec ->
                OwnedBikeEntity(
                    bikeId = spec.id,
                    isUnlocked = index == 0, // Koma 100 unlocked by default
                    speedLevel = 1,
                    accelLevel = 1,
                    handlingLevel = 1,
                    nitroLevel = 1,
                    selectedColorHex = spec.defaultColorHex,
                    selectedExhaustHex = 0xFF00E5FF
                )
            }
            dao.insertBikes(initialBikes)

            // Seed mission levels
            val levels = (1..20).map { lvl ->
                LevelProgressEntity(
                    levelId = lvl,
                    starsEarned = if (lvl == 1) 0 else 0,
                    isCompleted = false,
                    bestScore = 0,
                    grade = "A"
                )
            }
            dao.insertLevels(levels)
        }
    }

    val profileFlow: Flow<PlayerProfileEntity> = dao.getProfileFlow().map {
        it ?: PlayerProfileEntity()
    }

    val ownedBikesFlow: Flow<List<OwnedBikeEntity>> = dao.getOwnedBikesFlow()

    val levelsProgressFlow: Flow<List<LevelProgressEntity>> = dao.getLevelsFlow()

    suspend fun addCurrency(coins: Int, gems: Int) {
        val current = dao.getProfileSync() ?: PlayerProfileEntity()
        dao.updateProfile(
            current.copy(
                coins = (current.coins + coins).coerceAtLeast(0),
                gems = (current.gems + gems).coerceAtLeast(0)
            )
        )
    }

    suspend fun equipBike(bikeId: String) {
        val current = dao.getProfileSync() ?: return
        dao.updateProfile(current.copy(activeBikeId = bikeId))
    }

    suspend fun unlockBike(bikeId: String, costCoins: Int, costGems: Int): Boolean {
        val current = dao.getProfileSync() ?: return false
        if (current.coins < costCoins || current.gems < costGems) return false

        dao.updateProfile(
            current.copy(
                coins = current.coins - costCoins,
                gems = current.gems - costGems,
                activeBikeId = bikeId
            )
        )

        val bike = dao.getBike(bikeId) ?: OwnedBikeEntity(bikeId = bikeId)
        dao.insertOrUpdateBike(bike.copy(isUnlocked = true))
        return true
    }

    suspend fun upgradeBikeStat(bikeId: String, statType: String, costCoins: Int): Boolean {
        val current = dao.getProfileSync() ?: return false
        if (current.coins < costCoins) return false

        val bike = dao.getBike(bikeId) ?: return false
        val updatedBike = when (statType) {
            "SPEED" -> if (bike.speedLevel < 5) bike.copy(speedLevel = bike.speedLevel + 1) else return false
            "ACCEL" -> if (bike.accelLevel < 5) bike.copy(accelLevel = bike.accelLevel + 1) else return false
            "HANDLING" -> if (bike.handlingLevel < 5) bike.copy(handlingLevel = bike.handlingLevel + 1) else return false
            "FUEL" -> if (bike.nitroLevel < 5) bike.copy(nitroLevel = bike.nitroLevel + 1) else return false
            else -> return false
        }

        dao.updateProfile(current.copy(coins = current.coins - costCoins))
        dao.insertOrUpdateBike(updatedBike)
        return true
    }

    suspend fun customizeBike(bikeId: String, colorHex: Long?) {
        val bike = dao.getBike(bikeId) ?: return
        dao.insertOrUpdateBike(
            bike.copy(
                selectedColorHex = colorHex ?: bike.selectedColorHex
            )
        )
    }

    suspend fun recordMissionSuccess(missionId: Int, grade: String, coinsEarned: Int, rubiesEarned: Int, distanceM: Int) {
        val current = dao.getProfileSync() ?: return
        val nextLevel = maxOf(current.currentCareerLevel, missionId + 1)
        dao.updateProfile(
            current.copy(
                currentCareerLevel = nextLevel,
                totalDistanceMeters = current.totalDistanceMeters + distanceM,
                totalOvertakes = current.totalOvertakes + 1,
                coins = current.coins + coinsEarned,
                gems = current.gems + rubiesEarned,
                highScore = current.highScore + coinsEarned
            )
        )
        dao.insertOrUpdateLevel(
            LevelProgressEntity(
                levelId = missionId,
                starsEarned = if (grade == "S" || grade == "A+") 3 else if (grade == "A") 2 else 1,
                isCompleted = true,
                bestScore = coinsEarned,
                grade = grade
            )
        )
    }

    suspend fun claimStarterPack() {
        val current = dao.getProfileSync() ?: return
        dao.updateProfile(
            current.copy(
                coins = current.coins + 35000,
                gems = current.gems + 120,
                hasAdFree = true,
                freeRevivesAvailable = current.freeRevivesAvailable + 5,
                activeBikeId = "bari_250"
            )
        )
        val bari = dao.getBike("bari_250") ?: OwnedBikeEntity(bikeId = "bari_250")
        dao.insertOrUpdateBike(bari.copy(isUnlocked = true, speedLevel = 2, accelLevel = 2))
    }

    suspend fun claimVipPass(isMonthly: Boolean) {
        val current = dao.getProfileSync() ?: return
        val extraGems = if (isMonthly) 500 else 150
        val extraCoins = if (isMonthly) 100000 else 30000
        val targetBike = if (isMonthly) "venom_1000" else "mirage_400"

        dao.updateProfile(
            current.copy(
                coins = current.coins + extraCoins,
                gems = current.gems + extraGems,
                isVipMember = true,
                hasAdFree = true,
                freeRevivesAvailable = current.freeRevivesAvailable + 10,
                activeBikeId = targetBike
            )
        )
        val bike = dao.getBike(targetBike) ?: OwnedBikeEntity(bikeId = targetBike)
        dao.insertOrUpdateBike(bike.copy(isUnlocked = true))
    }
}
