package com.example.eatarai

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eatarai.databinding.ItemHistoryBinding
import com.example.eatarai.data.HistoryRecord
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private var historyList: MutableList<HistoryRecord>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyRecord = historyList[position]

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = try {
            // Try to parse the timestamp; use safe call to handle nullability
            val timestampAsDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(historyRecord.timestamp)
            timestampAsDate?.let { dateFormat.format(it) } ?: "Invalid Date" // Format if not null, otherwise "Invalid Date"
        } catch (e: Exception) {
            "Invalid Date" // Fallback in case of exception
        }

        holder.binding.apply {
            timestampTextView.text = formattedDate
            cuisineTextView.text = historyRecord.cuisine
            menuTextView.text = historyRecord.menu

            deleteMenuBtn.setOnClickListener {
                // Show confirmation dialog before deletion
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        val dbHelper = FoodRandomizerDbHelper(holder.itemView.context)
                        dbHelper.deleteHistoryRecord(historyRecord.menu)  // Delete from database
                        (historyList as MutableList).removeAt(position)  // Remove from the list in adapter
                        notifyItemRemoved(position)  // Notify adapter of item removal
                    }
                    .setNegativeButton("No", null)
                    .show()
            }

        }
    }

    fun updateHistoryList(newHistoryList: MutableList<HistoryRecord>) {
        this.historyList = newHistoryList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}


