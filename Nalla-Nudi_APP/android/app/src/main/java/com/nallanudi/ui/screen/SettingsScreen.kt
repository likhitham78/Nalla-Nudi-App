package com.nallanudi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nallanudi.ui.theme.*
import com.nallanudi.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Sound toggle
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Blue100, modifier = Modifier.size(44.dp)) {
                            Icon(
                                if (state.soundEnabled) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                                contentDescription = null, tint = Blue600, modifier = Modifier.padding(10.dp).size(24.dp),
                            )
                        }
                        Column {
                            Text("Sound", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                            Text(
                                if (state.soundEnabled) "Pronunciation enabled" else "Pronunciation disabled",
                                style = MaterialTheme.typography.bodyMedium, color = Slate600,
                            )
                        }
                    }
                    Switch(
                        checked = state.soundEnabled,
                        onCheckedChange = { viewModel.toggleSound() },
                        colors = SwitchDefaults.colors(checkedTrackColor = Blue600),
                    )
                }
            }

            // Reset progress
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Red100, modifier = Modifier.size(44.dp)) {
                            Icon(Icons.Filled.DeleteForever, contentDescription = null, tint = Red600, modifier = Modifier.padding(10.dp).size(24.dp))
                        }
                        Column {
                            Text("Reset Progress", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                            Text("Clear all learning data", style = MaterialTheme.typography.bodyMedium, color = Slate600)
                        }
                    }
                    IconButton(onClick = { showResetDialog = true }) {
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Slate300)
                    }
                }
            }

            // About
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Surface(shape = RoundedCornerShape(12.dp), color = Teal100, modifier = Modifier.size(44.dp)) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = Teal600, modifier = Modifier.padding(10.dp).size(24.dp))
                    }
                    Column {
                        Text("About", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                        Text("Nalla-Nudi v1.0", style = MaterialTheme.typography.bodyMedium, color = Slate600)
                        Text("Bridge Dictionary for Kannada students", style = MaterialTheme.typography.bodyMedium, color = Slate600)
                    }
                }
            }
        }
    }

    // Reset confirmation dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress?", fontWeight = FontWeight.Bold) },
            text = { Text("This will clear all your learning progress, streak, and saved words. This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetProgress()
                        showResetDialog = false
                    },
                ) { Text("Reset", color = Red600, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel", fontWeight = FontWeight.Bold) }
            },
        )
    }
}
