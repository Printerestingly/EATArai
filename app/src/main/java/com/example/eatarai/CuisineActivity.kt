package com.example.eatarai

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.eatarai.data.CuisineData
import com.example.eatarai.databinding.FoodCuisineBinding
import com.example.eatarai.databinding.DessertTypeBinding
import kotlin.random.Random

class CuisineActivity : AppCompatActivity() {

    private var isFood: Boolean = false

    private var useRandomLimit: Boolean = false
    private var maxRandomTries = 0
    private var currentRandomCount = 0

    private var foodBinding: FoodCuisineBinding? = null
    private var dessertBinding: DessertTypeBinding? = null

    private lateinit var dbHelper: FoodRandomizerDbHelper
    private lateinit var randomCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve isFood from intent
        isFood = intent.getBooleanExtra("isFood", false)
        useRandomLimit = intent.getBooleanExtra("useRandomLimit", false)

        maxRandomTries = if (useRandomLimit){
            Random.nextInt(3, 5)
        } else {
            Int.MAX_VALUE
        }

        // Initialize DB Helper
        dbHelper = FoodRandomizerDbHelper(this)

        // Inflate the correct layout using ViewBinding
        if (isFood) {
            foodBinding = FoodCuisineBinding.inflate(layoutInflater)
            setContentView(foodBinding!!.root)
            randomCountText = foodBinding!!.randomCount
            setupRandomButton(foodBinding!!)
        } else {
            dessertBinding = DessertTypeBinding.inflate(layoutInflater)
            setContentView(dessertBinding!!.root)
            randomCountText = dessertBinding!!.randomCount
            setupRandomButton(dessertBinding!!)
        }
    }

    private fun setupRandomButton(binding: ViewBinding) {
        val randomButton = when (binding) {
            is FoodCuisineBinding -> binding.randomBtn
            is DessertTypeBinding -> binding.randomBtn
            else -> return
        }

        updateRandomCountText()

        randomButton.setOnClickListener {
            handleRandomClick()
        }
    }

    fun handleRandomClick() {
        currentRandomCount++

        // Get a random cuisine to pass to the dialog
        val randomCuisineName = dbHelper.getRandomCuisine(isFood) ?: "Unknown Cuisine"

        Log.d("CuisineActivity","Cuisine : $randomCuisineName")

        val cuisineData = CuisineData(name = randomCuisineName, isFood = isFood)

        updateRandomCountText()
        showRandomResultDialog(cuisineData)
    }

    private fun updateRandomCountText() {

        if (useRandomLimit){
            randomCountText.visibility = View.VISIBLE
        } else {
            randomCountText.visibility = View.INVISIBLE
        }

        val triesLeft = maxRandomTries - currentRandomCount
        randomCountText.text = "Random count: $triesLeft"
    }
    private fun showRandomResultDialog(cuisineData: CuisineData) {
        val dialog = RandomResultDialog(
            isFood, isCuisine = true,
            maxRandomTries, currentRandomCount
        )
        val bundle = Bundle().apply {
            putParcelable("cuisineData", cuisineData)
        }
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "RandomCuisineDialog")
    }

    fun navigateToMenu(cuisineData: CuisineData) {
        val intent = Intent(this, MenuActivity::class.java).apply {
            putExtra("selected_cuisine", cuisineData.name)
            putExtra("isFood", isFood)
            putExtra("useRandomLimit", useRandomLimit)
        }
        startActivity(intent)
    }

    fun navigateToMenuWithDelay(cuisineData: CuisineData) {
        val toast = Toast.makeText(this, "Random limit reached! Navigating to Menu...", Toast.LENGTH_SHORT)
        toast.setGravity(android.view.Gravity.TOP or android.view.Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MenuActivity::class.java).apply {
                putExtra("isFood", isFood)
                putExtra("useRandomLimit", useRandomLimit)
                putExtra("selected_cuisine", cuisineData.name)
            }
            startActivity(intent)
        }, 2000) //delay 2sec
    }

    override fun onDestroy() {
        super.onDestroy()
        foodBinding = null
        dessertBinding = null
    }
}
