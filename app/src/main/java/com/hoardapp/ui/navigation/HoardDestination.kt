package com.hoardapp.ui.navigation

sealed class HoardDestination(val route: String, val label: String) {
    data object Tasks : HoardDestination("tasks", "Tasks")
    data object Rewards : HoardDestination("rewards", "Rewards")
    data object Profile : HoardDestination("profile", "Profile")
}
