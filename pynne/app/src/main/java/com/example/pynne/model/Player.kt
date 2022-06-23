package com.example.pynne.model

data class Player(
    var name: String? = null,
    var score: Int? = null,
    var wins: Int? = null
) {
    companion object {
        fun generatePlayer() = Player()
    }
}
