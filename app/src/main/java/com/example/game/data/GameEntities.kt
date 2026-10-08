package com.example.game.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val coins: Int = 8500,
    val gems: Int = 80,
    val highScore: Int = 0,
    val totalDistanceMeters: Int = 0,
    val totalOvertakes: Int = 0,
    val currentCareerLevel: Int = 1,
    val activeBikeId: String = "koma_100",
    val isVipMember: Boolean = false,
    val hasAdFree: Boolean = false,
    val freeRevivesAvailable: Int = 3
)

@Entity(tableName = "owned_bikes")
data class OwnedBikeEntity(
    @PrimaryKey val bikeId: String,
    val isUnlocked: Boolean = false,
    val speedLevel: Int = 1,
    val accelLevel: Int = 1,
    val handlingLevel: Int = 1,
    val nitroLevel: Int = 1,
    val selectedColorHex: Long = 0xFFE50914,
    val selectedExhaustHex: Long = 0xFF00E5FF
)

@Entity(tableName = "career_levels")
data class LevelProgressEntity(
    @PrimaryKey val levelId: Int,
    val starsEarned: Int = 0,
    val isCompleted: Boolean = false,
    val bestScore: Int = 0,
    val grade: String = "A"
)
