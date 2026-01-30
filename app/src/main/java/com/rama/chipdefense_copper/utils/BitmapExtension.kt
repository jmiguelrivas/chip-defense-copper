@file:Suppress("DEPRECATION")

package com.rama.chipdefense_copper.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat

fun Bitmap.flipHorizontally(): Bitmap
        /** flips the bitmap horizontally. Taken from
         * https://stackoverflow.com/questions/36493977/flip-a-bitmap-image-horizontally-or-vertically
         */
{
    val matrix = Matrix().apply { postScale(-1f, 1f, width / 2f, height / 2f) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.flipVertically(): Bitmap
        /** flips the bitmap vertically. */
{
    val matrix = Matrix().apply { postScale(1f, -1f, width / 2f, height / 2f) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.clear()
        /** just a function to make the naming clearer */
{
    eraseColor(Color.TRANSPARENT)
}

fun Context.vectorToBitmap(
    @DrawableRes drawableId: Int,
    width: Int,
    height: Int,
    @StyleRes styleRes: Int? = null
): Bitmap {
    val context =
        if (styleRes != null)
            ContextThemeWrapper(this, styleRes)
        else
            this

    val drawable = ResourcesCompat.getDrawable(
            context.resources,
            drawableId,
            context.theme
    ) ?: error("Drawable not found: $drawableId")

    drawable.mutate()

    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { bitmap ->
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
    }
}

