package com.vanguard.flashmorse.data.flashlight
 
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.vanguard.flashmorse.domain.model.MorseSymbol
import com.vanguard.flashmorse.domain.model.MorseSymbol.Companion.scaledDuration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject

class MorseFlashlightTransmitter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val flashlightDataSource: FlashlightDataSource
) {
    private val sharedPrefs = context.getSharedPreferences("flashmorse_prefs", Context.MODE_PRIVATE)

    /**
     * Plays Morse symbols using flashlight and optional haptic vibration.
     */
    suspend fun transmit(
        symbols: List<MorseSymbol>,
        speedMultiplier: Float
    ) {
        val vibrationEnabled = sharedPrefs.getBoolean("flashlight_vibration", true)
        
        val vibrator = if (vibrationEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
        } else null

        for (symbol in symbols) {
            val duration = symbol.scaledDuration(speedMultiplier)

            if (symbol.isOn) {
                flashlightDataSource.turnOn()
                vibrator?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        it.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(duration)
                    }
                }
            } else {
                flashlightDataSource.turnOff()
                vibrator?.cancel()
            }

            delay(duration)
        }

        // Safety OFF at end
        flashlightDataSource.turnOff()
        vibrator?.cancel()
    }
}