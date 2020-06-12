package com.example.taskscheduler

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.taskscheduler.Data.TodoEntry
import com.example.taskscheduler.Util.AlarmReceiver
import com.example.taskscheduler.Util.sendNotification
import com.example.taskscheduler.databinding.ActivityTaskDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskDetails : AppCompatActivity() {
    private lateinit var tasks : Tasks
    private lateinit var timePicker: TimePicker
    private val TAG = "TaskDetails"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)


        val intent = intent
        tasks = intent.getSerializableExtra("Tasks") as Tasks
        val binding: ActivityTaskDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_task_details)

        binding.tasks = tasks
        timePicker = findViewById(R.id.time_picker)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_task){

            val handler = TaskQueryHandler(contentResolver)
            val alertDialogBuilder =  AlertDialog.Builder(this)
            alertDialogBuilder.setMessage(getString(R.string.message_delete_task))
            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)
            alertDialogBuilder.setTitle(getString(R.string.delete_task_dialog_title))
            alertDialogBuilder.setPositiveButton("Ok", object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, button: Int) {
                    val uri = Uri.withAppendedPath(TodoEntry.CONTENT_URI, tasks.id.get().toString())
                    val selection = "${TodoEntry.ID} = ?"
                    val args = arrayOfNulls<String>(1)
                    args[0] = tasks.id.get().toString()
                    handler.startDelete(1, null, uri, selection, args)
                    val intent = Intent(this@TaskDetails, TaskListActivity::class.java)
                    startActivity(intent)
                }
            })

            alertDialogBuilder.setNegativeButton(android.R.string.no, null)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        return true


    }

    override fun onPause() {
        super.onPause()
        createTask()
    }

    private fun createTask(){
        val args  = arrayOfNulls<String>(1)
        val handler = TaskQueryHandler(contentResolver)
        val values = ContentValues()
        values.put(TodoEntry.COLUMN_TEXT, tasks.text.get())
        values.put(TodoEntry.COLUMN_DESCRIPTION, tasks.description.get())
        values.put(TodoEntry.DATE_CREATED, tasks.created.get())
        values.put(TodoEntry.COLUMN_DONE, tasks.done.get())


        if (tasks.id.get() != 0){
            args[0] = tasks.id.get().toString()
            handler.startUpdate(1, null, TodoEntry.CONTENT_URI, values, "${TodoEntry.ID} = ?", args)
            notificationChecked()
        }

        else if (tasks.id.get() == 0 && tasks.text.get() != ""){
            handler.startInsert(1, null, TodoEntry.CONTENT_URI, values)
            notificationChecked()
        }

    }

    fun createNotification(){

        Toast.makeText(this, "Sucessfully created notification", Toast.LENGTH_SHORT).show()
        //Create Alarm before creating notification

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // TODO() Use the time from timePicker to set the notification
        val time = System.currentTimeMillis()
        val tenSeconds= 1000 * 10

        alarmManager.set(AlarmManager.RTC_WAKEUP, time + tenSeconds, pendingIntent)
    }

    fun notificationChecked(){
        if (tasks.done.get()){
            createNotification()
            checkTime()
        }
    }

    fun checkTime(){
        Log.d(TAG, "Hour is ${timePicker.hour} and minute is ${timePicker.minute}")
    }
}