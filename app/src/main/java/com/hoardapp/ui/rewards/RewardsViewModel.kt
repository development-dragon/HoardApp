package com.hoardapp.ui.rewards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hoardapp.HoardApplication
import com.hoardapp.data.Reward
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RewardsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as HoardApplication).repository

    val rewards: StateFlow<List<Reward>> = repository.rewards.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val totalPoints: StateFlow<Int> = repository.totalPoints.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), 0
    )

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages

    fun addReward(title: String, cost: Int) {
        viewModelScope.launch { repository.addReward(title, cost) }
    }

    fun deleteReward(reward: Reward) {
        viewModelScope.launch { repository.deleteReward(reward) }
    }

    fun redeemReward(reward: Reward) {
        viewModelScope.launch {
            val success = repository.redeemReward(reward)
            _messages.emit(
                if (success) "Redeemed \"${reward.title}\"" else "Not enough points for \"${reward.title}\""
            )
        }
    }
}
