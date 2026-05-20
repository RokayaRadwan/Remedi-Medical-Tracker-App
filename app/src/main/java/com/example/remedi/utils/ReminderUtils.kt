package com.example.remedi.utils

import android.content.Context
import android.widget.Toast
import com.example.remedi.data.local.entity.ReminderEntity

object ReminderUtils {

    fun isReminderValid(reminder: ReminderEntity): Boolean {
        return reminder.medicineName.isNotBlank()
                && reminder.doseAmount.isNotBlank()
                && reminder.time.isNotBlank()
                && reminder.repeatType.isNotBlank()
    }

    fun showReminderSavedMessage(context: Context) {
        Toast.makeText(
            context,
            "Reminder saved successfully",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showReminderDeletedMessage(context: Context) {
        Toast.makeText(
            context,
            "Reminder deleted",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showDoseTakenMessage(context: Context) {
        Toast.makeText(
            context,
            "Dose marked as taken",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showDoseMissedMessage(context: Context) {
        Toast.makeText(
            context,
            "Dose marked as missed",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showInvalidReminderMessage(context: Context) {
        Toast.makeText(
            context,
            "Please fill all reminder fields",
            Toast.LENGTH_SHORT
        ).show()
    }
}