package com.rama.chipdefense_copper.gameElements

import android.graphics.*
import androidx.core.content.res.ResourcesCompat
import com.rama.chipdefense_copper.GameView
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.effects.Fadable
import com.rama.chipdefense_copper.effects.Fader
import com.rama.chipdefense_copper.utils.displayTextCenteredInRect
import com.rama.chipdefense_copper.utils.inflate
import com.rama.chipdefense_copper.utils.setCenter
import java.util.*

class Button(
    val gameView: GameView,
    var text: String
) : Fadable {

    private var alpha = 255

    val area = Rect()
    val touchableArea = Rect()

    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = gameView.resources.getColor(R.color.button_color)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = ResourcesCompat.getFont(
                gameView.context,
                R.font.jersey25_regular
        ) ?: Typeface.DEFAULT

        color = gameView.resources.getColor(R.color.foreground_color)
        textSize = 32f * gameView.scaleFactor
        letterSpacing = 0.08f
        isFakeBoldText = true
    }

    init {
        val padding =
            (16f * gameView.resources.displayMetrics.density).toInt()

        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)

        val width = bounds.width() + padding * 2
        val height = bounds.height() + padding * 2

        area.set(0, 0, width, height)

        touchableArea.set(area)
        touchableArea.inflate(padding / 2)
    }

    fun alignLeft(left: Int, top: Int) {
        area.offsetTo(left, top)
        touchableArea.setCenter(
                area.centerX(),
                area.centerY()
        )
    }

    fun alignRight(right: Int, top: Int) {
        area.offsetTo(right - area.width(), top)
        touchableArea.setCenter(
                area.centerX(),
                area.centerY()
        )
    }

    override fun fadeDone(type: Fader.Type) {}

    override fun setOpacity(opacity: Float) {
        alpha = (opacity * 255).toInt()
    }

    fun display(canvas: Canvas) {
        buttonPaint.alpha = alpha
        textPaint.alpha = alpha

        canvas.drawRect(area, buttonPaint)

        area.displayTextCenteredInRect(
                canvas,
                text.uppercase(Locale.getDefault()),
                textPaint
        )
    }
}
