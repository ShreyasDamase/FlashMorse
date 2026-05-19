package com.example.flashmorse.domain.usecase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FormatTimestampUseCase @Inject constructor() {
    operator fun invoke(): String {
        return SimpleDateFormat(
            "HH:mm:ss", Locale.getDefault()
        ).format(Date())
    }
}