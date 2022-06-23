package com.example.pynne.di

import android.content.Context
import com.example.pynne.ui.di.MainComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelBuilderModule::class, SubcomponentsModule::class, DataModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun mainComponent() : MainComponent.Factory
}

@Module(subcomponents = [MainComponent::class])
object SubcomponentsModule