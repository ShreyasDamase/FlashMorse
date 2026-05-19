package com.example.flashmorse.data.flashlight
 
import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FlashlightDataSource @Inject constructor(

    @ApplicationContext
    context: Context

) {

    // Android system flashlight service
    private val cameraManager =
        context.getSystemService(
            Context.CAMERA_SERVICE
        ) as CameraManager

    // First available camera with flash
    private val cameraId =
        cameraManager.cameraIdList.first()


    /**
     * Turns flashlight ON
     */
    fun turnOn() {

        cameraManager.setTorchMode(
            cameraId,
            true
        )
    }


    /**
     * Turns flashlight OFF
     */
    fun turnOff() {

        cameraManager.setTorchMode(
            cameraId,
            false
        )
    }
}