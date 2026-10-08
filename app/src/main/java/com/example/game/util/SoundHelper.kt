package com.example.game.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundHelper(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (_: Exception) {}
    }

    fun playBlinkerClick() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
            vibrate(15)
        } catch (_: Exception) {}
    }

    fun playHorn() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_DIAL, 250)
            vibrate(80)
        } catch (_: Exception) {}
    }

    fun playGearShift() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 60)
            vibrate(25)
        } catch (_: Exception) {}
    }

    fun playMissionClear() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 300)
            vibrate(100)
        } catch (_: Exception) {}
    }

    fun playCrash() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 450)
            vibrate(350)
        } catch (_: Exception) {}
    }

    fun playNearMiss() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 80)
            vibrate(30)
        } catch (_: Exception) {}
    }

    private fun vibrate(millis: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(millis)
            }
        } catch (_: Exception) {}
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
