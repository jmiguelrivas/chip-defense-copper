@file:Suppress("DEPRECATION")

package com.rama.chipdefense_copper

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.rama.chipdefense_copper.effects.Fadable
import com.rama.chipdefense_copper.effects.Fader
import com.rama.chipdefense_copper.utils.setTopLeft
import com.rama.chipdefense_copper.utils.textStyleContent
import kotlin.random.Random

class Instructions(
    val gameView: GameView, var stage: Stage.Identifier, var showLeaveDialogue: Boolean,
    private var callback: (() -> Unit)?
) : Fadable {
    private val margin = 32
    private var topInset = 0
    var myArea = Rect()
    var vertOffset = 0f

    var alpha = 0
    val resources: Resources = gameView.resources
    var paint = Paint()
    private var funFact = if (Random.nextFloat() > 0.3)
        resources.getString(R.string.instr_did_you_know) + "\n\n" +
                resources.getStringArray(R.array.fun_fact).random()
    // resources.getStringArray(R.array.fun_fact).last() // for debugging purposes
    else ""
    var bitmap: Bitmap =
        createBitmap(instructionText(stage.number), gameView.width - 2 * margin)

    fun setTextArea(rect: Rect) {
        // Get the top inset for notch/status bar
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            gameView.rootWindowInsets?.let { insets ->
                topInset = insets.displayCutout?.safeInsetTop ?: insets.systemWindowInsetTop
            }
        }

        // Apply margin + inset
        myArea = Rect(
                rect.left + margin,
                rect.top + margin + topInset,
                rect.right - margin,
                rect.bottom - margin
        )
    }

    private fun instructionText(level: Int): String {
        if (gameView.intermezzo.type in setOf(Intermezzo.Type.GAME_LOST, Intermezzo.Type.GAME_WON))
            return ""
        else if (showLeaveDialogue)
            return ""
        else if (stage.series == GameMechanics.SERIES_NORMAL) {
            return when (level) {
                1 -> resources.getString(R.string.instr_1)
                2 -> resources.getString(R.string.instr_2)
                3 -> resources.getString(R.string.instr_3)
                4 -> resources.getString(R.string.instr_4)
                5 -> resources.getString(R.string.instr_5)
                6 -> resources.getString(R.string.instr_6)
                7 -> resources.getString(R.string.instr_7)
                8 -> resources.getString(R.string.instr_7a)
                9 -> resources.getString(R.string.instr_8)
                14 -> resources.getString(R.string.instr_9)
                20 -> resources.getString(R.string.instr_10)
                23 -> resources.getString(R.string.instr_12)
                24 -> resources.getString(R.string.instr_16)
                10 -> resources.getString(R.string.instr_11)
                21 -> resources.getString(R.string.instr_13)
                27 -> resources.getString(R.string.instr_14)
                28 -> resources.getString(R.string.instr_15).format(GameMechanics.temperatureLimit)
                30 -> resources.getString(R.string.instr_17)
                31 -> resources.getString(R.string.instr_18)
                32 -> resources.getString(R.string.instr_23)
                else -> ""
            }
        } else if (stage.series == GameMechanics.SERIES_TURBO) {
            return when (level) {
                1 -> resources.getString(R.string.instr_2_1)
                else -> ""
            }
        } else if (stage.series == GameMechanics.SERIES_ENDLESS) {
            return when (level) {
                1 -> resources.getString(R.string.instr_endless)
                98 -> resources.getString(R.string.instr_2_98)
                else -> funFact
            }
        } else
            return ""
    }

    init {
        Fader(gameView, this, Fader.Type.APPEAR, Fader.Speed.SLOW)
    }

    override fun fadeDone(type: Fader.Type) {
        callback?.let { it() }  // call callback function, if defined.
    }

    override fun setOpacity(opacity: Float) {
        alpha = (opacity * 255).toInt()
    }

    fun display(canvas: Canvas) {
        paint.alpha = alpha
        val sourceRect = Rect(myArea).setTopLeft(0, vertOffset.toInt())
        canvas.drawBitmap(bitmap, sourceRect, myArea, paint)
    }

    private fun createBitmap(text: String, width: Int): Bitmap {
        // Title layout
        val title = "S${stage.series}-${stage.number}\n"
        val titlePaint = TextPaint(textStyleContent(gameView.context, textSizeSp = 22f))
        val titleLayout = StaticLayout(
                title, titlePaint, width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false
        )

        // Text layout
        val textPaint = TextPaint(textStyleContent(gameView.context))
        val textLayout = StaticLayout(
                text, textPaint, width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false
        )

        // Total bitmap height = title + text
        val bitmapHeight = titleLayout.height + textLayout.height
        val bitmap = Bitmap.createBitmap(width, bitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw title at the top
        canvas.save()
        canvas.translate(0f, 0f)
        titleLayout.draw(canvas)
        canvas.restore()

        // Draw text below title
        canvas.save()
        canvas.translate(0f, titleLayout.height.toFloat()) // shift down by title height
        textLayout.draw(canvas)
        canvas.restore()

        return bitmap
    }

}