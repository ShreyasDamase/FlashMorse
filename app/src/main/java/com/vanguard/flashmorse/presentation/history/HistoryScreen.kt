package com.vanguard.flashmorse.presentation.history

import android.app.Application
import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanguard.flashmorse.presentation.communicator.LogEntry
import com.vanguard.flashmorse.presentation.communicator.LogType
import com.vanguard.flashmorse.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {
    private val sharedPrefs = application.getSharedPreferences("flashmorse_prefs", Context.MODE_PRIVATE)
    
    private val _historyLogs = MutableStateFlow<List<LogEntry>>(emptyList())
    val historyLogs = _historyLogs.asStateFlow()

    init {
        loadLogs()
    }

    fun loadLogs() {
        val serializedLogs = sharedPrefs.getString("comms_history", null)
        if (serializedLogs != null) {
            try {
                val logsList = mutableListOf<LogEntry>()
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
                _historyLogs.value = logsList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearHistory() {
        sharedPrefs.edit().remove("comms_history").apply()
        _historyLogs.value = emptyList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val logs by viewModel.historyLogs.collectAsState()
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val backgroundColor = if (isDark) Color(0xFF000000) else Color(0xFFE8DFD3)
    val cardBackgroundColor = if (isDark) Color(0xFF121212) else Color(0xFFF2ECE4)
    val primaryTextColor = if (isDark) Color(0xFFFFFFFF) else Color(0xFF4A453E)
    val secondaryTextColor = if (isDark) Color(0xFFB0B0B0) else Color(0xFF8B8479)
    val cardBodyColor = if (isDark) Color(0xFFE0E0E0) else Color.DarkGray

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Clear History", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to permanently delete all communication history logs?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showDeleteDialog = false
                    }
                ) {
                    Text("CLEAR", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("CANCEL", color = if (isDark) Color.LightGray else Color.Gray)
                }
            },
            containerColor = cardBackgroundColor,
            titleContentColor = primaryTextColor,
            textContentColor = cardBodyColor
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("COMMUNICATION HISTORY", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = primaryTextColor)
                    }
                },
                actions = {
                    if (logs.isNotEmpty()) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear All", tint = primaryTextColor)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = primaryTextColor
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = "Empty History",
                            modifier = Modifier.size(72.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No logged communications yet.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Transmitted & received Morse signals will appear here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(logs.reversed()) { entry ->
                        HistoryLogItem(entry, cardBackgroundColor, primaryTextColor, cardBodyColor, isDark)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryLogItem(
    entry: LogEntry,
    cardBgColor: Color,
    titleColor: Color,
    bodyColor: Color,
    isDark: Boolean
) {
    val typeColor = if (entry.type == LogType.SENT) Color(0xFFFFA500) else Color(0xFF4CAF50)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = cardBgColor
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.type.name,
                    color = typeColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = entry.timestamp,
                    color = if (isDark) Color(0xFF888888) else Color.Gray,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = entry.message,
                color = titleColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
