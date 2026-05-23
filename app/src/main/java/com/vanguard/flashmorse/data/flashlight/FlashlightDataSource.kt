package com.vanguard.flashmorse.data.flashlight

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.camera.core.CameraControl
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashlightDataSource @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val cameraManager by lazy {
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private val cameraId by lazy {
        try {
            cameraManager.cameraIdList.firstOrNull() ?: "0"
        } catch (e: Exception) {
            "0"
        }
    }

    private var cameraControl: CameraControl? = null

    /**
     * Set the CameraControl from an active CameraX session.
     * This allows using the torch without conflicting with the open camera.
     */
    fun setCameraControl(control: CameraControl?) {
        this.cameraControl = control
    }

    fun turnOn() {
        val control = cameraControl
        if (control != null) {
            try {
                control.enableTorch(true)
            } catch (e: Exception) {
                e.printStackTrace()
                fallbackTurnOn()
            }
        } else {
            fallbackTurnOn()
        }
    }

    private fun fallbackTurnOn() {
        try {
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: Exception) {
            // Log error instead of crashing if camera is in use
            e.printStackTrace()
        }
    }

    fun turnOff() {
        val control = cameraControl
        if (control != null) {
            try {
                control.enableTorch(false)
            } catch (e: Exception) {
                e.printStackTrace()
                fallbackTurnOff()
            }
        } else {
            fallbackTurnOff()
        }
    }

    private fun fallbackTurnOff() {
        try {
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
