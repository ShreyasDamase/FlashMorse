package com.example.flashmorse.presentation.communicator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashmorse.data.speech.SpeechRecognitionManager
import com.example.flashmorse.domain.usecase.AppendReceivedMessageUseCase
import com.example.flashmorse.domain.usecase.CreateLogEntryUseCase
import com.example.flashmorse.domain.usecase.EncodeTextUseCase
import com.example.flashmorse.domain.usecase.SendMessageUseCase
import com.example.flashmorse.domain.usecase.TransmitMorseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicatorViewModel @Inject constructor(
    private val encodeTextUseCase: EncodeTextUseCase,
    private val speechRecognitionManager: SpeechRecognitionManager,
    private val createLogEntryUseCase: CreateLogEntryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val appendReceivedMessageUseCase: AppendReceivedMessageUseCase,
    private val transmitMorseUseCase: TransmitMorseUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(CommunicatorUiState())
    val uiState = _uiState.asStateFlow()


    init {

        speechRecognitionManager.onPartialResult = { text ->

            onPartialReceivedTextUpdate(text)
        }

        speechRecognitionManager.onFinalResult = { text ->

            onCommittedReceivedTextUpdate(text)
        }

        speechRecognitionManager.onError = {

            _uiState.update {
                it.copy(
                    partialReceivedText = ""
                )
            }
        }
    }

    fun transmitMessage() {

        val message =
            uiState.value.messageText

        if (message.isBlank()) {
            return
        }

        viewModelScope.launch {

            transmitMorseUseCase(
                text = message,
                speedMultiplier =
                    uiState.value.durationMultiplier
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

    fun startListening() {

        speechRecognitionManager.startListening()

        _uiState.update {
            it.copy(isListening = true)
        }
    }

    fun stopListening() {

        speechRecognitionManager.stopListening()

        _uiState.update {
            it.copy(isListening = false)
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

        val morseSignal =
            encodeTextUseCase(text)

        _uiState.update { state ->

            val newLog =
                state.communicationLog +
                        createLogEntryUseCase(
                            LogType.SENT,
                            text
                        )

            state.copy(
                messageText = text,
                committedReceivedText =
                appendReceivedMessageUseCase(
                    state.committedReceivedText,
                    text
                ),
                partialReceivedText = "",
                sendingSignal = morseSignal,
                communicationLog = newLog
            )
        }

        // Automatically transmit the voice input
        viewModelScope.launch {
            transmitMorseUseCase(
                text = text,
                speedMultiplier = _uiState.value.durationMultiplier
            )
        }
    }

    fun clearReceivedText() {
        _uiState.update { it.copy(committedReceivedText = "", partialReceivedText = "") }
    }

    fun onSendMessage() {

        val currentState = _uiState.value

        if (currentState.messageText.isBlank()) return

        val updatedLogs =
            sendMessageUseCase(
                message = currentState.messageText,
                currentLogs = currentState.communicationLog
            )

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
                text = "T", // 'T' is just one dash, good for testing
                speedMultiplier = uiState.value.durationMultiplier
            )
        }
    }

    fun onSpeedChanged(multiplier: Float) {
        _uiState.update { it.copy(durationMultiplier = multiplier) }
    }
}
