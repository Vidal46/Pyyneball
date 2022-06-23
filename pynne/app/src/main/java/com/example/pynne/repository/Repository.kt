package com.example.pynne.repository

import com.example.pynne.model.Player
import io.reactivex.Observable

interface Repository {

    fun getPlayers(): Observable<MutableList<Player>>
}