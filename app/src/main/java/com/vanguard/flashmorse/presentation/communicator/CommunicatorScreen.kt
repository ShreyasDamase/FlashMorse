package com.vanguard.flashmorse.presentation.communicator

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanguard.flashmorse.R
import com.vanguard.flashmorse.presentation.common.PermissionUtils
import com.vanguard.flashmorse.ui.theme.appDrawerSelectedContainer
import com.vanguard.flashmorse.ui.theme.appDrawerSelectedText
import com.vanguard.flashmorse.ui.theme.appInputSurface
import com.vanguard.flashmorse.ui.theme.appMutedText
import com.vanguard.flashmorse.ui.theme.appReceiverGreen
import com.vanguard.flashmorse.ui.theme.appSecondaryText
import com.vanguard.flashmorse.ui.theme.appSwitchUncheckedThumb
import com.vanguard.flashmorse.ui.theme.appSwitchUncheckedTrack
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun CommunicatorScreen(
    viewModel: CommunicatorViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToMorseGuide: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    fun navigateFromDrawer(navigate: () -> Unit) {
        scope.launch {
            drawerState.close()
            navigate()
        }
    }

    var hasCameraPermission by remember {
        mutableStateOf(PermissionUtils.isPermissionGranted(context, Manifest.permission.CAMERA))
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.startVoiceListening()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission =
                    PermissionUtils.isPermissionGranted(context, Manifest.permission.CAMERA)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorScheme.surface,
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(R.string.label_flash_morse),
                    modifier = Modifier.padding(horizontal = 28.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.label_version),
                    modifier = Modifier.padding(horizontal = 28.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.appSecondaryText
                )
                Spacer(Modifier.height(24.dp))
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_communicator)) },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = colorScheme.appDrawerSelectedContainer,
                        selectedTextColor = colorScheme.appDrawerSelectedText
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_morse_guide)) },
                    selected = false,
                    onClick = { navigateFromDrawer(onNavigateToMorseGuide) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = colorScheme.onSurface
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_history)) },
                    selected = false,
                    onClick = { navigateFromDrawer(onNavigateToHistory) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = colorScheme.onSurface
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_settings)) },
                    selected = false,
                    onClick = { navigateFromDrawer(onNavigateToSettings) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = colorScheme.onSurface
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_privacy_policy)) },
                    selected = false,
                    onClick = { navigateFromDrawer(onNavigateToPrivacyPolicy) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = colorScheme.onSurface
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_about_app)) },
                    selected = false,
                    onClick = { navigateFromDrawer(onNavigateToAbout) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                val helpUrl = stringResource(R.string.app_help_url)
                val feedbackUrl = stringResource(R.string.app_feedback_url)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Help",
                        modifier = Modifier.clickable {
                            uriHandler.openUri(helpUrl)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = colorScheme.appSecondaryText,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Feedback",
                        modifier = Modifier.clickable {
                            uriHandler.openUri(feedbackUrl)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = colorScheme.appSecondaryText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                HeaderSection(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSettingsClick = onNavigateToSettings,
                    primaryTextColor = colorScheme.onBackground,
                    secondaryTextColor = colorScheme.appSecondaryText
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Message and Camera View Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MessageInputSection(
                        modifier = Modifier.weight(1f),
                        text = uiState.messageText,
                        isVoiceListening = uiState.isVoiceListening,
                        onTextChange = viewModel::onMessageTextChanged,
                        onVoiceClick = {
                            if (!uiState.isVoiceListening) {
                                val granted = PermissionUtils.isPermissionGranted(
                                    context = context, Manifest.permission.RECORD_AUDIO
                                )
                                if (granted) {
                                    viewModel.startVoiceListening()
                                } else {
                                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            } else {
                                viewModel.stopVoiceListening()
                            }
                        },
                        onSendClick = viewModel::onSendMessage,
                        onTestClick = viewModel::testFlashlight,
                        cardBgColor = colorScheme.surface,
                        textColor = colorScheme.onSurface,
                        inputBgColor = colorScheme.appInputSurface
                    )
                    CameraViewSection(
                        modifier = Modifier.weight(1f),
                        hasCameraPermission = hasCameraPermission,
                        onRequestPermission = {
                            cameraPermissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                        },
                        onCameraControlReady = viewModel::onCameraControlReady,
                        onBrightnessDetected = viewModel::onBrightnessDetected,
                        signalStrength = uiState.signalStrength
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ListeningModeSection(
                    isListening = uiState.isReceivingFlashlight,
                    onToggleListening = {
                        if (uiState.isReceivingFlashlight) {
                            viewModel.stopReceivingFlashlight()
                        } else {
                            viewModel.startReceivingFlashlight()
                        }
                    },
                    cardBgColor = colorScheme.surface,
                    textColor = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Morse Signal Section
                MorseSignalSection(
                    sendingSignal = uiState.sendingSignal,
                    receivingSignal = uiState.receivingSignal,
                    liveMorseBuffer = uiState.liveMorseBuffer,
                    signalStrength = uiState.signalStrength,
                    cardBgColor = colorScheme.surface,
                    textColor = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Communication Log Section
                CommunicationLogSection(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(horizontal = 16.dp),
                    logs = uiState.communicationLog,
                    committedText = uiState.committedReceivedText,
                    cardBgColor = colorScheme.surface,
                    textColor = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Controls
                BottomControlsSection(
                    speed = uiState.durationMultiplier,
                    onSpeedChange = viewModel::onSpeedChanged,
                    cardBgColor = colorScheme.surface,
                    textColor = colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun HeaderSection(
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.cd_menu), tint = primaryTextColor)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.header_morse_communicator),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )
            Text(
                text = stringResource(R.string.header_light_signal_connect),
                style = MaterialTheme.typography.labelSmall,
                color = secondaryTextColor
            )
        }
        IconButton(onClick = onSettingsClick) {
            Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.cd_settings), tint = primaryTextColor)
        }
    }
}

@Composable
fun MessageInputSection(
    modifier: Modifier,
    text: String,
    isVoiceListening: Boolean,
    onTextChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onSendClick: () -> Unit,
    onTestClick: () -> Unit,
    cardBgColor: Color,
    textColor: Color,
    inputBgColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .padding(12.dp)
    ) {
        Text(
            stringResource(R.string.message_title),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(inputBgColor)
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
                placeholder = {
                    Text(
                        stringResource(R.string.message_placeholder),
                        color = MaterialTheme.colorScheme.appMutedText
                    )
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onTestClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
            ) {
                Text(stringResource(R.string.button_test), color = Color.Black, fontSize = 10.sp)
            }

            FloatingActionButton(
                onClick = onVoiceClick,
                containerColor = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = stringResource(R.string.cd_voice),
                    tint = if (isVoiceListening) MaterialTheme.colorScheme.appReceiverGreen else Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }

            Button(
                onClick = onSendClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
            ) {
                Text(stringResource(R.string.button_send), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MorseSignalSection(
    sendingSignal: String,
    receivingSignal: String,
    liveMorseBuffer: String,
    signalStrength: Float,
    cardBgColor: Color,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.morse_signal_title),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            // Signal Strength/Alignment Indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.alignment_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.appMutedText,
                    fontSize = 8.sp
                )
                Spacer(Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { signalStrength },
                    modifier = Modifier
                        .width(60.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (signalStrength > 0.6f) MaterialTheme.colorScheme.appReceiverGreen else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.appInputSurface,
                    strokeCap = StrokeCap.Round
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.appInputSurface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SignalRow(stringResource(R.string.signal_sending), MaterialTheme.colorScheme.primary, sendingSignal)
            SignalRow(
                label = stringResource(R.string.signal_receiving),
                color = MaterialTheme.colorScheme.appReceiverGreen,
                signal = receivingSignal,
                buffer = liveMorseBuffer
            )
        }
    }
}

@Composable
fun SignalRow(
    label: String,
    color: Color,
    signal: String,
    buffer: String = ""
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.width(70.dp)
        )
        Text(
            text = if (buffer.isNotEmpty()) "$signal|$buffer" else signal,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CommunicationLogSection(
    modifier: Modifier,
    logs: List<LogEntry>,
    committedText: String,
    cardBgColor: Color,
    textColor: Color
) {
    val listState = rememberLazyListState()
    val hasConversation = logs.isNotEmpty() || committedText.isNotEmpty()

    // Auto-scroll to bottom when new logs arrive or text is being committed
    LaunchedEffect(logs.size, committedText) {
        if (hasConversation) {
            listState.animateScrollToItem(if (committedText.isNotEmpty()) logs.size else logs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .padding(12.dp)
    ) {
        Text(
            stringResource(R.string.communication_log_title),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        if (!hasConversation) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.65f))
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        stringResource(R.string.communication_log_empty_title),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
                    )
                    Text(
                        stringResource(R.string.communication_log_empty_body),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.appMutedText
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.appInputSurface)
                    .padding(8.dp)
            ) {
                items(logs) { entry ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            entry.timestamp,
                            color = MaterialTheme.colorScheme.appMutedText,
                            fontSize = 10.sp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            entry.type.name,
                            color = if (entry.type == LogType.SENT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.appReceiverGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            entry.message,
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Show live decoding text that hasn't been finalized yet
                if (committedText.isNotEmpty()) {
                    item {
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                "--:--",
                                color = MaterialTheme.colorScheme.appMutedText,
                                fontSize = 10.sp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.communication_log_typing),
                                color = MaterialTheme.colorScheme.appReceiverGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                committedText,
                                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomControlsSection(
    speed: Float,
    onSpeedChange: (Float) -> Unit,
    cardBgColor: Color,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Unified Speed Slider - Full Width for fine tuning
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(cardBgColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.transmission_unit_title),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = String.format(Locale.US, "%.2fx", speed),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = speed,
                onValueChange = onSpeedChange,
                valueRange = 0.5f..3.0f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                stringResource(R.string.transmission_unit_hint),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.appMutedText
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ListeningModeSection(
    isListening: Boolean,
    onToggleListening: () -> Unit,
    cardBgColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(R.string.listening_mode_title),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                stringResource(R.string.listening_mode_body),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.appMutedText
            )
        }
        Switch(
            checked = isListening,
            onCheckedChange = { onToggleListening() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.appSwitchUncheckedThumb,
                uncheckedTrackColor = MaterialTheme.colorScheme.appSwitchUncheckedTrack
            )
        )
    }
}
