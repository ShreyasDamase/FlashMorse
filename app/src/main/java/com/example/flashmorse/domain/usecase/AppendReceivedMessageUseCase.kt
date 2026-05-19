package com.example.flashmorse.domain.usecase

import javax.inject.Inject

class AppendReceivedMessageUseCase @Inject constructor() {

    operator fun invoke(
        current: String,
        incoming: String
    ): String {

        return if (current.isBlank()) {
            incoming
        } else {
            "$current $incoming"
        }
    }
}