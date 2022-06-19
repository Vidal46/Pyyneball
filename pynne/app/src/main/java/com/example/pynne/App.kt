package com.example.pynne

import android.app.Application
import com.example.pynne.di.AppComponent
import com.example.pynne.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App: Application(), HasAndroidInjector {

    lateinit var applicationComponent: AppComponent

    @Inject
    lateinit var mInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerAppComponent.factory().create(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = mInjector
}