package com.rama.chipdefense_copper

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

class IconTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val iconView: ImageView
    private val textView: TextView

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_icon_text_button, this, true)

        iconView = findViewById(R.id.icon)
        textView = findViewById(R.id.text)

        isClickable = true
        isFocusable = true

        radius = 0f
        preventCornerOverlap = false
        useCompatPadding = false

        // Read XML attributes
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.IconTextButton)

            ta.getDrawable(R.styleable.IconTextButton_icon)?.let { drawable ->
                iconView.setImageDrawable(drawable)
            }

            ta.getString(R.styleable.IconTextButton_text)?.let { text ->
                textView.text = text
            }

            ta.recycle()
        }
    }

    /* --- Public API --- */

    fun setIcon(resId: Int) {
        iconView.setImageResource(resId)
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setText(resId: Int) {
        textView.setText(resId)
    }
}
