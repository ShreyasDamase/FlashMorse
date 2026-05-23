package com.vanguard.flashmorse.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPrefs = context.getSharedPreferences("flashmorse_prefs", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(sharedPrefs.getBoolean("dark_mode", false))
    val isDarkMode = _isDarkMode.asStateFlow()

    private val _isVibrationEnabled = MutableStateFlow(sharedPrefs.getBoolean("flashlight_vibration", true))
    val isVibrationEnabled = _isVibrationEnabled.asStateFlow()

    private val _isSoundEnabled = MutableStateFlow(sharedPrefs.getBoolean("sound_feedback", true))
    val isSoundEnabled = _isSoundEnabled.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        sharedPrefs.edit().putBoolean("dark_mode", enabled).apply()
        _isDarkMode.value = enabled
    }

    fun setVibrationEnabled(enabled: Boolean) {
        sharedPrefs.edit().putBoolean("flashlight_vibration", enabled).apply()
        _isVibrationEnabled.value = enabled
    }

    fun setSoundEnabled(enabled: Boolean) {
        sharedPrefs.edit().putBoolean("sound_feedback", enabled).apply()
        _isSoundEnabled.value = enabled
    }
}
