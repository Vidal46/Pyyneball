package com.example.pynne.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pynne.model.Player
import com.example.pynne.repository.RankingRepository
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
        get() = _players

    init {
        _firstPlayer.value = Player.generatePlayer()
        _secondPlayer.value = Player.generatePlayer()

        repository.getPlayers().subscribe {
            _players.value = it
        }
    }

    fun handleScore() {
        val winner =
            when {
                firstPlayer.value?.score?.toInt() ?: 0 > secondPlayer.value?.score?.toInt() ?: 0 -> {
                    Player(firstPlayer.value?.name, firstPlayer.value?.score, "1")
                }

                else -> Player(secondPlayer.value?.name, secondPlayer.value?.score, "1")
            }

        _players.value?.add(winner)
    }
}