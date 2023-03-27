package com.example.mall

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class Haptics(private val vibrator: Vibrator) {
    fun light() {
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator.vibrate(VibrationEffect.createOneShot(10, 80))
        }
        else {
            vibrator.vibrate(10)
        }
    }

    /*fun light() {
        val timings: LongArray = longArrayOf(50, 50, 50, 50, 50, 100, 350, 25, 25, 25, 25, 200)
        val amplitudes: IntArray = intArrayOf(33, 51, 75, 113, 170, 255, 0, 38, 62, 100, 160, 255)
        val repeatIndex = -1 // Do not repeat.

        vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex))
    }*/


    fun heavy() {
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
        }
        else {
            vibrator.vibrate(60)
        }
    }
}