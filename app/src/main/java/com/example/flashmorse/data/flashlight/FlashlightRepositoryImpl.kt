package com.example.flashmorse.data.flashlight

import com.example.flashmorse.domain.model.MorseSymbol
import com.example.flashmorse.domain.repository.FlashlightRepository
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