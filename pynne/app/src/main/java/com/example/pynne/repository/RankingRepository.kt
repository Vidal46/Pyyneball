package com.example.pynne.repository

import com.example.pynne.model.Player
import io.reactivex.Observable
import javax.inject.Inject

class RankingRepository @Inject constructor() : Repository {

    private val list: Observable<MutableList<Player>> = Observable.just(
        mutableListOf(
            Player("Antonio", "64", "7"),
            Player("Joao", "34", "5"),
            Player("Priscilla", "23", "4"),
            Player("Julio", "12", "3"),
            Player("Edenilson", "14", "2"),
            Player("Bibiana", "16", "6")
        )
    )

    override fun getPlayers(): Observable<MutableList<Player>> = list

}