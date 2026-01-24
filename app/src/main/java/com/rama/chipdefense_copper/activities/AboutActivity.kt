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
        val info = packageManager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        val versionView: TextView = findViewById(R.id.about_version)
        versionView.text = getString(R.string.about_version).format(info.versionName)
    }

    fun dismiss(@Suppress("UNUSED_PARAMETER") v: View) {
        finish()
    }

    fun wiki(@Suppress("UNUSED_PARAMETER") v: View) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ochadenas/cpudefense/wiki/Chip-Defense"))
        try {
            startActivity(browserIntent)
        } catch (_: Exception) {
        }  // come here if no external app can handle the request
    }
}