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
    title: String,
    description: String,
    @StyleRes styleRes: Int? = null
): Bitmap {
    val width = 400
    val height = 400
    val padding = 56

    val themedContext =
        if (styleRes != null) ContextThemeWrapper(this, styleRes) else this

    val drawable = AppCompatResources.getDrawable(themedContext, R.drawable.vector_cpu)
        ?: error("Drawable not found: R.drawable.vector_cpu")

//    val colorParam = themedContext.resolveColorAttr(R.attr.chipTextColor)

    // --- TEXT PAINTS (same pattern as the rest of the app)
    val titlePaint = TextPaint(
            textStyle(
                    themedContext,
                    colorParam = R.color.chip_title,
                    textSizeSp = 16f
            )
    )

    val descriptionPaint = TextPaint(
            textStyle(
                    themedContext,
                    colorParam = R.color.chip_description,
                    textSizeSp = 9f
            )
    )

    // --- TEXT LAYOUTS
    val titleLayout = StaticLayout(
            title.toUpperCase(),
            titlePaint,
            width - padding * 2,
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            false
    )

    val descriptionLayout = StaticLayout(
            ("<" + description + ">").toUpperCase(),
            descriptionPaint,
            width - padding * 2,
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            false
    )

    // --- TOTAL TEXT HEIGHT
    val textYAdjustment = 10
    val totalTextHeight = titleLayout.height + descriptionLayout.height
    val startY = ((height - totalTextHeight) / 2f) - textYAdjustment  // <-- vertical center

    // --- BITMAP
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // --- DRAW IMAGE
    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)


    // --- DRAW TITLE (vertically centered)
    canvas.save()
    canvas.translate(padding.toFloat(), startY)
    titleLayout.draw(canvas)
    canvas.restore()

    // --- DRAW DESCRIPTION (below title)
    canvas.save()
    canvas.translate(padding.toFloat(), startY + titleLayout.height)
    descriptionLayout.draw(canvas)
    canvas.restore()

    return bitmap
}

