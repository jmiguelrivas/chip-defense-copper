package com.rama.chipdefense_copper.gameElements

import android.graphics.Canvas
import com.rama.chipdefense_copper.networkmap.Viewport

abstract class GameElement {
    abstract fun update()

    abstract fun display(canvas: Canvas, viewport: Viewport)
}