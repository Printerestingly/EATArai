package com.example.eatarai

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eatarai.databinding.FoodMenuSummaryBinding
import com.example.eatarai.databinding.DessertMenuSummaryBinding

class MenuSummaryActivity : AppCompatActivity() {

    private var foodBinding: FoodMenuSummaryBinding? = null
    private var dessertBinding: DessertMenuSummaryBinding? = null
    private var isFood: Boolean = false
    private lateinit var menuName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve isFood flag and data from intent
        isFood = intent.getBooleanExtra("isFood", false)
        menuName = intent.getStringExtra("menuName") ?: "No Menu Found"
        val cuisineName = intent.getStringExtra("cuisineName") ?: "No Cuisine Found"

        // Inflate the appropriate layout based on isFood
        if (isFood) {
            foodBinding = FoodMenuSummaryBinding.inflate(layoutInflater)
            setContentView(foodBinding!!.root)
            setupFoodSummary(cuisineName, menuName)
        } else {
            dessertBinding = DessertMenuSummaryBinding.inflate(layoutInflater)
            setContentView(dessertBinding!!.root)
            setupDessertSummary(cuisineName, menuName)
        }
    }

    private fun setupFoodSummary(cuisineName: String, menuName: String) {
        foodBinding?.apply {
            menuTextCuisine.text = cuisineName
            textMenu.text = menuName
            restartBtn.setOnClickListener {
                onRestartClicked()
            }
            locationBtn.setOnClickListener {
                openGoogleMapsWithSearch(menuName)
            }
            historyBtn.setOnClickListener {
                val intent = Intent(this@MenuSummaryActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupDessertSummary(cuisineName: String, menuName: String) {
        dessertBinding?.apply {
            menuTextCuisine.text = cuisineName
            textMenu.text = menuName
            restartBtn.setOnClickListener {
                onRestartClicked()
            }
            locationBtn.setOnClickListener {
                openGoogleMapsWithSearch(menuName)
            }
            historyBtn.setOnClickListener {
                val intent = Intent(this@MenuSummaryActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // Function to open Google Maps with a search query
    private fun openGoogleMapsWithSearch(query: String) {
        val gmmIntentUri = Uri.parse("geo:13.7291,100.7800?q=${Uri.encode(query)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    // Called when restart_Btn is clicked
    private fun onRestartClicked() {
        val intent = Intent(this, ChooseFoodDessert::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Optional: Ensure the back button doesn't take the user back to this screen
    }

    override fun onDestroy() {
        super.onDestroy()
        foodBinding = null
        dessertBinding = null
    }
}