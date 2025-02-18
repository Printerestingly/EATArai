package com.example.eatarai

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eatarai.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var dbHelper: FoodRandomizerDbHelper
    private lateinit var historyAdapter: HistoryAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up ViewBinding
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database helper and RecyclerView adapter
        dbHelper = FoodRandomizerDbHelper(this)
        val historyList = dbHelper.getHistoryRecords().toMutableList()
        historyAdapter = HistoryAdapter(historyList)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.deleteBtn.setOnClickListener{
            showClearHistoryConfirmation()
        }

        // Set up RecyclerView
        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun showClearHistoryConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Clear History")
            .setMessage("Are you sure you want to delete all history records?")
            .setPositiveButton("Yes") { _, _ ->
                dbHelper.clearHistory()
                historyAdapter.updateHistoryList(mutableListOf()) // Clear the adapter data
            }
            .setNegativeButton("No", null)
            .show()
    }
}
