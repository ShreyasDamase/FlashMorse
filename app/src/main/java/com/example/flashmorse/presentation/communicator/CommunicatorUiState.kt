package com.example.flashmorse.presentation.communicator

data class CommunicatorUiState(
    val messageText: String = "",
    val isVoiceListening: Boolean = false,
    val isReceivingFlashlight: Boolean = false,
    val partialReceivedText: String = "",
    val committedReceivedText: String = "",
    val communicationLog: List<LogEntry> = emptyList(),
    val sendingSignal: String = "",
    val receivingSignal: String = "",
    val liveMorseBuffer: String = "",
    val signalStrength: Float = 0f,
    val durationMultiplier: Float = 1.0f
)

data class LogEntry(
    val timestamp: String,
    val type: LogType,
    val message: String
)

enum class LogType {
    SENT, RECEIVED
}
