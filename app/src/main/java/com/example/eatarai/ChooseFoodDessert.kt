package com.example.eatarai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eatarai.databinding.FoodOrDessertBinding // Import ViewBinding

class ChooseFoodDessert : AppCompatActivity() {

    private lateinit var binding: FoodOrDessertBinding
    private var isFood = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = FoodOrDessertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for "Food" option
        binding.rightFood.setOnClickListener {
            isFood = true
            navigateToNext()
        }

        // Set click listener for "Dessert" option
        binding.leftDessert.setOnClickListener {
            isFood = false
            navigateToNext()
        }
    }

    private fun navigateToNext() {
        val intent = Intent(this, ChooseOption::class.java)
        intent.putExtra("isFood", isFood)
        startActivity(intent)
    }
}
