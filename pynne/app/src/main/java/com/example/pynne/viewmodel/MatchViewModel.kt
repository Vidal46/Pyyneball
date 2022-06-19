package com.example.pynne.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MatchViewModel @Inject constructor() : ViewModel() {

    val firstPlayerScore = MutableLiveData<String>()

    val secondPlayerScore = MutableLiveData<String>()
}