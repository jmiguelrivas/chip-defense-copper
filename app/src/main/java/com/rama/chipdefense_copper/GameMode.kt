package com.rama.chipdefense_copper

sealed class GameMode {
    object Basic : GameMode()
    object Endless : GameMode()
}
