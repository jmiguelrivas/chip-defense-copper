package com.rama.chipdefense_copper.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.rama.chipdefense_copper.BaseFullscreenActivity
import com.rama.chipdefense_copper.R


class AboutActivity : BaseFullscreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        applySystemInsets(findViewById<View>(R.id.root))
    }

    fun dismiss(@Suppress("UNUSED_PARAMETER") v: View) {
        finish()
    }
}