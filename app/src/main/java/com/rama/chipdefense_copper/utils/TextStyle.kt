package com.rama.chipdefense_copper.utils

import android.content.Context
import android.graphics.Paint
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.rama.chipdefense_copper.R

fun textStyle(
    context: Context,
    colorParam: Int,
    textSizeSp: Float = 14f
): Paint = Paint().apply {

    color = ContextCompat.getColor(context, colorParam)

    typeface = ResourcesCompat.getFont(
            context,
            R.font.jersey25_regular
    )

    letterSpacing = 0.05f

    textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            textSizeSp,
            context.resources.displayMetrics
    )

    isAntiAlias = true
}

fun textStyleContent(
    context: Context,
    textSizeSp: Float = 18f,
    colorParam: Int = R.color.foreground_color
): Paint = textStyle(
        context = context,
        colorParam = colorParam,
        textSizeSp = textSizeSp
).apply {
    letterSpacing = 0.08f
}

fun textStyleDisplay(
    context: Context,
    textSizeSp: Float = 14f,
    colorParam: Int = R.color.dashboard_display_foregorund_color
): Paint = textStyle(
        context = context,
        colorParam = colorParam,
        textSizeSp = textSizeSp
).apply {
    letterSpacing = 0.08f
}

fun textStyleTitle(
    context: Context,
    textSizeSp: Float = 14f
): Paint = textStyle(
        context = context,
        colorParam = R.color.foreground_color,
        textSizeSp = textSizeSp
).apply {
    letterSpacing = 0.08f
}