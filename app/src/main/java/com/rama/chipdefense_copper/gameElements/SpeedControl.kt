package com.rama.chipdefense_copper.gameElements

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.MotionEvent
import com.rama.chipdefense_copper.GameView
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.utils.setCenter
import com.rama.chipdefense_copper.utils.setLeft
import androidx.core.graphics.createBitmap

class SpeedControl(var gameView: GameView)
/** set of buttons that control the game speed, but also provide additional interaction such
 * as "lock scrolling" or "return to main menu". Also shows the level number.
 */
{
    private val gameMechanics = gameView.gameMechanics
    private var button1 =
        SpeedControlButton(gameView, gameMechanics, SpeedControlButton.Type.X0, this)
    private var button2 =
        SpeedControlButton(gameView, gameMechanics, SpeedControlButton.Type.X1, this)
    private var button3 =
        SpeedControlButton(gameView, gameMechanics, SpeedControlButton.Type.X2, this)
    private var button4 =
        SpeedControlButton(gameView, gameMechanics, SpeedControlButton.Type.X3, this)

    private var buttons = mutableListOf(button4, button3, button2, button1)
    private var areaCenter = Rect(0, 0, 0, 0)

    private var stageInfoText = ""
    private var statusInfoBitmap: Bitmap? = null
    private var bitmapPaint = Paint()

    fun setSize(parentArea: Rect) {
        val actualButtonSize =
            (GameView.speedControlButtonSize *
                    gameView.resources.displayMetrics.density *
                    if (gameView.gameActivity.settings.configUseLargeButtons) 1.6f else 1.0f).toInt()

        val margin = actualButtonSize / 2

        buttons = mutableListOf(button4, button3, button2, button1)

        buttons.forEach { it.setSize(actualButtonSize) }

        val centerY = parentArea.bottom - (margin / 3) - actualButtonSize / 2

        // Start from the right edge and move left
        var cursorX = parentArea.right - margin - actualButtonSize / 2

        button1.area.setCenter(cursorX, centerY)
        cursorX -= actualButtonSize + margin

        button2.area.setCenter(cursorX, centerY)
        cursorX -= actualButtonSize + margin

        button3.area.setCenter(cursorX, centerY)
        cursorX -= actualButtonSize + margin

        button4.area.setCenter(cursorX, centerY)

        areaCenter = Rect(
                button1.area.right,
                parentArea.bottom - actualButtonSize - margin,
                button4.area.left,
                parentArea.bottom - margin
        )
    }

    fun resetButtons() {
        button1.type = SpeedControlButton.Type.X0
        button2.type = SpeedControlButton.Type.X1
        button3.type = SpeedControlButton.Type.X2
        button4.type = SpeedControlButton.Type.X3
    }

    fun onDown(p0: MotionEvent): Boolean {
        buttons.forEach { if (it.onDown(p0)) return true }
        return false
    }

    fun display(canvas: Canvas) {
        buttons.forEach { it.display(canvas) }

        statusInfoBitmap?.let {
            val statusLineRect = Rect(0, 0, it.width, it.height)
            statusLineRect.setCenter(areaCenter.centerX(), areaCenter.centerY())
            canvas.drawBitmap(it, null, statusLineRect, bitmapPaint)
        }
    }
}