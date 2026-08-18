package com.hoardapp.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Single-row table holding the user's current point balance. */
@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey val id: Int = 0,
    val totalPoints: Int = 0
)

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE id = 0")
    fun observe(): Flow<Profile?>

    @Query("SELECT * FROM profile WHERE id = 0")
    suspend fun get(): Profile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: Profile)
}
