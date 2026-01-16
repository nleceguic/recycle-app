package com.example.recycle.ui.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recycle.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}