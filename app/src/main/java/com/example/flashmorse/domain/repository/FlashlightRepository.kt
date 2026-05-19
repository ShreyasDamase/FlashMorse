package com.example.flashmorse.domain.repository

import com.example.flashmorse.domain.model.MorseSymbol

interface FlashlightRepository {
    suspend fun transmit(
        symbols: List<MorseSymbol>,
        speedMultiplier: Float
    )
}
