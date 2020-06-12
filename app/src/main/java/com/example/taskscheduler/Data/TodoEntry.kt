package com.example.taskscheduler.Data

import android.net.Uri
import android.provider.BaseColumns


const val CONTENT_AUTHORITY = "com.example.taskscheduler.taskprovider"

//const val PATH_TASKS = "Todos"
const val PATH_TASKS = "Tasks"
val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")

object TodoEntry : BaseColumns{
    val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASKS)

    // Table Name
    //const val TABLE_NAME = "Todos"
    const val TABLE_NAME = "Tasks"

    // Fields
    const val ID = BaseColumns._ID
    const val COLUMN_TEXT = "Text"
    const val COLUMN_DESCRIPTION = "Description"
    const val DATE_CREATED = "Created"
    const val COLUMN_DONE = "Done"
}