package com.hoardapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "redeemed_rewards")
data class RedeemedReward(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rewardTitle: String,
    val cost: Int,
    val redeemedAt: Long = System.currentTimeMillis()
)

@Dao
interface RedeemedRewardDao {
    @Query("SELECT * FROM redeemed_rewards ORDER BY redeemedAt DESC")
    fun getAll(): Flow<List<RedeemedReward>>

    @Insert
    suspend fun insert(redeemedReward: RedeemedReward): Long

    @Delete
    suspend fun delete(redeemedReward: RedeemedReward)
}
