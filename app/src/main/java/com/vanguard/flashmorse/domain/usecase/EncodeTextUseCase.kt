package com.vanguard.flashmorse.domain.usecase

import com.vanguard.flashmorse.domain.morse.MorseEncoder
import javax.inject.Inject

class EncodeTextUseCase @Inject constructor(
    private val morseEncoder: MorseEncoder
) {
    operator fun invoke(text: String): String {
        return morseEncoder.encode(text)
    }


}