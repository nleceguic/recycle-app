package com.example.recycle.ui.stats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recycle.databinding.ActivityStatsBinding

class StatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}