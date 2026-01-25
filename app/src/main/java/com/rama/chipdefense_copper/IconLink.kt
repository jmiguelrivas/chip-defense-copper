package com.rama.chipdefense_copper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class IconLink @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val icon: ImageView
    private val text: TextView
    private val iconContainer: FrameLayout

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context)
            .inflate(R.layout.view_icon_link, this, true)

        icon = findViewById(R.id.icon)
        text = findViewById(R.id.text)
        iconContainer = findViewById(R.id.icon_container)

        val a = context.obtainStyledAttributes(attrs, R.styleable.IconLink)

        val url = a.getString(R.styleable.IconLink_url)

        // Basic content
        icon.setImageDrawable(a.getDrawable(R.styleable.IconLink_icon))
        text.text = a.getString(R.styleable.IconLink_text)

        isClickable = true
        isFocusable = true

        if (!url.isNullOrBlank()) {
            setOnClickListener {
                context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                )
            }
        }

        a.recycle()
    }
}
