package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AuraDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("SELECT * FROM micro_tasks ORDER BY category ASC, id ASC")
    fun getAllTasks(): Flow<List<MicroTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: MicroTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<MicroTask>)

    @Update
    suspend fun updateTask(task: MicroTask)

    @Query("UPDATE micro_tasks SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun setTaskCompleted(id: Long, isCompleted: Boolean)

    @Query("DELETE FROM micro_tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("UPDATE micro_tasks SET isCompleted = 0 WHERE isCustom = 0")
    suspend fun resetDailyDefaultTasks()

    @Query("SELECT * FROM daily_history ORDER BY dateStr DESC LIMIT 30")
    fun getRecentHistory(): Flow<List<DailyHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyHistory(history: DailyHistory)

    @Query("SELECT * FROM gratitude_entries ORDER BY timestamp DESC LIMIT 50")
    fun getGratitudeEntries(): Flow<List<GratitudeEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGratitudeEntry(entry: GratitudeEntry): Long

    @Query("SELECT * FROM cash_transactions ORDER BY timestamp DESC LIMIT 30")
    fun getCashTransactions(): Flow<List<CashTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashTransaction(transaction: CashTransaction): Long

    @Query("SELECT * FROM owner_admin_vault WHERE id = 1 LIMIT 1")
    fun getOwnerVault(): Flow<OwnerAdminVault?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateVault(vault: OwnerAdminVault)

    @Query("SELECT * FROM creator_vouchers ORDER BY id DESC")
    fun getCreatorVouchers(): Flow<List<CreatorVoucher>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreatorVoucher(voucher: CreatorVoucher): Long

    @Query("UPDATE creator_vouchers SET isRedeemed = 1 WHERE id = :id")
    suspend fun markVoucherRedeemed(id: Long)
}
