package com.vanguard.flashmorse.presentation.communicator

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.camera.core.CameraControl
import com.vanguard.flashmorse.data.flashlight.FlashlightDataSource
import com.vanguard.flashmorse.data.speech.SpeechRecognitionManager
import com.vanguard.flashmorse.domain.model.BASE_UNIT_MS
import com.vanguard.flashmorse.domain.usecase.CreateLogEntryUseCase
import com.vanguard.flashmorse.domain.usecase.EncodeTextUseCase
import com.vanguard.flashmorse.domain.usecase.SendMessageUseCase
import com.vanguard.flashmorse.domain.usecase.TransmitMorseUseCase
import com.vanguard.flashmorse.domain.morse.MorseDecoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

@HiltViewModel
class CommunicatorViewModel @Inject constructor(
    private val application: Application,
    private val encodeTextUseCase: EncodeTextUseCase,
    private val speechRecognitionManager: SpeechRecognitionManager,
    private val createLogEntryUseCase: CreateLogEntryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val transmitMorseUseCase: TransmitMorseUseCase,
    private val flashlightDataSource: FlashlightDataSource,
    private val morseDecoder: MorseDecoder
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunicatorUiState())
    val uiState = _uiState.asStateFlow()

    private var autoFinalizeJob: Job? = null
    private val receiveFinalizeDelimiters = setOf('.', ',', '!', '?', ';', ':')

    init {
        speechRecognitionManager.onPartialResult = { text ->
            onPartialReceivedTextUpdate(text)
        }

        speechRecognitionManager.onFinalResult = { text ->
            onCommittedReceivedTextUpdate(text)
        }

        speechRecognitionManager.onError = {
            _uiState.update {
                it.copy(partialReceivedText = "")
            }
        }

        // Load logs from SharedPreferences
        loadLogsFromStorage()
    }

    fun transmitMessage() {
        val message = uiState.value.messageText
        if (message.isBlank()) return

        viewModelScope.launch {
            transmitMorseUseCase(
                text = message,
                speedMultiplier = uiState.value.durationMultiplier
            )
        }
    }

    fun onMessageTextChanged(newText: String) {
        _uiState.update {
            it.copy(
                messageText = newText,
                sendingSignal = encodeTextUseCase(newText)
            )
        }
    }

    fun startVoiceListening() {
        speechRecognitionManager.startListening()
        _uiState.update { it.copy(isVoiceListening = true) }
    }

    fun stopVoiceListening() {
        speechRecognitionManager.stopListening()
        _uiState.update { it.copy(isVoiceListening = false) }
    }

    fun startReceivingFlashlight() {
        morseDecoder.clear()
        autoFinalizeJob?.cancel()
        _uiState.update {
            it.copy(
                isReceivingFlashlight = true,
                committedReceivedText = "",
                receivingSignal = "",
                liveMorseBuffer = "",
                signalStrength = 0f
            )
        }
    }

    fun stopReceivingFlashlight() {
        autoFinalizeJob?.cancel()
        finalizeReceivedMessage()
        _uiState.update {
            it.copy(
                isReceivingFlashlight = false,
                receivingSignal = "",
                liveMorseBuffer = "",
                signalStrength = 0f
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognitionManager.destroy()
    }

    fun onPartialReceivedTextUpdate(text: String) {
        _uiState.update { 
            it.copy(
                partialReceivedText = text,
                messageText = text,
                sendingSignal = encodeTextUseCase(text)
            ) 
        }
    }

    fun onCommittedReceivedTextUpdate(text: String) {
        val morseSignal = encodeTextUseCase(text)
        _uiState.update { state ->
            val newLog = state.communicationLog + createLogEntryUseCase(LogType.SENT, text)
            saveLogs(newLog)
            state.copy(
                messageText = text,
                partialReceivedText = "",
                sendingSignal = morseSignal,
                communicationLog = newLog
            )
        }

        viewModelScope.launch {
            transmitMorseUseCase(
                text = text,
                speedMultiplier = _uiState.value.durationMultiplier
            )
        }
    }

    fun clearReceivedText() {
        autoFinalizeJob?.cancel()
        _uiState.update {
            it.copy(
                committedReceivedText = "",
                partialReceivedText = "",
                receivingSignal = "",
                liveMorseBuffer = "",
                signalStrength = 0f
            )
        }
        morseDecoder.clear()
    }

    fun onSendMessage() {
        val currentState = _uiState.value
        if (currentState.messageText.isBlank()) return

        val updatedLogs = sendMessageUseCase(
            message = currentState.messageText,
            currentLogs = currentState.communicationLog
        )

        saveLogs(updatedLogs)
        transmitMessage()

        _uiState.update {
            it.copy(
                communicationLog = updatedLogs,
                messageText = ""
            )
        }
    }

    fun testFlashlight() {
        viewModelScope.launch {
            transmitMorseUseCase(
                text = "T",
                speedMultiplier = uiState.value.durationMultiplier
            )
        }
    }

    fun onSpeedChanged(multiplier: Float) {
        _uiState.update { it.copy(durationMultiplier = multiplier) }
    }

    fun onCameraControlReady(control: CameraControl?) {
        flashlightDataSource.setCameraControl(control)
    }

    fun onBrightnessDetected(brightness: Double) {
        val previousState = _uiState.value
        if (!previousState.isReceivingFlashlight) return

        // Normalize brightness for signal strength (0.0 to 1.0)
        // Assuming 255 is max brightness from Y-plane
        val strength = (brightness / 255.0).coerceIn(0.0, 1.0).toFloat()

        val decodedChar = morseDecoder.processBrightness(
            brightness = brightness,
            speedMultiplier = _uiState.value.durationMultiplier
        )

        val updatedPulseHistory = morseDecoder.getPulseHistory()
        val updatedLiveBuffer = morseDecoder.getCurrentLetterMorse()
        val hadSignalProgress =
            updatedPulseHistory != previousState.receivingSignal ||
                updatedLiveBuffer != previousState.liveMorseBuffer

        if (decodedChar != null) {
            appendDecodedReceivedText(decodedChar)
        }

        if (hadSignalProgress) {
            resetAutoFinalizeTimer(_uiState.value.committedReceivedText)
        }

        _uiState.update { state ->
            state.copy(
                // Shows history like ". . - -" in the UI
                receivingSignal = updatedPulseHistory,
                liveMorseBuffer = updatedLiveBuffer,
                signalStrength = strength
            )
        }
    }

    private fun resetAutoFinalizeTimer(currentText: String) {
        autoFinalizeJob?.cancel()
        val finalizeDelayMs = calculateReceiveFinalizeDelay(currentText, _uiState.value.durationMultiplier)
        autoFinalizeJob = viewModelScope.launch {
            // Wait longer than a normal Morse word gap so we do not split valid messages.
            delay(finalizeDelayMs)
            finalizeReceivedMessage()
        }
    }

    private fun appendDecodedReceivedText(decodedChunk: String) {
        _uiState.update { state ->
            val finalText = mergeReceivedText(state.committedReceivedText, decodedChunk)
            state.copy(committedReceivedText = finalText)
        }
    }

    private fun mergeReceivedText(current: String, incoming: String): String {
        if (incoming == " ") {
            return if (current.isBlank() || current.endsWith(" ")) current else "$current "
        }

        if (incoming.isBlank()) {
            return current
        }

        val trimmedIncoming = incoming.trimStart()
        return when {
            current.isBlank() -> trimmedIncoming
            trimmedIncoming.firstOrNull() in receiveFinalizeDelimiters && current.endsWith(" ") ->
                current.dropLast(1) + trimmedIncoming
            else -> current + incoming
        }
    }

    private fun calculateReceiveFinalizeDelay(currentText: String, speedMultiplier: Float): Long {
        val unitMs = (BASE_UNIT_MS / speedMultiplier)
            .toLong()
            .coerceAtLeast(50L)

        // Base delay is comfortably longer than a word gap (7 units), so a valid
        // inter-word pause does not prematurely finalize the current message.
        val baseDelay = unitMs * 12

        // If the latest decoded text ends in punctuation, a slightly shorter delay
        // feels natural while still requiring an actual pause before finalizing.
        val lastVisibleChar = currentText.trimEnd().lastOrNull()
        return if (lastVisibleChar in receiveFinalizeDelimiters) {
            (unitMs * 9).coerceAtLeast(baseDelay / 2)
        } else {
            baseDelay
        }
    }

    private fun finalizeReceivedMessage() {
        autoFinalizeJob?.cancel()
        morseDecoder.flushPendingDecodedText()?.let { pendingText ->
            appendDecodedReceivedText(pendingText)
        }

        val text = _uiState.value.committedReceivedText.trim()
        if (text.isNotEmpty()) {
            _uiState.update { state ->
                val newLog = state.communicationLog + createLogEntryUseCase(
                    LogType.RECEIVED,
                    text
                )
                saveLogs(newLog)
                state.copy(
                    communicationLog = newLog,
                    committedReceivedText = "",
                    receivingSignal = "",
                    liveMorseBuffer = ""
                )
            }
        } else {
            _uiState.update { state ->
                state.copy(
                    receivingSignal = "",
                    liveMorseBuffer = ""
                )
            }
        }
        morseDecoder.clear()
    }

    private fun saveLogs(logs: List<LogEntry>) {
        val sharedPrefs = application.getSharedPreferences("flashmorse_prefs", Context.MODE_PRIVATE)
        val jsonArray = buildJsonArray {
            for (entry in logs) {
                add(buildJsonObject {
                    put("timestamp", entry.timestamp)
                    put("type", entry.type.name)
                    put("message", entry.message)
                })
            }
        }
        sharedPrefs.edit().putString("comms_history", jsonArray.toString()).apply()
    }

    private fun loadLogsFromStorage() {
        val sharedPrefs = application.getSharedPreferences("flashmorse_prefs", Context.MODE_PRIVATE)
        val serializedLogs = sharedPrefs.getString("comms_history", null)
        val logsList = mutableListOf<LogEntry>()
        if (serializedLogs != null) {
            try {
                val jsonArray = Json.parseToJsonElement(serializedLogs)
                if (jsonArray is kotlinx.serialization.json.JsonArray) {
                    for (element in jsonArray) {
                        if (element is kotlinx.serialization.json.JsonObject) {
                            val msg = element["message"]?.toString()?.removeSurrounding("\"") ?: ""
                            val timestamp = element["timestamp"]?.toString()?.removeSurrounding("\"") ?: ""
                            val typeStr = element["type"]?.toString()?.removeSurrounding("\"") ?: "SENT"
                            val type = if (typeStr == "RECEIVED") LogType.RECEIVED else LogType.SENT
                            logsList.add(LogEntry(timestamp, type, msg))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _uiState.update { it.copy(communicationLog = logsList) }
    }
}
