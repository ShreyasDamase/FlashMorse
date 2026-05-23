package com.vanguard.flashmorse.presentation.communicator

import android.annotation.SuppressLint
import android.content.Context
import android.util.Size
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(
    onCameraControlReady: (CameraControl?) -> Unit = {},
    onBrightnessDetected: (Double) -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            analysisExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { context: Context ->
            val previewView = PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val lowResSelector = ResolutionSelector.Builder()
                    .setResolutionStrategy(
                        ResolutionStrategy(
                            Size(640, 480),
                            ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER
                        )
                    ).build()

                val preview = Preview.Builder()
                    .setResolutionSelector(lowResSelector)
                    .build()
                
                preview.surfaceProvider = previewView.surfaceProvider

                val imageAnalysis = ImageAnalysis.Builder()
                    .setResolutionSelector(lowResSelector)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                    .build()

                imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
                    val plane = imageProxy.planes[0]
                    val buffer = plane.buffer
                    val width = imageProxy.width
                    val height = imageProxy.height
                    val rowStride = plane.rowStride
                    
                    // Use a tight center ROI and average only the brightest samples so
                    // room lighting does not look like a constant "ON" signal.
                    val roiWidth = width / 8
                    val roiHeight = height / 8
                    val startX = (width - roiWidth) / 2
                    val startY = (height - roiHeight) / 2
                    
                    val topSamples = IntArray(12)
                    var topCount = 0
                    
                    for (y in startY until startY + roiHeight step 2) {
                        for (x in startX until startX + roiWidth step 2) {
                            val index = y * rowStride + x
                            val brightness = buffer.get(index).toInt() and 0xFF
                            if (topCount < topSamples.size) {
                                topSamples[topCount] = brightness
                                topCount++
                            } else {
                                var minIndex = 0
                                for (i in 1 until topSamples.size) {
                                    if (topSamples[i] < topSamples[minIndex]) {
                                        minIndex = i
                                    }
                                }
                                if (brightness > topSamples[minIndex]) {
                                    topSamples[minIndex] = brightness
                                }
                            }
                        }
                    }
                    
                    val brightnessScore = if (topCount == 0) {
                        0.0
                    } else {
                        topSamples.take(topCount).average()
                    }

                    onBrightnessDetected(brightnessScore)
                    imageProxy.close()
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                    
                    val control = camera.cameraControl
                    
                    // ELIMINATE DISTORTION: Lock exposure and focus to the center
                    val factory = SurfaceOrientedMeteringPointFactory(1f, 1f)
                    val centerPoint = factory.createPoint(0.5f, 0.5f)
                    val action = FocusMeteringAction.Builder(centerPoint)
                        .disableAutoCancel() // Keep locked
                        .build()
                    
                    control.startFocusAndMetering(action)
                    onCameraControlReady(control)

                } catch (e: Exception) {
                    e.printStackTrace()
                    onCameraControlReady(null)
                }

            }, ContextCompat.getMainExecutor(context))

            previewView
        }
    )
}
