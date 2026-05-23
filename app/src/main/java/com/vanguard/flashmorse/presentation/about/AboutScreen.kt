package com.vanguard.flashmorse.presentation.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanguard.flashmorse.ui.theme.FlashOrange
import com.vanguard.flashmorse.ui.theme.appBodyText
import com.vanguard.flashmorse.ui.theme.appMutedText
import com.vanguard.flashmorse.ui.theme.appSecondaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ABOUT FLASHMORSE", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand Header
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(FlashOrange),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "• --",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "FLASHMORSE",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = colorScheme.onBackground
            )
            
            Text(
                "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.appMutedText
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Mission",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "FlashMorse is a high-performance, real-time light signal communicator using optical camera analysis for Morse decoding and device flashlight controls for optical transmission. Engineered for absolute speed and accuracy in local optical signal processing.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.appBodyText,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tech Stack Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = "Code",
                            tint = colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Architecture & Stack",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TechItem("UI Framework", "Jetpack Compose & Material 3", colorScheme.appSecondaryText, colorScheme.appBodyText)
                    TechItem("Asynchronous Flow", "Kotlin Coroutines & Flow", colorScheme.appSecondaryText, colorScheme.appBodyText)
                    TechItem("Dependency Injection", "Dagger Hilt", colorScheme.appSecondaryText, colorScheme.appBodyText)
                    TechItem("Camera System", "Android Jetpack CameraX", colorScheme.appSecondaryText, colorScheme.appBodyText)
                    TechItem("Audio/Speech Engine", "On-Device SpeechRecognizer", colorScheme.appSecondaryText, colorScheme.appBodyText)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Licenses Credits
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Licensing",
                            tint = colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Open Source Licensing",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "This application utilizes software libraries distributed under standard open-source licenses (Apache 2.0 and MIT), including Google Android Jetpack Libraries, Dagger Hilt, and Kotlinx Serialization.\n\nAll optical processing runs completely offline and respects open source guidelines.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.appMutedText,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "© 2026 Vanguard. All rights reserved.",
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.appMutedText
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TechItem(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = labelColor,
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor
        )
    }
}
