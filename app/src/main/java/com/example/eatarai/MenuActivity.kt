package com.example.eatarai

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.eatarai.data.MenuData
import com.example.eatarai.databinding.DessertMenuBinding
import com.example.eatarai.databinding.FoodMenuBinding
import kotlin.random.Random

class MenuActivity : AppCompatActivity() {

    private var isFood: Boolean = false
    private var useRandomLimit: Boolean = false
    private var includeOthers: Boolean = false  // Updated to correct the typo
    private var maxRandomTries = 0
    private var currentRandomCount = 0

    private var foodBinding: FoodMenuBinding? = null
    private var dessertBinding: DessertMenuBinding? = null

    private lateinit var dbHelper: FoodRandomizerDbHelper
    private lateinit var randomCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve isFood, useRandomLimit, and selected cuisine from intent
        isFood = intent.getBooleanExtra("isFood", false)
        includeOthers = intent.getBooleanExtra("includeOthers", false)
        useRandomLimit = intent.getBooleanExtra("useRandomLimit", false)

        // Retrieve the single selected cuisine name as a string
        val selectedCuisine = intent.getStringExtra("selected_cuisine") ?: "No Cuisine Found"

        maxRandomTries = if (useRandomLimit) {
            Random.nextInt(10, 20)
        } else {
            Int.MAX_VALUE
        }

        // Initialize DB Helper
        dbHelper = FoodRandomizerDbHelper(this)

        // Inflate the correct layout using ViewBinding
        if (isFood) {
            foodBinding = FoodMenuBinding.inflate(layoutInflater)
            setContentView(foodBinding!!.root)
            randomCountText = foodBinding!!.randomCount

            // Set cuisine name to the appropriate TextView
            foodBinding!!.menuTextCuisine.text = selectedCuisine

            setupRandomButton(foodBinding!!)
        } else {
            dessertBinding = DessertMenuBinding.inflate(layoutInflater)
            setContentView(dessertBinding!!.root)
            randomCountText = dessertBinding!!.randomCount

            // Set cuisine name to the appropriate TextView
            dessertBinding!!.menuTextCuisine.text = selectedCuisine

            setupRandomButton(dessertBinding!!)
        }
    }

    private fun setupRandomButton(binding: ViewBinding) {
        val randomButton = when (binding) {
            is FoodMenuBinding -> binding.randomBtn
            is DessertMenuBinding -> binding.randomBtn
            else -> return
        }

        updateRandomCountText()

        randomButton.setOnClickListener {
            handleRandomClick()
        }
    }

    fun handleRandomClick() {
        currentRandomCount++

        val selectedCuisine = intent.getStringExtra("selected_cuisine") ?: ""
        Log.d("MenuActivity", "Selected Cuisine: $selectedCuisine")

        // Fetch random menu data using the selected cuisine and includeOthers flag
        val menuResult: Pair<String?, String?> = dbHelper.getRandomMenu(
            cuisines = listOf(selectedCuisine),
            isFood = isFood,
            includeOthers = includeOthers
        ) ?: ("Unknown Menu" to "Unknown Cuisine")

        val randomMenuName = menuResult.first ?: "Unknown Menu"
        val actualCuisineName = menuResult.second ?: "Unknown Cuisine"

        Log.d("MenuActivity", "Random Menu Name: $randomMenuName, Actual Cuisine: $actualCuisineName, includeOthers: $includeOthers")

        val menuData = MenuData(name = randomMenuName, cuisineName = actualCuisineName)

        updateRandomCountText()
        showRandomResultDialog(menuData)
    }

    private fun updateRandomCountText() {
        if (useRandomLimit) {
            randomCountText.visibility = View.VISIBLE
        } else {
            randomCountText.visibility = View.INVISIBLE
        }

        val triesLeft = maxRandomTries - currentRandomCount
        randomCountText.text = "Random count: $triesLeft"
    }

    private fun showRandomResultDialog(menuData: MenuData) {
        val dialog = RandomResultDialog(
            isFood, isCuisine = false,
            maxRandomTries, currentRandomCount
        )
        val bundle = Bundle().apply {
            putParcelable("menu", menuData)
        }
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "RandomMenuDialog")
    }

    fun navigateToSummary(menuName: String, cuisineName: String) {
        Toast.makeText(this, "Navigating to Summary...", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MenuSummaryActivity::class.java).apply {
            putExtra("isFood", isFood)
            putExtra("menuName", menuName)
            putExtra("cuisineName", cuisineName)  // Use actual cuisine name here
        }
        startActivity(intent)
    }

    fun navigateToSummaryWithDelay(menuData: MenuData) {
        val toast = Toast.makeText(this, "Random limit reached! Navigating to Summary...", Toast.LENGTH_SHORT)
        toast.setGravity(android.view.Gravity.TOP or android.view.Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MenuSummaryActivity::class.java).apply {
                putExtra("isFood", isFood)
                putExtra("menuName", menuData.name)
                putExtra("cuisineName", menuData.cuisineName)  // Use actual cuisine name here
            }
            startActivity(intent)
        }, 2000) // Delay 2 seconds
    }

    override fun onDestroy() {
        super.onDestroy()
        foodBinding = null
        dessertBinding = null
    }
}
