package com.vanguard.flashmorse.data.flashlight
 
import com.vanguard.flashmorse.domain.model.MorseSymbol
import com.vanguard.flashmorse.domain.model.MorseSymbol.Companion.scaledDuration
import kotlinx.coroutines.delay
import javax.inject.Inject

class MorseFlashlightTransmitter @Inject constructor(

    private val flashlightDataSource: FlashlightDataSource

) {

    /**
     * Plays Morse symbols using flashlight.
     */
    suspend fun transmit(
        symbols: List<MorseSymbol>,
        speedMultiplier: Float
    ) {

        for (symbol in symbols) {

            if (symbol.isOn) {

                flashlightDataSource.turnOn()

            } else {

                flashlightDataSource.turnOff()
            }

            delay(
                symbol.scaledDuration(
                    speedMultiplier
                )
            )
        }

        // Safety OFF at end
        flashlightDataSource.turnOff()
    }
}