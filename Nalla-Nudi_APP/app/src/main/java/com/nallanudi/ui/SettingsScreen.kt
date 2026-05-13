package com.nallanudi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(viewModel: com.nallanudi.viewmodel.MainViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val remindersEnabled by viewModel.remindersEnabled.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("General", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        item {
            SettingsItem(
                icon = Icons.Default.Language,
                title = "App Language",
                subtitle = "Current: $appLanguage",
                onClick = { viewModel.toggleLanguage() }
            )
        }
        item {
            SettingsItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                subtitle = when(isDarkMode) {
                    true -> "On"
                    false -> "Off"
                    null -> "System Default"
                },
                onClick = { viewModel.toggleDarkMode() }
            )
        }
        item {
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Study Reminders",
                subtitle = if (remindersEnabled) "Enabled" else "Disabled",
                onClick = { viewModel.toggleReminders() }
            )
        }
        
        item { Spacer(modifier = Modifier.height(8.dp)) }
        
        item {
            Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        item {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "Version",
                subtitle = "1.0.0 (Native Release)",
                onClick = {}
            )
        }
        item {
            SettingsItem(
                icon = Icons.Default.Share,
                title = "Share Nalla-Nudi",
                subtitle = "Invite friends to learn technical terms",
                onClick = { viewModel.shareApp(context) }
            )
        }
        item {
            SettingsItem(
                icon = Icons.Default.Star,
                title = "Rate App",
                subtitle = "Help us improve",
                onClick = { viewModel.rateApp(context) }
            )
        }
        
        item {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("Made with ❤️ in Karnataka", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
