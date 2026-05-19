package com.example.flashmorse.domain.usecase

import com.example.flashmorse.domain.model.MorseSymbol
import com.example.flashmorse.domain.model.MorseSymbol.*
import com.example.flashmorse.domain.morse.MorseTable
import javax.inject.Inject

class EncodeTextToSymbolsUseCase @Inject constructor() {

    operator fun invoke(text: String): List<MorseSymbol> {
        val symbols = mutableListOf<MorseSymbol>()
        val words = text.trim().uppercase().split("\\s+".toRegex())

        words.forEachIndexed { wordIndex, word ->
            word.forEachIndexed { letterIndex, letter ->
                val morseCode = MorseTable.lookupMorse(letter) ?: return@forEachIndexed

                morseCode.forEachIndexed { symbolIndex, morseChar ->
                    when (morseChar) {
                        '.' -> symbols.add(DOT)
                        '-' -> symbols.add(DASH)
                    }

                    // Gap between symbols inside same letter
                    if (symbolIndex != morseCode.lastIndex) {
                        symbols.add(SYMBOL_GAP)
                    }
                }

                // Gap between letters within a word
                if (letterIndex != word.lastIndex) {
                    symbols.add(LETTER_GAP)
                }
            }

            // Gap between words
            if (wordIndex != words.lastIndex) {
                symbols.add(WORD_GAP)
            }
        }

        return symbols
    }
}
