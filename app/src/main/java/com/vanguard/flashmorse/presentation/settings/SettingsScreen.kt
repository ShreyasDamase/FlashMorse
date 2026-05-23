package com.vanguard.flashmorse.presentation.settings

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanguard.flashmorse.R
import com.vanguard.flashmorse.ui.theme.appMutedText
import com.vanguard.flashmorse.ui.theme.appSecondaryText
import com.vanguard.flashmorse.ui.theme.appSwitchUncheckedThumb
import com.vanguard.flashmorse.ui.theme.appSwitchUncheckedTrack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark by viewModel.isDarkMode.collectAsState()
    val isVibrationEnabled by viewModel.isVibrationEnabled.collectAsState()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onBackground
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.settings_general),
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                title = stringResource(R.string.settings_dark_mode),
                description = stringResource(R.string.settings_dark_mode_desc),
                checked = isDark,
                onCheckedChange = viewModel::setDarkMode,
                primaryTextColor = colorScheme.onBackground
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = colorScheme.outline)
            
            Text(
                stringResource(R.string.settings_transmission),
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                title = stringResource(R.string.settings_vibration),
                description = stringResource(R.string.settings_vibration_desc),
                checked = isVibrationEnabled,
                onCheckedChange = viewModel::setVibrationEnabled,
                primaryTextColor = colorScheme.onBackground
            )
            
            SettingItem(
                title = stringResource(R.string.settings_sound_feedback),
                description = stringResource(R.string.settings_sound_feedback_desc),
                checked = isSoundEnabled,
                onCheckedChange = viewModel::setSoundEnabled,
                primaryTextColor = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                stringResource(R.string.label_flash_morse_version),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.appMutedText
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
    primaryTextColor: Color
) {
    val colorScheme = MaterialTheme.colorScheme
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
                color = colorScheme.appMutedText
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorScheme.primary,
                    checkedTrackColor = colorScheme.primary.copy(alpha = 0.5f),
                    uncheckedThumbColor = colorScheme.appSwitchUncheckedThumb,
                    uncheckedTrackColor = colorScheme.appSwitchUncheckedTrack
                )
            )
        }
}
