package com.rama.chipdefense_copper.effects
interface Explodable {
    fun remove()
    fun getPositionOnScreen(): Pair<Int, Int>

    val explosionColour: Int?
}