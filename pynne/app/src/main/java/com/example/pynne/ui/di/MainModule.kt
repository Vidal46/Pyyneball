package com.example.pynne.ui.di

import androidx.lifecycle.ViewModel
import com.example.pynne.di.ViewModelKey
import com.example.pynne.viewmodel.MainViewModel
import com.example.pynne.viewmodel.MatchInputViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchInputViewModel::class)
    fun bindRankingViewModel(viewModel: MatchInputViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMatchViewModel(viewModel: MainViewModel): ViewModel
}