package com.rama.chipdefense_copper

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

object Sounds {
    private var soundPool: SoundPool? = null
    private var mediaPlayer: MediaPlayer? = null
    var enabled: Boolean = true
    private var btnClickId: Int = 0
    private var btnClickBackId: Int = 0
    private var btnClickActiveId: Int = 0
    private var btnClickToggleId: Int = 0
    private var btnClickSelectId: Int = 0
    private var btnClickSpeedId: Int = 0
    private var btnClickPauseId: Int = 0
    private var buildId: Int = 0
    private var monitorId: Int = 0
    private var fadeHandler: Handler? = null
    private var fadeRunnable: Runnable? = null

    private var shootId: Int = 0

    fun init(context: Context) {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attrs)
            .build()

        btnClickId = soundPool!!.load(context, R.raw.beep_35, 1)
        btnClickBackId = soundPool!!.load(context, R.raw.beep_67, 1)
        btnClickActiveId = soundPool!!.load(context, R.raw.beep_15, 1)
        btnClickToggleId = soundPool!!.load(context, R.raw.beep_81, 1)
        btnClickSelectId = soundPool!!.load(context, R.raw.beep_03, 1)
        btnClickSpeedId = soundPool!!.load(context, R.raw.beep_82, 1)
        btnClickPauseId = soundPool!!.load(context, R.raw.beep_58, 1)

        shootId = soundPool!!.load(context, R.raw.beep_94, 1)
        buildId = soundPool!!.load(context, R.raw.beep_84, 1)
        monitorId = soundPool!!.load(context, R.raw.beep_86, 1)

        mediaPlayer = MediaPlayer.create(context, R.raw.every_friday_nes)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(1f, 1f)
    }

    private fun play(id: Int, left: Float = 1f, right: Float = 1f, loop: Int = 0) {
        if (!enabled) return
        soundPool?.play(id, left, right, 1, loop, 1f)
    }

    fun playBtnSound() {
        play(btnClickId)
    }

    fun playBtnBackSound() {
        play(btnClickBackId)
    }

    fun playBtnActiveSound() {
        play(btnClickActiveId)
    }

    fun playBtnToggleSound() {
        play(btnClickToggleId, .5f, .5f)
    }

    fun playBtnSelectSound() {
        play(btnClickSelectId)
    }

    fun playBtnSpeedSound() {
        play(btnClickSpeedId)
    }

    fun playBtnPauseSound() {
        play(btnClickPauseId, .4f, .4f)
    }

    fun playShootSound() {
        play(shootId, 1f, 1f)
    }

    fun playBuildSound() {
        play(buildId, 1f, 1f)
    }

    fun playMonitorSound() {
        play(monitorId, 1f, 1f)
    }

    fun playSoundtrack(durationMs: Long = 500) {
        if (!enabled) return
        val player = mediaPlayer ?: return

        // reset any ongoing fade
        fadeHandler?.removeCallbacksAndMessages(null)

        // start volume at 0
        player.setVolume(0f, 0f)
        if (!player.isPlaying) player.start()

        val steps = 20
        val delay = durationMs / steps
        var currentStep = -15

        fadeHandler = Handler(Looper.getMainLooper())
        fadeRunnable = object : Runnable {
            override fun run() {
                val volume = (currentStep.toFloat() / steps.toFloat())
                player.setVolume(volume, volume)

                currentStep++

                if (currentStep <= steps) {
                    fadeHandler?.postDelayed(this, delay)
                } else {
                    player.setVolume(.5f, .5f)
                }
            }
        }

        fadeHandler?.post(fadeRunnable!!)
    }

    fun stopSoundtrack(durationMs: Long = 0) {
        val player = mediaPlayer ?: return

        fadeHandler?.removeCallbacksAndMessages(null)

        val steps = 20
        val delay = durationMs / steps
        var currentStep = 0

        fadeHandler = Handler(Looper.getMainLooper())

        fadeRunnable = object : Runnable {
            override fun run() {
                val volume = 1f - (currentStep.toFloat() / steps.toFloat())
                player.setVolume(volume, volume)

                currentStep++

                if (currentStep <= steps) {
                    fadeHandler?.postDelayed(this, delay)
                } else {
                    // finally stop when fully faded out
                    player.stop()
                    player.prepareAsync()

                    // reset volume so next play starts at full volume
                    player.setVolume(.5f, .5f)
                }
            }
        }

        fadeHandler?.post(fadeRunnable!!)
    }
}
