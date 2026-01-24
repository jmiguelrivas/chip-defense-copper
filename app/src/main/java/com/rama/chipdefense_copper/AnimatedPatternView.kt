package com.rama.chipdefense_copper

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class AnimatedPatternView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint()
    private val shader: BitmapShader
    private val matrix = Matrix()

    private var offset = 0f

    init {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pattern)

        shader = BitmapShader(
                bitmap,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT
        )

        paint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        offset += 0.5f          // speed
        matrix.setTranslate(offset, offset)  // diagonal
        shader.setLocalMatrix(matrix)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        invalidate() // keep animating
    }
}
