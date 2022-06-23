package com.example.pynne.ui.di

import com.example.pynne.ui.MainActivity
import com.example.pynne.ui.fragment.MatchInputFragment
import com.example.pynne.ui.fragment.RankingFragment
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(fragment: RankingFragment)
    fun inject(fragment: MatchInputFragment)
}