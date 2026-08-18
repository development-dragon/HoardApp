package com.hoardapp.ui.rewards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoardapp.data.Reward
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(viewModel: RewardsViewModel = viewModel()) {
    val rewards by viewModel.rewards.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingReward by remember { mutableStateOf<Reward?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.messages.collect { message ->
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rewards") },
                actions = {
                    Text(
                        text = "$totalPoints pts",
                        modifier = Modifier.padding(end = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add reward")
            }
        }
    ) { padding ->
        if (rewards.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No rewards yet. Tap + to add one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rewards, key = { it.id }) { reward ->
                    RewardRow(
                        reward = reward,
                        onRedeem = { viewModel.redeemReward(reward) },
                        onEdit = { editingReward = reward },
                        onDelete = { viewModel.deleteReward(reward) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        RewardDialog(
            dialogTitle = "New Reward",
            confirmLabel = "Add",
            onDismiss = { showAddDialog = false },
            onConfirm = { title, cost ->
                viewModel.addReward(title, cost)
                showAddDialog = false
            }
        )
    }

    editingReward?.let { reward ->
        RewardDialog(
            dialogTitle = "Edit Reward",
            confirmLabel = "Save",
            initialTitle = reward.title,
            initialCost = reward.cost.toString(),
            onDismiss = { editingReward = null },
            onConfirm = { title, cost ->
                viewModel.updateReward(reward, title, cost)
                editingReward = null
            }
        )
    }
}

@Composable
private fun RewardRow(reward: Reward, onRedeem: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(reward.title, fontWeight = FontWeight.SemiBold)
                Text("${reward.cost} pts")
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit reward")
            }
            IconButton(onClick = onRedeem) {
                Icon(Icons.Filled.Redeem, contentDescription = "Redeem reward")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete reward")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RewardDialog(
    dialogTitle: String,
    confirmLabel: String,
    initialTitle: String = "",
    initialCost: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var cost by remember { mutableStateOf(initialCost) }
    val costValue = cost.toIntOrNull()
    val isValid = title.isNotBlank() && costValue != null && costValue > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Reward name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = cost,
                    onValueChange = { input -> cost = input.filter { it.isDigit() } },
                    label = { Text("Cost (points)") },
                    singleLine = true,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (isValid) onConfirm(title.trim(), costValue!!) },
                enabled = isValid
            ) { Text(confirmLabel) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
