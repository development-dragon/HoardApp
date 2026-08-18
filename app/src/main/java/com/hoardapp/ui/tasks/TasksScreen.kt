package com.hoardapp.ui.tasks

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoardapp.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(viewModel: TasksViewModel = viewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    Text(
                        text = "$totalPoints pts",
                        modifier = Modifier.padding(end = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No tasks yet. Tap + to add one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onComplete = { viewModel.completeTask(task) },
                        onEdit = { editingTask = task },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        TaskDialog(
            dialogTitle = "New Task",
            confirmLabel = "Add",
            onDismiss = { showAddDialog = false },
            onConfirm = { title, points ->
                viewModel.addTask(title, points)
                showAddDialog = false
            }
        )
    }

    editingTask?.let { task ->
        TaskDialog(
            dialogTitle = "Edit Task",
            confirmLabel = "Save",
            initialTitle = task.title,
            initialPoints = task.points.toString(),
            onDismiss = { editingTask = null },
            onConfirm = { title, points ->
                viewModel.updateTask(task, title, points)
                editingTask = null
            }
        )
    }
}

@Composable
private fun TaskRow(task: Task, onComplete: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, fontWeight = FontWeight.SemiBold)
                Text("${task.points} pts")
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit task")
            }
            IconButton(onClick = onComplete) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Complete task")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete task")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDialog(
    dialogTitle: String,
    confirmLabel: String,
    initialTitle: String = "",
    initialPoints: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var points by remember { mutableStateOf(initialPoints) }
    val pointsValue = points.toIntOrNull()
    val isValid = title.isNotBlank() && pointsValue != null && pointsValue > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = points,
                    onValueChange = { input -> points = input.filter { it.isDigit() } },
                    label = { Text("Points") },
                    singleLine = true,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (isValid) onConfirm(title.trim(), pointsValue!!) },
                enabled = isValid
            ) { Text(confirmLabel) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
