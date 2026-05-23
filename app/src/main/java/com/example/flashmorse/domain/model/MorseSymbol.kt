package com.example.flashmorse.domain.model

 
/**
 * Base Morse timing unit.
 *
 * 1 unit = 200ms
 */

const val BASE_UNIT_MS: Long = 200L

enum class MorseSymbol(
    val durationMs: Long,
    val isOn: Boolean,
) {

    // ─────────────────────────────────────────────────────────────
    // SIGNAL ON
    // ─────────────────────────────────────────────────────────────

    /**
     * Short ON pulse
     *
     * Duration:
     * 200ms
     *
     * Morse:
     * .
     */
    DOT(
        durationMs = BASE_UNIT_MS * 1,
        isOn = true,
    ),

    /**
     * Long ON pulse
     *
     * Duration:
     * 600ms
     *
     * Morse:
     * -
     */
    DASH(
        durationMs = BASE_UNIT_MS * 3,
        isOn = true,
    ),

    // ─────────────────────────────────────────────────────────────
    // SIGNAL OFF
    // ─────────────────────────────────────────────────────────────

    /**
     * Short OFF gap between symbols
     * inside the SAME letter.
     *
     * Example:
     * A = .-
     *
     * DOT
     * SYMBOL_GAP
     * DASH
     */
    SYMBOL_GAP(
        durationMs = BASE_UNIT_MS * 1,
        isOn = false,
    ),

    /**
     * Medium OFF gap between letters.
     *
     * Example:
     * H   I
     */
    LETTER_GAP(
        durationMs = BASE_UNIT_MS * 3,
        isOn = false,
    ),

    /**
     * Long OFF gap between words.
     *
     * Example:
     * HELLO   WORLD
     */
    WORD_GAP(
        durationMs = BASE_UNIT_MS * 7,
        isOn = false,
    );

    // ─────────────────────────────────────────────────────────────
    // COMPANION OBJECT
    // ─────────────────────────────────────────────────────────────

    companion object {


        /**
         * Adjusts transmission speed.
         *
         * Example:
         * multiplier = 2f
         * → faster transmission
         *
         * multiplier = 0.5f
         * → slower transmission
         */
        fun MorseSymbol.scaledDuration(
            multiplier: Float,
        ): Long {
            return (durationMs / multiplier)
                .toLong()
                .coerceAtLeast(50L)
        }

        /**
         * Classifies flashlight ON duration
         * into DOT or DASH.
         */
        fun classifyOnPulse(
            durationMs: Long,
            multiplier: Float = 1.0f
        ): MorseSymbol {
            val scaledBase = BASE_UNIT_MS / multiplier
            return if (durationMs < scaledBase * 2) {
                DOT
            } else {
                DASH
            }
        }

        /**
         * Classifies flashlight OFF duration
         * into Morse gap types.
         */
        fun classifyGap(
            durationMs: Long,
            multiplier: Float = 1.0f
        ): MorseSymbol {
            val scaledBase = BASE_UNIT_MS / multiplier
            return when {
                durationMs < scaledBase * 2 -> SYMBOL_GAP
                durationMs < scaledBase * 5 -> LETTER_GAP
                else -> WORD_GAP
            }
        }
    }
}