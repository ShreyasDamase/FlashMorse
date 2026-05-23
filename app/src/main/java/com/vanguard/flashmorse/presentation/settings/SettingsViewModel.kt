package com.vanguard.flashmorse.presentation.settings

import androidx.lifecycle.ViewModel
import com.vanguard.flashmorse.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    val isDarkMode = repository.isDarkMode
    val isVibrationEnabled = repository.isVibrationEnabled
    val isSoundEnabled = repository.isSoundEnabled

    fun setDarkMode(enabled: Boolean) {
        repository.setDarkMode(enabled)
    }

    fun setVibrationEnabled(enabled: Boolean) {
        repository.setVibrationEnabled(enabled)
    }

    fun setSoundEnabled(enabled: Boolean) {
        repository.setSoundEnabled(enabled)
    }
}
