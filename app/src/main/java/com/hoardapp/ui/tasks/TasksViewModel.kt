package com.hoardapp.ui.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hoardapp.HoardApplication
import com.hoardapp.data.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as HoardApplication).repository

    val tasks: StateFlow<List<Task>> = repository.tasks.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val totalPoints: StateFlow<Int> = repository.totalPoints.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), 0
    )

    fun addTask(title: String, points: Int) {
        viewModelScope.launch { repository.addTask(title, points) }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch { repository.completeTask(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }
}
