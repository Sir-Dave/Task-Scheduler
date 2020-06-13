package com.example.taskscheduler.Util

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.taskscheduler.R
import com.example.taskscheduler.TaskDetails
import com.example.taskscheduler.TaskListActivity

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, context: Context) {

    val contentIntent = Intent(context, TaskListActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context, NOTIFICATION_ID,
        contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )


    val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_tasks_id))
        .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
        .setContentText(messageBody)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelAllNotifications(){
    cancelAll()
}