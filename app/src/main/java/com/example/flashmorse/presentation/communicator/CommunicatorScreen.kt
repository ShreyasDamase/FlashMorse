package com.example.flashmorse.presentation.communicator

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.flashmorse.presentation.common.PermissionUtils

@Composable
fun CommunicatorScreen(
    viewModel: CommunicatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.startListening()

        }
    }



    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE8DFD3) // Matching the background color from image
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            HeaderSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Message and Camera View Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MessageInputSection(
                    modifier = Modifier.weight(1f),
                    text = uiState.messageText,
                    onTextChange = viewModel::onMessageTextChanged,
                    onVoiceClick = {
                        if (!uiState.isListening) {
                            val granted = PermissionUtils.isPermissionGranted(
                                context = context, Manifest.permission.RECORD_AUDIO
                            )
                            if (granted) {
                                viewModel.startListening()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        } else {
                            viewModel.stopListening()

                        }
                    }
                )
                CameraViewSection(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Morse Signal Section
            MorseSignalSection(
                sendingSignal = uiState.sendingSignal,
                receivingSignal = uiState.receivingSignal
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Communication Log Section
            CommunicationLogSection(
                modifier = Modifier.weight(1f),
                logs = uiState.communicationLog
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Controls
            BottomControlsSection(
                speed = uiState.durationMultiplier,
                onSpeedChange = viewModel::onSpeedChanged,
                isListening = uiState.isListening,
                onToggleListening = {
                    // Similar logic to onVoiceClick
                }
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO */ }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MORSE COMMUNICATOR",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A453E)
            )
            Text(
                text = "• LIGHT. SIGNAL. CONNECT. •",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF8B8479)
            )
        }
        IconButton(onClick = { /* TODO */ }) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
fun MessageInputSection(
    modifier: Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onVoiceClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF2ECE4))
            .padding(12.dp)
    ) {
        Text("MESSAGE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E))
                .padding(8.dp)
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedTextColor = Color(0xFFE8DFD3),
                    unfocusedTextColor = Color(0xFFE8DFD3),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text("Enter text...", color = Color.Gray) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Text and Voice buttons (simplified)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("TEXT", color = Color.Black, fontSize = 10.sp)
                }
            }
            FloatingActionButton(
                onClick = onVoiceClick,
                containerColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                // Mic icon would go here
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("VOICE", color = Color.Black, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun CameraViewSection(modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF2ECE4))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Green)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "CAMERA VIEW",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            // Camera Preview placeholder
            Text("REC", color = Color.Red, modifier = Modifier.padding(8.dp), fontSize = 10.sp)
        }
    }
}

@Composable
fun MorseSignalSection(sendingSignal: String, receivingSignal: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF2ECE4))
            .padding(12.dp)
    ) {
        Text(
            "MORSE SIGNAL",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SignalRow("SENDING", Color(0xFFFFA500), sendingSignal)
            SignalRow("RECEIVING", Color(0xFF90EE90), receivingSignal)
        }
    }
}

@Composable
fun SignalRow(label: String, color: Color, signal: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = color, fontSize = 10.sp, modifier = Modifier.width(70.dp))
        Text(
            text = signal.ifBlank { "• • •  - - -  • • •" }, // Placeholder if empty
            color = color,
            letterSpacing = 4.sp,
            maxLines = 1
        )
    }
}

@Composable
fun CommunicationLogSection(modifier: Modifier, logs: List<LogEntry>) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF2ECE4))
            .padding(12.dp)
    ) {
        Text(
            "COMMUNICATION LOG",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E))
                .padding(8.dp)
        ) {
            items(logs) { entry ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(entry.timestamp, color = Color.Gray, fontSize = 10.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        entry.type.name,
                        color = if (entry.type == LogType.SENT) Color(0xFFFFA500) else Color(
                            0xFF90EE90
                        ),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(entry.message, color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun BottomControlsSection(
    speed: Float,
    onSpeedChange: (Float) -> Unit,
    isListening: Boolean,
    onToggleListening: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF2ECE4))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("SPEED", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Slider(value = speed, onValueChange = onSpeedChange, valueRange = 0.5f..2.0f)
            Text("DURATION MULTIPLIER", fontSize = 8.sp)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF2ECE4))
                .padding(12.dp)
        ) {
            Text(
                "MODE LISTENING",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Switch(checked = isListening, onCheckedChange = { onToggleListening() })
            Text("When ON, device listens and decodes incoming light signals.", fontSize = 8.sp)
        }
    }
}
