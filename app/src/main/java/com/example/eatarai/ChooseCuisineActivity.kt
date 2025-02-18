package com.example.eatarai

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.eatarai.data.CuisineData
import com.example.eatarai.databinding.DessertChooseCuisineBinding
import com.example.eatarai.databinding.FoodChooseCuisineBinding

class ChooseCuisineActivity : AppCompatActivity() {

    private var foodBinding: FoodChooseCuisineBinding? = null
    private var dessertBinding: DessertChooseCuisineBinding? = null
    private var selectedCuisine: String? = null  // Store the selected cuisine as a single string
    private var isFood: Boolean = true
    private var isOthersSelected: Boolean = false  // Track if "Others" is selected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve isFood from intent
        isFood = intent.getBooleanExtra("isFood", true)

        // Inflate the correct layout
        val binding = inflateBinding(isFood)
        setContentView(binding.root)

        // Set up the cuisine buttons with listeners
        setupCuisineButtons(binding)

        // Set up actions for "Next" and "Cancel" buttons
        when (binding) {
            is FoodChooseCuisineBinding -> {
                binding.nextBtn.setOnClickListener {
                    if (selectedCuisine == null) {
                        // Show an alert if no cuisine is selected
                        showAlert("Please select a cuisine")
                    } else {
                        openMenuActivity(selectedCuisine!!, isOthersSelected)
                    }
                }
                binding.cancelBtn.setOnClickListener {
                    finish()
                }
            }
            is DessertChooseCuisineBinding -> {
                binding.nextBtn.setOnClickListener {
                    if (selectedCuisine == null) {
                        // Show an alert if no cuisine is selected
                        showAlert("Please select a cuisine")
                    } else {
                        openMenuActivity(selectedCuisine!!, isOthersSelected)
                    }
                }
                binding.cancelBtn.setOnClickListener {
                    finish()
                }
            }
        }
    }

    private fun inflateBinding(isFood: Boolean): ViewBinding {
        return if (isFood) {
            foodBinding = FoodChooseCuisineBinding.inflate(layoutInflater)
            foodBinding!!
        } else {
            dessertBinding = DessertChooseCuisineBinding.inflate(layoutInflater)
            dessertBinding!!
        }
    }

    private fun setupCuisineButtons(binding: ViewBinding) {
        val buttons = when (binding) {
            is FoodChooseCuisineBinding -> listOf(
                binding.chooseCuisine1 to "Thai Food",
                binding.chooseCuisine2 to "Japanese Food",
                binding.chooseCuisine3 to "Korean",
                binding.chooseCuisine4 to "Italian",
                binding.chooseCuisine5 to "Chinese",
                binding.chooseCuisine6 to "Others"
            )
            is DessertChooseCuisineBinding -> listOf(
                binding.chooseCuisine1 to "Cake",
                binding.chooseCuisine2 to "Ice Cream",
                binding.chooseCuisine3 to "Pudding",
                binding.chooseCuisine4 to "Pastry",
                binding.chooseCuisine5 to "Cookies",
                binding.chooseCuisine6 to "Others"
            )
            else -> emptyList()
        }

        buttons.forEach { (button, cuisine) ->
            button.setOnClickListener {
                if (selectedCuisine == cuisine) {
                    // Deselect if the same button is clicked again
                    selectedCuisine = null
                    isOthersSelected = false
                    updateButtonSelection(buttons, null)
                } else if (selectedCuisine == null) {
                    // Select the cuisine if none is currently selected
                    selectedCuisine = cuisine
                    isOthersSelected = cuisine == "Others"
                    updateButtonSelection(buttons, cuisine)
                } else {
                    // Show alert if trying to select a second cuisine
                    showAlert("You can only select one cuisine.")
                }
            }
        }
    }

    private fun updateButtonSelection(buttons: List<Pair<Button, String>>, selectedCuisine: String?) {
        buttons.forEach { (button, cuisine) ->
            val background = if (isFood) {
                if (cuisine == selectedCuisine) {
                    ContextCompat.getDrawable(this, R.drawable.food_pressed_choose_cuisine)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.food_default_choose_cuisine)
                }
            } else {
                if (cuisine == selectedCuisine) {
                    ContextCompat.getDrawable(this, R.drawable.dessert_pressed_choose_cuisine)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.dessert_default_choose_cuisine)
                }
            }
            button.background = background
        }
    }

    private fun openMenuActivity(selectedCuisine: String, includeOthers: Boolean) {
        val intent = Intent(this, MenuActivity::class.java).apply {
            putExtra("selected_cuisine", selectedCuisine)
            putExtra("isFood", isFood)
            putExtra("includeOthers", includeOthers)  // Pass "Others" flag to the next activity
        }
        startActivity(intent)
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        foodBinding = null
        dessertBinding = null
    }
}
