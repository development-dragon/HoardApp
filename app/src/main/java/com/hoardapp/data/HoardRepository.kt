package com.hoardapp.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HoardRepository(private val db: AppDatabase) {
    private val taskDao = db.taskDao()
    private val rewardDao = db.rewardDao()
    private val redeemedRewardDao = db.redeemedRewardDao()
    private val profileDao = db.profileDao()

    val tasks: Flow<List<Task>> = taskDao.getAll()
    val rewards: Flow<List<Reward>> = rewardDao.getAll()
    val redeemedRewards: Flow<List<RedeemedReward>> = redeemedRewardDao.getAll()
    val totalPoints: Flow<Int> = profileDao.observe().map { it?.totalPoints ?: 0 }

    suspend fun addTask(title: String, points: Int) {
        taskDao.insert(Task(title = title, points = points))
    }

    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    /** Awards the task's points to the balance. The task itself stays in the list, so it can be reused. */
    suspend fun completeTask(task: Task) {
        db.withTransaction {
            val profile = profileDao.get() ?: Profile()
            profileDao.upsert(profile.copy(totalPoints = profile.totalPoints + task.points))
        }
    }

    suspend fun addReward(title: String, cost: Int) {
        rewardDao.insert(Reward(title = title, cost = cost))
    }

    suspend fun deleteReward(reward: Reward) {
        rewardDao.delete(reward)
    }

    /** Redeems [reward] if there are enough points. Returns false when the balance is insufficient. */
    suspend fun redeemReward(reward: Reward): Boolean {
        return db.withTransaction {
            val profile = profileDao.get() ?: Profile()
            if (profile.totalPoints < reward.cost) {
                false
            } else {
                profileDao.upsert(profile.copy(totalPoints = profile.totalPoints - reward.cost))
                redeemedRewardDao.insert(
                    RedeemedReward(rewardTitle = reward.title, cost = reward.cost)
                )
                true
            }
        }
    }

    /** Removes a redeemed reward entry and refunds its points to the balance. */
    suspend fun removeRedeemedReward(redeemed: RedeemedReward) {
        db.withTransaction {
            val profile = profileDao.get() ?: Profile()
            profileDao.upsert(profile.copy(totalPoints = profile.totalPoints + redeemed.cost))
            redeemedRewardDao.delete(redeemed)
        }
    }
}
