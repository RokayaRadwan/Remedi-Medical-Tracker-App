package com.example.remedi.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remedi.data.local.entity.DoseHistoryEntity
import com.example.remedi.databinding.ItemDoseHistoryBinding

class DoseHistoryAdapter(
    private var historyList: List<DoseHistoryEntity>
) : RecyclerView.Adapter<DoseHistoryAdapter.DoseHistoryViewHolder>() {

    class DoseHistoryViewHolder(
        private val binding: ItemDoseHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(history: DoseHistoryEntity) {
            binding.tvMedicineName.text = history.medicineName
            binding.tvDoseDetails.text = history.doseDetails
            binding.tvDoseDateTime.text = history.dateTime
            binding.tvDoseStatus.text = history.status

            if (history.status == "Taken") {
                binding.tvDoseStatus.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_green_dark)
                )
            } else {
                binding.tvDoseStatus.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_red_dark)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoseHistoryViewHolder {
        val binding = ItemDoseHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DoseHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoseHistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun updateList(newList: List<DoseHistoryEntity>) {
        historyList = newList
        notifyDataSetChanged()
    }
}