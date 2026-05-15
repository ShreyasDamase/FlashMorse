package com.example.flashmorse.presentation.sender

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SenderScreen(
) {
    val viewModel: SenderViewModel = hiltViewModel()
    val context = LocalContext.current
    val cameraManager = remember {
        context.getSystemService(
            Context.CAMERA_SERVICE
        ) as CameraManager
    }

    val cameraId = rememberSaveable {
        cameraManager.cameraIdList.first()
    }

    var isFlashOn by rememberSaveable() { mutableStateOf(false) }
    val text = viewModel.text.collectAsStateWithLifecycle()
    val encodedCode = viewModel.encodedCode.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            isFlashOn = !isFlashOn
            cameraManager.setTorchMode(cameraId, isFlashOn)
        }) {
            Text(
                text = "Toggle Flashlight",
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = text.value,
            onValueChange = viewModel::onTextChanged,
            label = { Text("Enter Message") },
            modifier = Modifier.fillMaxWidth()
        )

        SelectionContainer {

            Text(
                text = encodedCode.value,

                modifier = Modifier.fillMaxWidth(),

                style = MaterialTheme.typography.headlineMedium,
            )
        }

    }

}