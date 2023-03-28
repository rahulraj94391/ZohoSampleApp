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

    fun deny() {
        val timings: LongArray = longArrayOf(10, 10, 60, 10, 10, 60, 10, 10)
        val amplitudes: IntArray = intArrayOf(180, 180, 0, 80, 80, 0, 180, 180)
        val repeatIndex = -1 // Do not repeat.
        vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex))
    }

    fun doubleClick() {
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
        }
        else {
            vibrator.vibrate(60)
        }
    }

    fun heavy() {
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator.vibrate(VibrationEffect.createOneShot(10, 255))
        }
        else {
            vibrator.vibrate(10)
        }
    }
}