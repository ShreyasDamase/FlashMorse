package com.example.flashmorse.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SpeechRecognitionManager @Inject constructor(
    @ApplicationContext

    private val context: Context
) {

    var onPartialResult: ((String) -> Unit)? = null

    var onFinalResult: ((String) -> Unit)? = null

    var onError: ((Int) -> Unit)? = null


    private var isListening = false


    private val recognizer =
        SpeechRecognizer.createSpeechRecognizer(context)


    private val speechIntent =
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                true
            )
        }


    init {

        recognizer.setRecognitionListener(

            object : RecognitionListener {

                override fun onPartialResults(partialResults: Bundle?) {

                    val text =
                        partialResults
                            ?.getStringArrayList(
                                SpeechRecognizer.RESULTS_RECOGNITION
                            )
                            ?.firstOrNull()
                            .orEmpty()

                    onPartialResult?.invoke(text)
                }

                override fun onResults(results: Bundle?) {

                    val text =
                        results
                            ?.getStringArrayList(
                                SpeechRecognizer.RESULTS_RECOGNITION
                            )
                            ?.firstOrNull()
                            .orEmpty()

                    onFinalResult?.invoke(text)

                    restartOrStop()
                }

                override fun onError(error: Int) {

                    onError?.invoke(error)

                    restartOrStop()
                }

                override fun onReadyForSpeech(params: Bundle?) {}

                override fun onBeginningOfSpeech() {}

                override fun onRmsChanged(rmsdB: Float) {}

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {}

                override fun onEvent(eventType: Int, params: Bundle?) {}
            }
        )
    }


    private fun restartOrStop() {

        if (!isListening) return

        Handler(Looper.getMainLooper()).postDelayed({

            if (isListening) {
                startListening()
            }

        }, 100L)
    }


    fun startListening() {

        isListening = true

        recognizer.startListening(speechIntent)
    }


    fun stopListening() {

        isListening = false

        recognizer.stopListening()
    }


    fun destroy() {

        isListening = false

        recognizer.destroy()
    }
}