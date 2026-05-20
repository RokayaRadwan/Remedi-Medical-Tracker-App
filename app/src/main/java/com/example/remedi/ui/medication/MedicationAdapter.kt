package com.example.remedi.ui.medication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remedi.data.local.entity.MedicationEntity
import com.example.remedi.databinding.ItemTodayMedicationBinding

class MedicationAdapter(
    private var medications: List<MedicationEntity>
) : RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>() {

    class MedicationViewHolder(
        val binding: ItemTodayMedicationBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val binding = ItemTodayMedicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MedicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val medication = medications[position]

        holder.binding.apply {
            tvMedicationName.text = medication.name
            tvInstruction.text = "${medication.instruction} • ${medication.daysOfWeek}"
            tvDose.text = medication.dosage
            tvTime.text = medication.time
            tvRefills.text = "${medication.refillsLeft} Left"

            root.setCardBackgroundColor(medication.color)
        }
    }

    override fun getItemCount(): Int {
        return medications.size
    }

    fun updateList(newMedications: List<MedicationEntity>) {
        medications = newMedications
        notifyDataSetChanged()
    }
}