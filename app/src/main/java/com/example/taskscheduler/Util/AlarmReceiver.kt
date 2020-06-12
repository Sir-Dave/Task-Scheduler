package com.example.taskscheduler.Util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.taskscheduler.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val notificationManager =
        ContextCompat.getSystemService(context,  NotificationManager::class.java)

        notificationManager!!.sendNotification(context.getText(R.string.timer_message).toString(), context)

    }

}