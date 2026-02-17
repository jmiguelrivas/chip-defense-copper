package com.rama.chipdefense_copper

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object Sounds {
    private var soundPool: SoundPool? = null
    var enabled: Boolean = true
    private var btnClickId: Int = 0
    private var btnClickBackId: Int = 0
    private var btnClickActiveId: Int = 0
    private var btnClickToggleId: Int = 0
    private var btnClickSelectId: Int = 0
    private var btnClickSpeedId: Int = 0
    private var btnClickPauseId: Int = 0

    //    private var explotionId: Int = 0
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

//        explotionId = soundPool!!.load(context, R.raw.beep_94, 1)
        shootId = soundPool!!.load(context, R.raw.beep_94, 1)
    }

    private fun play(id: Int, left: Float = 1f, right: Float = 1f) {
        if (!enabled) return
        soundPool?.play(id, left, right, 1, 0, 1f)
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
        play(btnClickPauseId, .6f, .6f)
    }

//    fun playExplotionSound() {
//        play(explotionId, 1f, 1f)
//    }

    fun playShootSound() {
        play(shootId, 1f, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
