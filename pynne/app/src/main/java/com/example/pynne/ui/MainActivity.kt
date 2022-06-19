package com.example.pynne.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pynne.App
import com.example.pynne.databinding.ActivityMainBinding
import com.example.pynne.di.AppComponent
import com.example.pynne.ui.di.MainComponent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (applicationContext as App).applicationComponent.mainComponent()
            .create()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
    }
}