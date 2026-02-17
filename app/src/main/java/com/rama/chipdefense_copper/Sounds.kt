package com.rama.chipdefense_copper

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object Sounds {
    private var soundPool: SoundPool? = null
    private var btnClickId: Int = 0
    private var btnClickBackId: Int = 0
    private var btnClickActiveId: Int = 0
    private var btnClickToggleId: Int = 0
    private var btnClickSelectId: Int = 0
    private var btnClickSpeedId: Int = 0
    private var btnClickPauseId: Int = 0

    fun init(context: Context) {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attrs)
            .build()

        // load your sound (put it in res/raw/click.wav)
        btnClickId = soundPool!!.load(context, R.raw.beep_35, 1)
        btnClickBackId = soundPool!!.load(context, R.raw.beep_67, 1)
        btnClickActiveId = soundPool!!.load(context, R.raw.beep_15, 1)
        btnClickToggleId = soundPool!!.load(context, R.raw.beep_81, 1)
        btnClickSelectId = soundPool!!.load(context, R.raw.beep_03, 1)
        btnClickSpeedId = soundPool!!.load(context, R.raw.beep_82, 1)
        btnClickPauseId = soundPool!!.load(context, R.raw.beep_58, 1)
    }

    fun playBtnSound() {
        soundPool?.play(
                btnClickId,
                1f,
                1f,
                1,
                0,
                1f
        )
    }

    fun playBtnBackSound() {
        soundPool?.play(
                btnClickBackId,
                1f,
                1f,
                1,
                0,
                1f
        )
    }

    fun playBtnActiveSound() {
        soundPool?.play(
                btnClickActiveId,
                1f,
                1f,
                1,
                0,
                1f
        )
    }

    fun playBtnToggleSound() {
        soundPool?.play(
                btnClickToggleId,
                .5f,
                .5f,
                1,
                0,
                1f
        )
    }

    fun playBtnSelectSound() {
        soundPool?.play(
                btnClickSelectId,
                1f,
                1f,
                1,
                0,
                1f
        )
    }

    fun playBtnSpeedSound() {
        soundPool?.play(
                btnClickSpeedId,
                1f,
                1f,
                1,
                0,
                1f
        )
    }

    fun playBtnPauseSound() {
        soundPool?.play(
                btnClickPauseId,
                .6f,
                .6f,
                1,
                0,
                1f
        )
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
