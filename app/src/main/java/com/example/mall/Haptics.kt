package com.example.mall

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class Haptics(private val vibrator: Vibrator) {
    fun light() {
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator.vibrate(VibrationEffect.createOneShot(10, 120))
        }
        else {
            vibrator.vibrate(10)
        }
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