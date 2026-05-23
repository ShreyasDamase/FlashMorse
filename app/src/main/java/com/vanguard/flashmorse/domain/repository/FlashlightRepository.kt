package com.vanguard.flashmorse.domain.repository

import com.vanguard.flashmorse.domain.model.MorseSymbol

interface FlashlightRepository {
    suspend fun transmit(
        symbols: List<MorseSymbol>,
        speedMultiplier: Float
    )
}
