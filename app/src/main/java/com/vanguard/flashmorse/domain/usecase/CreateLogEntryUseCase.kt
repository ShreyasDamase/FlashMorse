package com.vanguard.flashmorse.domain.usecase

import com.vanguard.flashmorse.presentation.communicator.LogEntry
import com.vanguard.flashmorse.presentation.communicator.LogType
import javax.inject.Inject

class CreateLogEntryUseCase @Inject constructor(
    private val formatTimestampUseCase: FormatTimestampUseCase
) {

    operator fun invoke(
        type: LogType,
        message: String
    ): LogEntry {

        return LogEntry(
            timestamp = formatTimestampUseCase(),
            type = type,
            message = message
        )
    }
}