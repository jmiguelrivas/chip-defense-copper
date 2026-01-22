package com.rama.chipdefense_copper

import org.junit.Test

import org.junit.Assert.*

class GameMechanicsTest {
    val gameMechanics = GameMechanics()

    @Test
    fun globalSpeedFactor() {
        assertEquals(gameMechanics.globalSpeedFactor(), 0.512f)
    }
}