package com.example.pynne.ui.di

import androidx.lifecycle.ViewModel
import com.example.pynne.di.ViewModelKey
import com.example.pynne.viewmodel.MatchViewModel
import com.example.pynne.viewmodel.RankingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(RankingViewModel::class)
    fun bindRankingViewModel(viewModel: RankingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    fun bindMatchViewModel(viewModel: MatchViewModel): ViewModel
}