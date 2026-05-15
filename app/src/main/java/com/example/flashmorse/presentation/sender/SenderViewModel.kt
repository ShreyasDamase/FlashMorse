package com.example.flashmorse.presentation.sender

import androidx.lifecycle.ViewModel
import com.example.flashmorse.domain.usecase.EncodeTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SenderViewModel @Inject constructor(
    private val encodeTextUseCase: EncodeTextUseCase
) : ViewModel() {
    private val _text = MutableStateFlow<String>("")
    val text = _text.asStateFlow()

    private val _encodedCode = MutableStateFlow<String>("")
    val encodedCode = _encodedCode.asStateFlow()


    fun onTextChanged(newText: String) {
        _text.value = newText
        _encodedCode.value = encodeTextUseCase(newText)
    }

}