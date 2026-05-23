package com.vanguard.flashmorse.presentation.history

import android.app.Application
import android.content.Context
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.vanguard.flashmorse.R
import com.vanguard.flashmorse.presentation.communicator.LogEntry
import com.vanguard.flashmorse.presentation.communicator.LogType
import com.vanguard.flashmorse.ui.theme.appBodyText
import com.vanguard.flashmorse.ui.theme.appHistoryReceivedGreen
import com.vanguard.flashmorse.ui.theme.appMutedText
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
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val logs by viewModel.historyLogs.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.history_clear_title), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.history_clear_body)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.history_clear_confirm), color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.history_clear_cancel), color = colorScheme.appMutedText)
                }
            },
            containerColor = colorScheme.surface,
            titleContentColor = colorScheme.onSurface,
            textContentColor = colorScheme.appBodyText
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back), tint = colorScheme.onBackground)
                    }
                },
                actions = {
                    if (logs.isNotEmpty()) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.cd_clear_all), tint = colorScheme.onBackground)
                        }
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
        ) {
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = stringResource(R.string.cd_empty_history),
                            modifier = Modifier.size(72.dp),
                            tint = colorScheme.appMutedText.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            stringResource(R.string.history_empty_title),
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorScheme.appMutedText,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            stringResource(R.string.history_empty_body),
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.appMutedText.copy(alpha = 0.8f),
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
                        HistoryLogItem(entry)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryLogItem(entry: LogEntry) {
    val colorScheme = MaterialTheme.colorScheme
    val typeColor = if (entry.type == LogType.SENT) colorScheme.primary else colorScheme.appHistoryReceivedGreen
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
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
                    color = colorScheme.appMutedText,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = entry.message,
                color = colorScheme.onSurface,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
