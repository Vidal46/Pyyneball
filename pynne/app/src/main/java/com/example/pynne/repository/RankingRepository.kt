package com.example.pynne.repository

import com.example.pynne.model.Match
import com.example.pynne.model.Player
import io.reactivex.Observable
import javax.inject.Inject

class RankingRepository @Inject constructor() : Repository {

    private val list: Observable<MutableList<Player>> = Observable.just(
        mutableListOf(
            Match(Player("Amos", 4), Player("Diego", 5)).getWinner(),
            Match(Player("Amos", 1), Player("Diego", 5)).getWinner(),
            Match(Player("Amos", 2), Player("Diego", 5)).getWinner(),
            Match(Player("Amos", 0), Player("Diego", 5)).getWinner(),
            Match(Player("Amos", 6), Player("Diego", 5)).getWinner(),
            Match(Player("Amos", 5), Player("Diego", 2)).getWinner(),
            Match(Player("Amos", 4), Player("Diego", 0)).getWinner(),
            Match(Player("Joel", 4), Player("Diego", 5)).getWinner(),
            Match(Player("Tim", 4), Player("Amos", 5)).getWinner(),
            Match(Player("Tim", 5), Player("Amos", 2)).getWinner(),
            Match(Player("Amos", 3), Player("Tim", 5)).getWinner(),
            Match(Player("Amos", 5), Player("Tim", 3)).getWinner(),
            Match(Player("Amos", 5), Player("Joel", 4)).getWinner(),
            Match(Player("Joel", 5), Player("Tim", 2)).getWinner(),
        )
    )

    override fun getPlayers(): Observable<MutableList<Player>> = list

}