package com.example.eatarai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.eatarai.databinding.FoodChooseOptionBinding
import com.example.eatarai.databinding.DessertChooseOptionBinding

class ChooseOption : AppCompatActivity() {

    private var isFood: Boolean = false
    private var useRandomLimit: Boolean = false
    private var foodBinding: FoodChooseOptionBinding? = null
    private var dessertBinding: DessertChooseOptionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFood = intent.getBooleanExtra("isFood", false)

        if (isFood) {
            foodBinding = FoodChooseOptionBinding.inflate(layoutInflater)
            setContentView(foodBinding!!.root)
            setupOption(foodBinding!!)
        } else {
            dessertBinding = DessertChooseOptionBinding.inflate(layoutInflater)
            setContentView(dessertBinding!!.root)
            setupOption(dessertBinding!!)
        }
    }

    private fun setupOption(binding: ViewBinding) {
        when (binding) {
            is FoodChooseOptionBinding -> {
                binding.chooseCuisineBtn.setOnClickListener {
                    openNextPage(ChooseCuisineActivity::class.java)
                }
                binding.randomNowBtn.setOnClickListener {
                    openNextPage(CuisineActivity::class.java)
                }
                binding.backBtn.setOnClickListener {
                    onBackPressed()
                }
                binding.checkRandomLimit.setOnCheckedChangeListener { _, isChecked ->
                    useRandomLimit = isChecked
                }
                binding.historyBtn.setOnClickListener {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
            }

            is DessertChooseOptionBinding -> {
                binding.chooseCuisineBtn.setOnClickListener {
                    openNextPage(ChooseCuisineActivity::class.java)
                }
                binding.randomNowBtn.setOnClickListener {
                    openNextPage(CuisineActivity::class.java)
                }
                binding.backBtn.setOnClickListener {
                    onBackPressed()
                }
                binding.checkRandomLimit.setOnCheckedChangeListener { _, isChecked ->
                    useRandomLimit = isChecked
                }
                binding.historyBtn.setOnClickListener {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun openNextPage(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.putExtra("isFood", isFood)
        intent.putExtra("useRandomLimit", useRandomLimit)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Avoid memory leaks by nullifying the bindings
        foodBinding = null
        dessertBinding = null
    }
}