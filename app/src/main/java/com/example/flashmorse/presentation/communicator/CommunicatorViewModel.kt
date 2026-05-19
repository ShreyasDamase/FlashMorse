package com.example.flashmorse.presentation.communicator

import androidx.lifecycle.ViewModel
import com.example.flashmorse.data.speech.SpeechRecognitionManager
import com.example.flashmorse.domain.usecase.AppendReceivedMessageUseCase
import com.example.flashmorse.domain.usecase.CreateLogEntryUseCase
import com.example.flashmorse.domain.usecase.EncodeTextUseCase
import com.example.flashmorse.domain.usecase.FormatTimestampUseCase
import com.example.flashmorse.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommunicatorViewModel @Inject constructor(
    private val encodeTextUseCase: EncodeTextUseCase,
    private val speechRecognitionManager: SpeechRecognitionManager,
    private val formatTimestampUseCase: FormatTimestampUseCase,
    private val createLogEntryUseCase: CreateLogEntryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val appendReceivedMessageUseCase: AppendReceivedMessageUseCase

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
        _uiState.update { it.copy(partialReceivedText = text) }
    }

    fun onCommittedReceivedTextUpdate(text: String) {
        _uiState.update { state ->
            val newLog = state.communicationLog + createLogEntryUseCase(LogType.RECEIVED, text)
            state.copy(
                committedReceivedText = appendReceivedMessageUseCase(
                    state.committedReceivedText,
                    text
                ),
                partialReceivedText = "",
                communicationLog = newLog
            )
        }
    }

    fun clearReceivedText() {
        _uiState.update { it.copy(committedReceivedText = "", partialReceivedText = "") }
    }

    fun onSendMessage() {

        val currentState = _uiState.value

        val updatedLogs =
            sendMessageUseCase(
                message = currentState.messageText,
                currentLogs = currentState.communicationLog
            )

        if (updatedLogs == currentState.communicationLog) {
            return
        }

        _uiState.update {

            it.copy(
                communicationLog = updatedLogs,
                messageText = ""
            )
        }
    }

    fun onSpeedChanged(multiplier: Float) {
        _uiState.update { it.copy(durationMultiplier = multiplier) }
    }
}
