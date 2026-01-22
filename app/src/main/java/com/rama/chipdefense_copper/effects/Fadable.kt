package com.rama.chipdefense_copper.effects

interface Fadable {
    fun fadeDone(type: Fader.Type)
    fun setOpacity(opacity: Float)
}