package com.example.pynne.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pynne.model.Player
import com.example.pynne.model.Player.Companion.generatePlayer
import com.example.pynne.repository.RankingRepository
import io.reactivex.Observable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: RankingRepository) : ViewModel() {

    private val _firstPlayer = MutableLiveData<Player>()
    val firstPlayer: LiveData<Player>
        get() = _firstPlayer

    private val _secondPlayer = MutableLiveData<Player>()
    val secondPlayer: LiveData<Player>
        get() = _secondPlayer

    private val _players = MutableLiveData<MutableList<Player>>()
    val players: LiveData<MutableList<Player>>
        get() {
            return _players
        }

    init {
        _firstPlayer.value = generatePlayer()
        _secondPlayer.value = generatePlayer()

        repository.getPlayers()
            .subscribe {
                _players.value = cleanUpList(it, FROM_REQUEST)
            }
    }

    private fun cleanUpList(list: MutableList<Player>?, action: Int): MutableList<Player> {
        val map = HashMap<String?, Int?>()
        val names = HashMap<String?, Int?>()
        val pureList = arrayListOf<Player>()
        val cleanedList = mutableListOf<Player>()

        list?.forEach { player ->
            if (map.containsKey(player.name)) {
                names[player.name] = names[player.name]?.plus(1)
                map[player.name] = (map[player.name]?.plus(player.wins ?: 0) ?: 0)
            } else {
                names[player.name] = 1
                map[player.name] = player.wins
            }
        }

        list?.forEach { player ->
            names.toList().forEach {
                if (player.name == it.first) pureList.add(Player(name = it.first, wins = it.second))
            }
        }

        map.toList().map {
            cleanedList.add(Player(name = it.first, wins = it.second))
        }

        return if (action == FROM_REQUEST)
            pureList.distinctBy { it.name }.toMutableList()
        else
            cleanedList.distinctBy { it.name }.toMutableList()
    }

    fun handleScore() {

        val winner =
            when {
                firstPlayer.value?.score ?: 0 > secondPlayer.value?.score ?: 0 -> {
                    Player(firstPlayer.value?.name, firstPlayer.value?.score, +1)
                }

                else -> Player(secondPlayer.value?.name, secondPlayer.value?.score, +1)
            }

        val updatedList = _players.value
        updatedList?.add(winner)

        _players.value = cleanUpList(updatedList, FROM_UI)
    }

    companion object {
        const val FROM_REQUEST = 0
        const val FROM_UI = 1
    }
}