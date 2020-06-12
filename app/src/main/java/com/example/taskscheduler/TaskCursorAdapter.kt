package com.example.taskscheduler

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.taskscheduler.Data.TodoEntry

class TaskCursorAdapter (context: Context, c: Cursor, autoRequery: Boolean): CursorAdapter(context, c, autoRequery){


    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val taskText: TextView = view!!.findViewById(R.id.txt_item_name)
        val textColumn = cursor!!.getColumnIndex(TodoEntry.COLUMN_TEXT)
        val text = cursor.getString(textColumn)
        taskText.text = text
    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false)
    }
}