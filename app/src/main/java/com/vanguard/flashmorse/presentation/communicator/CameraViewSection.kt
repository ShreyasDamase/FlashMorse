package com.vanguard.flashmorse.presentation.communicator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.flashmorse.ui.theme.appInputSurface
import com.vanguard.flashmorse.ui.theme.appMutedText
import com.vanguard.flashmorse.ui.theme.appReceiverGreen

import androidx.camera.core.CameraControl

@Composable
fun CameraViewSection(
    modifier: Modifier,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit,
    onCameraControlReady: (CameraControl?) -> Unit = {},
    onBrightnessDetected: (Double) -> Unit = {},
    signalStrength: Float = 0f
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colorScheme.surface)
            .padding(12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (signalStrength > 150f/255f) colorScheme.appReceiverGreen else colorScheme.appMutedText)
            )

            Spacer(Modifier.width(4.dp))

            Text(
                "RECEIVER CAM (ROI)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorScheme.appInputSurface),
            contentAlignment = Alignment.Center
        ) {

            if (hasCameraPermission) {

                CameraPreview(
                    onCameraControlReady = onCameraControlReady,
                    onBrightnessDetected = onBrightnessDetected
                )
                
                // ROI Overlay Visual - Helps user center the light
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            width = 2.dp,
                            color = if (signalStrength > 150f/255f) colorScheme.appReceiverGreen else Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                )

            } else {

                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.outline)
                ) {

                    Text("Grant Camera Permission", fontSize = 10.sp, color = Color.White)
                }
            }
        }
    }
}
