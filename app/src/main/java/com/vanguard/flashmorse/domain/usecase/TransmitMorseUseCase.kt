package com.vanguard.flashmorse.domain.usecase

import com.vanguard.flashmorse.domain.repository.FlashlightRepository
import javax.inject.Inject

class TransmitMorseUseCase @Inject constructor(
    private val encodeTextToSymbolsUseCase: EncodeTextToSymbolsUseCase,

    private val flashlightRepository: FlashlightRepository
) {
    suspend operator fun invoke(
        text: String,
        speedMultiplier: Float
    ) {

        // Convert text into Morse protocol symbols
        val symbols =
            encodeTextToSymbolsUseCase(text)

        // Physically transmit them
        flashlightRepository.transmit(
            symbols,
            speedMultiplier
        )
    }
}