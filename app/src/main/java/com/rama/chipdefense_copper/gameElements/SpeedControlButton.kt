package com.rama.chipdefense_copper.gameElements

import android.graphics.*
import android.view.MotionEvent
import com.rama.chipdefense_copper.GameMechanics
import com.rama.chipdefense_copper.GameView
import com.rama.chipdefense_copper.effects.Fadable
import com.rama.chipdefense_copper.effects.Fader
import androidx.core.graphics.scale

class SpeedControlButton(
    val gameView: GameView,
    val gameMechanics: GameMechanics,
    var type: Type = Type.X0,
    private val panel: SpeedControl
) : Fadable {
    enum class Type { X0, X2, X3, X1, RETURN }

    var area = Rect()
    var paint = Paint()
    var alpha = 160
    private var bitmapOfType = hashMapOf<Type, Bitmap>()

    fun setSize(size: Int) {
        area = Rect(0, 0, size, size)
        bitmapOfType[Type.X0] = gameView.pauseIcon.scale(size, size)
        bitmapOfType[Type.X1] = gameView.playIcon.scale(size, size)
        bitmapOfType[Type.X2] = gameView.fastIcon.scale(size, size)
        bitmapOfType[Type.X3] = gameView.fastestIcon.scale(size, size)
        bitmapOfType[Type.RETURN] = gameView.returnIcon.scale(size, size)
    }

    override fun fadeDone(type: Fader.Type) {
    }

    override fun setOpacity(opacity: Float) {
        alpha = (opacity * 255).toInt()
    }

    fun onDown(p0: MotionEvent): Boolean {
        if (area.contains(p0.x.toInt(), p0.y.toInt())) {
            when (type) {
                Type.X0 -> {
                    gameView.gameActivity.setGameSpeed(GameMechanics.GameSpeed.X1)
                    gameView.gameActivity.changeToGamePhase(GameMechanics.GamePhase.PAUSED)
                    panel.resetButtons()
                    type = Type.X1
                }

                Type.X1 -> {
                    gameView.gameActivity.setGameSpeed(GameMechanics.GameSpeed.X1)
                    gameView.gameActivity.changeToGamePhase(GameMechanics.GamePhase.RUNNING)
                    panel.resetButtons()
                }

                Type.X2 -> {
                    gameView.gameActivity.setGameSpeed(GameMechanics.GameSpeed.X2)
                    gameView.gameActivity.changeToGamePhase(GameMechanics.GamePhase.RUNNING)
                    panel.resetButtons()
                    type = Type.X1
                }

                Type.X3 -> {
                    gameView.gameActivity.setGameSpeed(GameMechanics.GameSpeed.X3)
                    gameView.gameActivity.changeToGamePhase(GameMechanics.GamePhase.RUNNING)
                    panel.resetButtons()
                    type = Type.X1
                }

                Type.RETURN -> {
                    gameView.gameActivity.showReturnDialog()
                }
            }
            return true
        } else
            return false
    }

    fun display(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.alpha = alpha
        // canvas.drawRect(area, paint)
        bitmapOfType[type]?.let { canvas.drawBitmap(it, null, area, paint) }
    }

}