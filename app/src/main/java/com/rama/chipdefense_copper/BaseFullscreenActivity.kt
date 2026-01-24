package com.rama.chipdefense_copper

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.rama.chipdefense_copper.utils.dp

abstract class BaseFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.attributes = window.attributes.apply {
            layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        hideSystemBars()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    protected fun hideSystemBars() {
        val controller =
            WindowCompat.getInsetsController(window, window.decorView)
        controller?.let {
            it.hide(WindowInsets.Type.systemBars())
            it.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    protected fun applySystemInsets(root: View, paddingDp: Int = 16) {
        val padding = dp(paddingDp)

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val cutoutInsets = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val gestureInsets = insets.getInsets(WindowInsetsCompat.Type.systemGestures())

            val left = maxOf(cutoutInsets.left, gestureInsets.left)
            val top = maxOf(cutoutInsets.top, gestureInsets.top)
            val right = maxOf(cutoutInsets.right, gestureInsets.right)
            val bottom = maxOf(cutoutInsets.bottom, gestureInsets.bottom)

            view.setPadding(
                    left + padding,
                    top + padding,
                    right + padding,
                    bottom + padding
            )

            insets
        }

        root.requestApplyInsets()
    }

}
