package com.rama.chipdefense_copper.activities

import android.os.Bundle
import android.view.View
import com.rama.chipdefense_copper.BaseFullscreenActivity
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.Sounds


class AboutActivity : BaseFullscreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        applySystemInsets(findViewById<View>(R.id.root))
    }

    fun dismiss(@Suppress("UNUSED_PARAMETER") v: View) {
        Sounds.playBtnBackSound()
        finish()
    }
}