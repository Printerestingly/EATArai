package com.example.eatarai

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.example.eatarai.data.CuisineData
import com.example.eatarai.data.MenuData
import com.example.eatarai.databinding.FoodDialogConfirmBinding
import com.example.eatarai.databinding.DessertDialogConfirmBinding

class RandomResultDialog(
    private val isFood: Boolean,
    private val isCuisine: Boolean,
    private val maxRandomTries: Int,
    private var currentRandomCount: Int
) : DialogFragment() {

    private var foodBinding: FoodDialogConfirmBinding? = null
    private var dessertBinding: DessertDialogConfirmBinding? = null
    private var latestCuisineData: CuisineData? = null
    private var latestMenuData: MenuData? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.CustomDialogTheme)

        val binding = if (isFood) {
            foodBinding = FoodDialogConfirmBinding.inflate(layoutInflater)
            foodBinding!!
        } else {
            dessertBinding = DessertDialogConfirmBinding.inflate(layoutInflater)
            dessertBinding!!
        }

        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        setupResultText(binding)
        setupButtons(binding)

        return dialog
    }

    private fun setupResultText(binding: ViewBinding) {
        val resultTextView = when (binding) {
            is FoodDialogConfirmBinding -> binding.randomResultTxt
            is DessertDialogConfirmBinding -> binding.randomResultTxt
            else -> return
        }

        val triesLeft = maxRandomTries - currentRandomCount

        if (isCuisine) {
            val cuisineData = arguments?.getParcelable<CuisineData>("cuisineData")
            latestCuisineData = cuisineData // Save the latest cuisine data
            resultTextView.text = cuisineData?.name ?: "Unknown Cuisine"
        } else {
            val menuData = arguments?.getParcelable<MenuData>("menu")
            latestMenuData = menuData // Save the latest menu data
            resultTextView.text = menuData?.name ?: "Unknown Menu"
        }

        val triesLeftText = when (binding) {
            is FoodDialogConfirmBinding -> binding.randomCount
            is DessertDialogConfirmBinding -> binding.randomCount
            else -> return
        }

        // Show or hide the tries left text based on the random limit
        if (maxRandomTries == Int.MAX_VALUE) {
            triesLeftText.visibility = View.INVISIBLE
        } else {
            triesLeftText.visibility = View.VISIBLE
            triesLeftText.text = "Tries Left: $triesLeft"
        }
    }

    private fun setupButtons(binding: ViewBinding) {
        val yesButton = when (binding) {
            is FoodDialogConfirmBinding -> binding.btnYes
            is DessertDialogConfirmBinding -> binding.btnYes
            else -> return
        }

        val noButton = when (binding) {
            is FoodDialogConfirmBinding -> binding.btnNo
            is DessertDialogConfirmBinding -> binding.btnNo
            else -> return
        }

        yesButton.setOnClickListener { handleYesClick() }
        noButton.setOnClickListener { handleNoClick() }
    }

    private fun handleYesClick() {
        dismiss()
        if (isCuisine) {
            val cuisineData = arguments?.getParcelable<CuisineData>("cuisineData")
            cuisineData?.let {
                if (maxRandomTries - currentRandomCount <= 0) {
                    (activity as? CuisineActivity)?.navigateToMenuWithDelay(latestCuisineData ?: it)
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog?.dismiss() // Close dialog after delay
                    }, 2000)
                } else {
                    (activity as? CuisineActivity)?.handleRandomClick()
                }
            }
        } else {
            val menuData = arguments?.getParcelable<MenuData>("menu")
            menuData?.let {
                if (maxRandomTries - currentRandomCount <= 0) {
                    (activity as? MenuActivity)?.navigateToSummaryWithDelay(latestMenuData ?: it)
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog?.dismiss() // Close dialog after delay
                    }, 2000)
                } else {
                    (activity as? MenuActivity)?.handleRandomClick()
                }
            }
        }
    }

    private fun handleNoClick() {
        dismiss()

        val dbHelper = FoodRandomizerDbHelper(requireContext())

        if (isCuisine) {
            val cuisineData = arguments?.getParcelable<CuisineData>("cuisineData")
            cuisineData?.let {
                (activity as? CuisineActivity)?.navigateToMenu(it)
            }
        } else {
            val menuData = arguments?.getParcelable<MenuData>("menu")
            val menuName = menuData?.name ?: "Unknown"
            val cuisineName = menuData?.cuisineName ?: "Unknown"

            // Save menu data to history
            val category = if (isFood) "Food" else "Dessert"
            dbHelper.addHistoryRecord(cuisine = cuisineName, menu = menuName, category)

            (activity as? MenuActivity)?.navigateToSummary(menuName, cuisineName)
        }
    }
}
