package com.example.flashmorse.domain.morse

import com.example.flashmorse.domain.model.MorseSymbol
import com.example.flashmorse.domain.model.MorseSymbol.Companion.classifyGap
import com.example.flashmorse.domain.model.MorseSymbol.Companion.classifyOnPulse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MorseDecoder @Inject constructor() {

    private var isLightOn = false
    private var stateStartTime = 0L
    private var currentLetterMorse = StringBuilder()
    private val pulseHistory = mutableListOf<String>()
    private var handledCurrentOffGap: MorseSymbol? = null
    
    // Threshold tuned for the filtered camera score produced by CameraPreview.
    private val brightnessThreshold = 150.0

    /**
     * Processes a brightness sample and returns the newly decoded character if any.
     */
    fun processBrightness(brightness: Double, speedMultiplier: Float): String? {
        val currentTime = System.currentTimeMillis()
        val currentIsOn = isSignalOn(brightness)
        
        if (stateStartTime == 0L) {
            stateStartTime = currentTime
            isLightOn = currentIsOn
            handledCurrentOffGap = null
            return null
        }

        if (currentIsOn != isLightOn) {
            val duration = currentTime - stateStartTime
            
            // Filter out very short noise/jitter (less than 30ms)
            if (duration < 30) return null

            val result = if (!isLightOn && handledCurrentOffGap != null) {
                null
            } else {
                handleStateChange(isLightOn, duration, speedMultiplier)
            }
            
            isLightOn = currentIsOn
            stateStartTime = currentTime
            handledCurrentOffGap = null
            return result
        } else {
            // Commit letters/spaces from sustained OFF gaps so text decoding does not depend
            // entirely on detecting the next rising edge at exactly the right time.
            if (!isLightOn) {
                val duration = currentTime - stateStartTime
                val gapType = classifyGap(duration, speedMultiplier)

                if (handledCurrentOffGap == null) {
                    if (gapType == MorseSymbol.LETTER_GAP && currentLetterMorse.isNotEmpty()) {
                        handledCurrentOffGap = MorseSymbol.LETTER_GAP
                        return completeLetter()
                    }

                    if (gapType == MorseSymbol.WORD_GAP && currentLetterMorse.isNotEmpty()) {
                        handledCurrentOffGap = MorseSymbol.WORD_GAP
                        val char = completeLetter()
                        return if (char != null) "$char " else null
                    }
                }

                if (
                    handledCurrentOffGap == MorseSymbol.LETTER_GAP &&
                    gapType == MorseSymbol.WORD_GAP
                ) {
                    handledCurrentOffGap = MorseSymbol.WORD_GAP
                    return " "
                }
            }
        }
        
        return null
    }

    private fun handleStateChange(wasOn: Boolean, duration: Long, speedMultiplier: Float): String? {
        if (wasOn) {
            val symbol = classifyOnPulse(duration, speedMultiplier)
            val mark = if (symbol == MorseSymbol.DOT) "." else "-"
            currentLetterMorse.append(mark)
            
            // Add to visual history
            pulseHistory.add(mark)
            if (pulseHistory.size > 15) pulseHistory.removeAt(0)
            
            return null
        } else {
            val gapType = classifyGap(duration, speedMultiplier)
            return when (gapType) {
                MorseSymbol.LETTER_GAP -> completeLetter()
                MorseSymbol.WORD_GAP -> {
                    val char = completeLetter()
                    if (char != null) "$char " else " "
                }
                else -> null 
            }
        }
    }

    private fun completeLetter(): String? {
        if (currentLetterMorse.isEmpty()) return null
        val morse = currentLetterMorse.toString()
        currentLetterMorse.clear()
        val char = MorseTable.lookupChar(morse)
        return char?.toString() ?: "[?]"
    }

    fun getCurrentLetterMorse(): String = currentLetterMorse.toString()
    
    fun getPulseHistory(): String = pulseHistory.joinToString(" ")

    fun isSignalOn(brightness: Double): Boolean = brightness >= brightnessThreshold

    /**
     * Flushes any pending flashlight-decoded letter that has been collected in the
     * current Morse buffer but has not yet been committed because no further gap
     * transition was observed.
     */
    fun flushPendingDecodedText(): String? {
        return completeLetter()
    }

    fun clear() {
        currentLetterMorse.setLength(0)
        pulseHistory.clear()
        isLightOn = false
        stateStartTime = 0L
        handledCurrentOffGap = null
    }
}
