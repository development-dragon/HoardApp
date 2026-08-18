package com.hoardapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val cost: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Reward>>

    @Insert
    suspend fun insert(reward: Reward): Long

    @Delete
    suspend fun delete(reward: Reward)
}
