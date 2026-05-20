package com.example.remedi.ui.medication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remedi.databinding.ItemVitaminBinding

class VitaminAdapter(private val vitamins: List<Vitamin>) :
    RecyclerView.Adapter<VitaminAdapter.VitaminViewHolder>() {

    class VitaminViewHolder(val binding: ItemVitaminBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VitaminViewHolder {
        val binding = ItemVitaminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VitaminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VitaminViewHolder, position: Int) {
        val vitamin = vitamins[position]
        holder.binding.apply {
            tvVitaminName.text = vitamin.name
            tvVitaminDosage.text = vitamin.dosage
            ivVitaminIcon.setImageResource(vitamin.icon)
        }
    }

    override fun getItemCount() = vitamins.size
}