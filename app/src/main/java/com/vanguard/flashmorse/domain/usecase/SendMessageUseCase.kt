package com.vanguard.flashmorse.domain.usecase

import com.vanguard.flashmorse.presentation.communicator.LogEntry
import com.vanguard.flashmorse.presentation.communicator.LogType
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val createLogEntryUseCase: CreateLogEntryUseCase
) {

    operator fun invoke(
        message: String,
        currentLogs: List<LogEntry>
    ): List<LogEntry> {

        if (message.isBlank()) {
            return currentLogs
        }

        val log =
            createLogEntryUseCase(
                LogType.SENT,
                message
            )

        return currentLogs + log
    }
}