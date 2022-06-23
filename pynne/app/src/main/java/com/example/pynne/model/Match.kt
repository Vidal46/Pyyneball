package com.example.pynne.model

data class Match(
    val firstPlayer: Player,
    val secondPlayer: Player
) {

    fun getWinner() =
        if (firstPlayer.score ?: 0 > secondPlayer.score ?: 0) firstPlayer else secondPlayer
}