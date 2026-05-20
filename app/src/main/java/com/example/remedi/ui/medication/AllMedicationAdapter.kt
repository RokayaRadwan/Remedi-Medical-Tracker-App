package com.example.remedi.ui.medication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remedi.data.local.entity.MedicationEntity
import com.example.remedi.databinding.ItemMedicationBinding

class AllMedicationAdapter(
    private var medications: List<MedicationEntity>,
    private val onEditClick: (MedicationEntity) -> Unit
) : RecyclerView.Adapter<AllMedicationAdapter.MedicationViewHolder>() {

    class MedicationViewHolder(
        val binding: ItemMedicationBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val binding = ItemMedicationBinding.inflate(
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
            tvDosage.text =
                "Dosage: ${medication.dosage}\n" +
                        "Time: ${medication.time}\n" +
                        "Instructions: ${medication.instruction}\n" +
                        "Refills: ${medication.refillsLeft} left\n" +
                        "Days: ${medication.daysOfWeek}"

            btnEdit.setOnClickListener {
                onEditClick(medication)
            }
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