package com.example.pynne.di

import android.content.Context
import com.example.pynne.repository.RankingRepository
import com.example.pynne.repository.Repository
//import com.example.pynne.repository.RankingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun provideRankingRepository(repository: RankingRepository): Repository

}