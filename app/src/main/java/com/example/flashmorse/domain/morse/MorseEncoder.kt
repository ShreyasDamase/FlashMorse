package com.example.flashmorse.domain.morse

import javax.inject.Inject

class MorseEncoder @Inject constructor() {

    fun encode(
        text: String,
    ): String {

        return text
            .trim()
            .uppercase()
            .split(" ")

            .joinToString(" ~ ") { word ->

                word.mapNotNull { char ->

                    MorseTable.lookupMorse(char)

                }.joinToString(" ")
            }
    }
}