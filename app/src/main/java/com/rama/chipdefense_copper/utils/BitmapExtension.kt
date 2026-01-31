@file:Suppress("DEPRECATION")

package com.rama.chipdefense_copper.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.rama.chipdefense_copper.R

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
    val themedContext =
        if (styleRes != null) ContextThemeWrapper(this, styleRes) else this

    val drawable = AppCompatResources.getDrawable(themedContext, drawableId)
        ?: error("Drawable not found: $drawableId")

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)

    return bitmap
}

@ColorInt
fun Context.resolveColorAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    val found = theme.resolveAttribute(attr, typedValue, true)

    if (!found) {
        error("Attribute not found: $attr")
    }

    return if (typedValue.resourceId != 0) {
        ContextCompat.getColor(this, typedValue.resourceId)
    } else {
        typedValue.data
    }
}

fun Context.createHero(
    @DrawableRes drawableId: Int,
    name: String = "Alan Turing",
    country: String = "United Kingdom",
    @StyleRes styleRes: Int? = null
): Bitmap {
    val width = 250
    val height = 250
    val padding = 16

    val themedContext =
        if (styleRes != null) ContextThemeWrapper(this, styleRes) else this

    val drawable = AppCompatResources.getDrawable(themedContext, drawableId)
        ?: error("Drawable not found: $drawableId")

    val colorParam =
        R.color.background_tertiary_color//themedContext.resolveColorAttr(R.attr.chipTextColor)

    // --- TEXT PAINTS (same pattern as the rest of the app)
    val titlePaint = TextPaint(
            textStyle(
                    themedContext,
                    colorParam = colorParam,
                    textSizeSp = 12f
            )
    )

    val countryPaint = TextPaint(
            textStyle(
                    themedContext,
                    colorParam = colorParam,
                    textSizeSp = 12f
            )
    )

    // --- TEXT LAYOUTS
    val titleLayout = StaticLayout(
            name,
            titlePaint,
            width - padding * 2,
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            false
    )

    val countryLayout = StaticLayout(
            country,
            countryPaint,
            width - padding * 2,
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            false
    )

    // --- BITMAP
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // --- DRAW IMAGE
    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)

    // --- DRAW TITLE (top)
    canvas.save()
    canvas.translate(padding.toFloat(), 8f)
    titleLayout.draw(canvas)
    canvas.restore()

    // --- DRAW COUNTRY (below title)
    canvas.save()
    canvas.translate(padding.toFloat(), (8 + titleLayout.height).toFloat())
    countryLayout.draw(canvas)
    canvas.restore()

    return bitmap
}


