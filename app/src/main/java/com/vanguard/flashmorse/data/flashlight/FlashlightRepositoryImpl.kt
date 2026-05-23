package com.vanguard.flashmorse.data.flashlight

import com.vanguard.flashmorse.domain.model.MorseSymbol
import com.vanguard.flashmorse.domain.repository.FlashlightRepository
import javax.inject.Inject

class FlashlightRepositoryImpl @Inject constructor(
    private val morseFlashlightTransmitter: MorseFlashlightTransmitter
) : FlashlightRepository {

    override suspend fun transmit(symbols: List<MorseSymbol>, speedMultiplier: Float) {
        morseFlashlightTransmitter.transmit(
            symbols, speedMultiplier
        )
    }
}