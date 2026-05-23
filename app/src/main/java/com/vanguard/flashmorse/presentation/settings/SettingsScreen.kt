package com.vanguard.flashmorse.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDark by viewModel.isDarkMode.collectAsState()
    val isVibrationEnabled by viewModel.isVibrationEnabled.collectAsState()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsState()

    val backgroundColor = if (isDark) Color(0xFF000000) else Color(0xFFE8DFD3)
    val primaryTextColor = if (isDark) Color(0xFFFFFFFF) else Color(0xFF4A453E)
    val secondaryTextColor = if (isDark) Color(0xFFB0B0B0) else Color(0xFF8B8479)
    val dividerColor = if (isDark) Color(0xFF222222) else Color(0xFFD1C7B7)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SETTINGS", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = primaryTextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = primaryTextColor
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "GENERAL",
                style = MaterialTheme.typography.labelMedium,
                color = secondaryTextColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                title = "Dark Mode",
                description = "Use dark theme for the interface",
                checked = isDark,
                onCheckedChange = viewModel::setDarkMode,
                primaryTextColor = primaryTextColor,
                isDark = isDark
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = dividerColor)
            
            Text(
                "TRANSMISSION",
                style = MaterialTheme.typography.labelMedium,
                color = secondaryTextColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                title = "Flashlight Vibration",
                description = "Vibrate device while transmitting",
                checked = isVibrationEnabled,
                onCheckedChange = viewModel::setVibrationEnabled,
                primaryTextColor = primaryTextColor,
                isDark = isDark
            )
            
            SettingItem(
                title = "Sound Feedback",
                description = "Play beeps during signal detection",
                checked = isSoundEnabled,
                onCheckedChange = viewModel::setSoundEnabled,
                primaryTextColor = primaryTextColor,
                isDark = isDark
            )

            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                "Flash Morse v1.0.0",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    primaryTextColor: Color,
    isDark: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = primaryTextColor
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDark) Color(0xFF888888) else Color.Gray
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFFFA500),
                checkedTrackColor = Color(0xFFFFA500).copy(alpha = 0.5f),
                uncheckedThumbColor = if (isDark) Color.DarkGray else Color.White,
                uncheckedTrackColor = if (isDark) Color(0xFF222222) else Color(0xFFCCCCCC)
            )
        )
    }
}
