package com.rama.chipdefense_copper

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class IconBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val icon: ImageView
    private val text: TextView

    init {
        inflate(context, R.layout.view_icon_badge, this)
        orientation = HORIZONTAL

        icon = findViewById(R.id.icon)
        text = findViewById(R.id.text)

        val a = context.obtainStyledAttributes(attrs, R.styleable.IconBadge)

        icon.setImageDrawable(a.getDrawable(R.styleable.IconBadge_icon))
        text.text = a.getString(R.styleable.IconBadge_text)

        a.recycle()

        isClickable = false
        isFocusable = false
    }
}
