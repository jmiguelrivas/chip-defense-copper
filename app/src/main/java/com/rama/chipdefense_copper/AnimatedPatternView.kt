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
    private val matrix = Matrix()
    private var shader: BitmapShader? = null
    private var offset = 0f

    init {
        // default pattern
        var patternResId = R.drawable.background_pattern_night

        // read XML attribute
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.AnimatedPatternView)
            val mode = typedArray.getString(R.styleable.AnimatedPatternView_mode)
            if (!mode.isNullOrEmpty()) {
                // dynamically get drawable id from name
                val resId = resources.getIdentifier(
                        "background_pattern_$mode",
                        "drawable",
                        context.packageName
                )
                if (resId != 0) {
                    patternResId = resId
                }
            }
            typedArray.recycle()
        }

        val bitmap = BitmapFactory.decodeResource(resources, patternResId)
        shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        paint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        offset += 0.5f
        shader?.setLocalMatrix(matrix.apply { setTranslate(offset, offset) })
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        invalidate()
    }
}