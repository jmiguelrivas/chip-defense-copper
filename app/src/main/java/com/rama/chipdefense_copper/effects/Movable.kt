package com.rama.chipdefense_copper.effects

interface Movable {
    fun moveStart()
    fun moveDone()
    fun setCenter(x: Int, y: Int)
}