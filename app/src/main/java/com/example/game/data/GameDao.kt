package com.example.game.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM player_profile WHERE id = 1")
    fun getProfileFlow(): Flow<PlayerProfileEntity?>

    @Query("SELECT * FROM player_profile WHERE id = 1")
    suspend fun getProfileSync(): PlayerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: PlayerProfileEntity)

    @Update
    suspend fun updateProfile(profile: PlayerProfileEntity)

    @Query("SELECT * FROM owned_bikes")
    fun getOwnedBikesFlow(): Flow<List<OwnedBikeEntity>>

    @Query("SELECT * FROM owned_bikes WHERE bikeId = :bikeId")
    suspend fun getBike(bikeId: String): OwnedBikeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBike(bike: OwnedBikeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBikes(bikes: List<OwnedBikeEntity>)

    @Query("SELECT * FROM career_levels")
    fun getLevelsFlow(): Flow<List<LevelProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLevel(level: LevelProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<LevelProgressEntity>)
}
