package com.hoardapp.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hoardapp.HoardApplication
import com.hoardapp.data.RedeemedReward
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as HoardApplication).repository

    val totalPoints: StateFlow<Int> = repository.totalPoints.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), 0
    )
    val redeemedRewards: StateFlow<List<RedeemedReward>> = repository.redeemedRewards.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )

    fun removeRedeemedReward(redeemed: RedeemedReward) {
        viewModelScope.launch { repository.removeRedeemedReward(redeemed) }
    }
}
