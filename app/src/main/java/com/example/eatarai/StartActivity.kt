package com.example.eatarai

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.eatarai.databinding.StartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: StartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add a delay of 2 seconds, then navigate to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ChooseFoodDessert::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
