package com.example.pynne.model

data class Player(
    var name: String? = null,
    var score: String? = null,
    var wins: String? = null
) {
    companion object {
        fun generatePlayer() = Player()
    }
}
