package com.example.remedi.ui.reminderschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remedi.data.local.entity.ReminderEntity
import com.example.remedi.databinding.ItemReminderScheduleBinding

class ReminderScheduleAdapter(
    private var reminders: List<ReminderEntity>
) : RecyclerView.Adapter<ReminderScheduleAdapter.ReminderViewHolder>() {

    inner class ReminderViewHolder(
        private val binding: ItemReminderScheduleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reminder: ReminderEntity) {
            binding.tvReminderMedicineName.text = reminder.medicineName
            binding.tvReminderDose.text = reminder.doseAmount
            binding.tvReminderTimeRepeat.text = "${reminder.repeatType}, ${reminder.time}"
            binding.tvReminderStatus.text = reminder.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminders[position])
    }

    override fun getItemCount(): Int {
        return reminders.size
    }

    fun updateList(newReminders: List<ReminderEntity>) {
        reminders = newReminders
        notifyDataSetChanged()
    }
}