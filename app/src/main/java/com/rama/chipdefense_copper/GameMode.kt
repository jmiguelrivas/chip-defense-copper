package com.rama.chipdefense_copper

sealed class GameMode(val rank: Int) : Comparable<GameMode> {
    object Basic : GameMode(0)
    object Endless : GameMode(1)

    override fun compareTo(other: GameMode): Int = this.rank - other.rank
}