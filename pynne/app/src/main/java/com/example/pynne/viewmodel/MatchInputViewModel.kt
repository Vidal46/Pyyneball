package com.example.pynne.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.component.Input
import javax.inject.Inject

class MatchInputViewModel @Inject constructor() : ViewModel() {

    val firstPlayerName = MutableLiveData<String>()
    val firstPlayerScore = MutableLiveData<String>()

    val secondPlayerName = MutableLiveData<String>()
    val secondPlayerScore = MutableLiveData<String>()

    fun validateForm(
        firstPlayerName: Input,
        firstPlayerScore: Input,
        secondPlayerName: Input,
        secondPlayerScore: Input
    ) =
        firstPlayerName.getText().isNotEmpty() &&
                secondPlayerName.getText().isNotEmpty() &&
                firstPlayerScore.getText().isNotEmpty() &&
                secondPlayerScore.getText().isNotEmpty()

}