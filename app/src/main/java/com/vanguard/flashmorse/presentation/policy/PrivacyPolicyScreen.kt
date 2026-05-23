package com.vanguard.flashmorse.presentation.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanguard.flashmorse.ui.theme.appBodyText
import com.vanguard.flashmorse.ui.theme.appMutedText
import com.vanguard.flashmorse.ui.theme.appSecondaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PRIVACY POLICY", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onBackground
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "DATA & SECURITY ASSURANCE",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "100% On-Device Processing",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "FlashMorse utilizes your device's Camera and Microphone hardware to decode light signal fluctuations and capture voice inputs. We strictly respect your privacy: ALL operations, calculations, and decoding runs are performed locally on your device in real-time. Absolutely zero image frames, audio recordings, or text logs are sent, stored, or processed on remote cloud servers.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.appBodyText,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "HARDWARE PERMISSIONS USED",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            PermissionInfoCard(
                title = "Camera Permission (CAMERA)",
                description = "Required to capture ambient light pulses and decode flashing sequences in real-time. Video frames are analyzed stream-by-stream locally and immediately discarded.",
                cardBgColor = colorScheme.surface,
                titleColor = colorScheme.onSurface,
                descColor = colorScheme.appBodyText
            )

            Spacer(modifier = Modifier.height(8.dp))

            PermissionInfoCard(
                title = "Audio Permission (RECORD_AUDIO)",
                description = "Optional and strictly requested only when you choose to transmit Morse code using voice-to-text. Voice data is processed locally using Google's Speech Recognizer API.",
                cardBgColor = colorScheme.surface,
                titleColor = colorScheme.onSurface,
                descColor = colorScheme.appBodyText
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "CONTACT & INQUIRIES",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "For any queries regarding this policy, security details, or bug reporting, feel free to contact us via the Developer Settings profile.",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.appMutedText,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PermissionInfoCard(
    title: String,
    description: String,
    cardBgColor: Color,
    titleColor: Color,
    descColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = cardBgColor
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = titleColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = descColor, lineHeight = 18.sp)
        }
    }
}
