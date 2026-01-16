package com.example.recycle.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recycle.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}