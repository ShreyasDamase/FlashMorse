package com.example.flashmorse.domain.usecase

import com.example.flashmorse.domain.morse.MorseEncoder
import javax.inject.Inject

class EncodeTextUseCase @Inject constructor(
    private val morseEncoder: MorseEncoder
) {
    operator fun invoke(text: String): String {
        return morseEncoder.encode(text)
    }


}