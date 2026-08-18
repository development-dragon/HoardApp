package com.hoardapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hoardapp.ui.navigation.HoardDestination
import com.hoardapp.ui.profile.ProfileScreen
import com.hoardapp.ui.rewards.RewardsScreen
import com.hoardapp.ui.tasks.TasksScreen

private val destinations = listOf(
    HoardDestination.Tasks,
    HoardDestination.Rewards,
    HoardDestination.Profile
)

@Composable
fun HoardApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                destinations.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (destination) {
                                    HoardDestination.Tasks -> Icons.Filled.Checklist
                                    HoardDestination.Rewards -> Icons.Filled.CardGiftcard
                                    HoardDestination.Profile -> Icons.Filled.Person
                                },
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HoardDestination.Tasks.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(HoardDestination.Tasks.route) { TasksScreen() }
            composable(HoardDestination.Rewards.route) { RewardsScreen() }
            composable(HoardDestination.Profile.route) { ProfileScreen() }
        }
    }
}
