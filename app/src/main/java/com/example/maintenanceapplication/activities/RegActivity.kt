package com.example.maintenanceapplication.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maintenanceapplication.R
import com.example.maintenanceapplication.databinding.ActivityRegBinding

class RegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}