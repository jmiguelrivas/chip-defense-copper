package com.rama.chipdefense_copper.gameElements

import android.graphics.Canvas
import android.graphics.Rect
import com.rama.chipdefense_copper.networkmap.Network
import com.rama.chipdefense_copper.networkmap.Viewport

class EntryPoint(network: Network, gridX: Int, gridY: Int): Chip(network, gridX, gridY)
{
    init {
        chipData.type = ChipType.ENTRY
        actualRect = Rect(0,0,0,0)
    }

    override fun update() {
    }

    override fun display(canvas: Canvas, viewport: Viewport) {
        // super.display(canvas, viewport)
    }
}