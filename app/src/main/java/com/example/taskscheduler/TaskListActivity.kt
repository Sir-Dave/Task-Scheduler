package com.example.taskscheduler

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.taskscheduler.Data.DatabaseHelper
import com.example.taskscheduler.Data.TodoEntry
import kotlinx.android.synthetic.main.activity_main.*


const val ALL_RECORDS = -1

class TaskListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var listView: ListView
    private lateinit var adapter: TaskCursorAdapter

    private var cursor: Cursor? = null
    private val URL_LOADER = 0
    private val TAG = "TaskListActivity"

    fun readData(): Cursor? {
        val projection = arrayOf(
            TodoEntry.COLUMN_TEXT,
            TodoEntry.TABLE_NAME + "." + TodoEntry.ID,
            TodoEntry.COLUMN_DESCRIPTION,
            TodoEntry.HOUR_CREATED,
            TodoEntry.MIN_CREATED,
            TodoEntry.COLUMN_DONE)
        cursor = contentResolver.query(TodoEntry.CONTENT_URI, projection, null, null, null)
        return cursor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportLoaderManager.initLoader(URL_LOADER, null, this)
        readData()

        adapter = TaskCursorAdapter(this, cursor!!, false)
        listView = findViewById(R.id.list_view)
        listView.adapter = adapter

        listView.setOnItemClickListener{ adapterView, view, pos, id ->
            cursor = adapterView.getItemAtPosition(pos) as Cursor

            val taskId = cursor!!.getInt(cursor!!.getColumnIndex(TodoEntry.ID))
            val taskText = cursor!!.getString(cursor!!.getColumnIndex(TodoEntry.COLUMN_TEXT))
            val taskDescription = cursor!!.getString(cursor!!.getColumnIndex(TodoEntry.COLUMN_DESCRIPTION))
            val taskHour = cursor!!.getInt(cursor!!.getColumnIndex(TodoEntry.HOUR_CREATED))
            val taskMin = cursor!!.getInt(cursor!!.getColumnIndex(TodoEntry.MIN_CREATED))
            val taskDone = cursor!!.getInt(cursor!!.getColumnIndex(TodoEntry.COLUMN_DONE))

            val boolDone = (taskDone == 1)

            val tasks = Tasks(taskId, taskText, taskDescription, taskHour, taskMin ,boolDone)

            val intent = Intent(this, TaskDetails::class.java)
            intent.putExtra("Tasks", tasks)
            startActivity(intent)
        }

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val tasks = Tasks(0, "", "", 0, 0 ,false)
            val intent = Intent(this, TaskDetails::class.java)
            intent.putExtra("Tasks", tasks)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             //R.id.action_settings -> true
             R.id.action_delete_all_tasks -> deleteTask(ALL_RECORDS)
             else -> super.onOptionsItemSelected(item)
         }
        return true
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
            TodoEntry.COLUMN_TEXT,
            TodoEntry.TABLE_NAME + "." + TodoEntry.ID,
            TodoEntry.COLUMN_DESCRIPTION,
            TodoEntry.HOUR_CREATED,
            TodoEntry.MIN_CREATED,
            TodoEntry.COLUMN_DONE)
        return CursorLoader(this, TodoEntry.CONTENT_URI, projection, null, null, null)
    }


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        //adapter.swapCursor(data)
        adapter.changeCursor(readData())
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.swapCursor(null)
    }

    private fun deleteTask(id : Int){
        var args: Array<String>?  = arrayOf(id.toString())
        if (id == ALL_RECORDS){
            args = null
        }
        val handler = TaskQueryHandler(this.contentResolver)
        handler.startDelete(1, null, TodoEntry.CONTENT_URI, "${TodoEntry.ID} = ?", args)

    }
}