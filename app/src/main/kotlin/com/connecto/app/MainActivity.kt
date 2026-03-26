package com.connecto.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.connecto.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initial setup for Connecto Anatomy Map
    }
}
