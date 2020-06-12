package com.example.taskscheduler.Data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class TaskProvider : ContentProvider() {

    private lateinit var helper: DatabaseHelper
    private val TAG = "TaskProvider"

    //constants

    private val TASKS = 1
    private val TASKS_ID = 2

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(CONTENT_AUTHORITY, PATH_TASKS, TASKS)
        addURI(CONTENT_AUTHORITY, "$PATH_TASKS/#", TASKS_ID)
    }

    override fun onCreate(): Boolean {
        helper  = DatabaseHelper(context)
        return true
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val match = uriMatcher.match(uri)
        when (match){
            TASKS->
                return insertRecord(uri, contentValues, TodoEntry.TABLE_NAME)
            else-> {
                    throw IllegalArgumentException("Error: insert uri $uri")
                }
            }
    }

    private fun insertRecord(uri: Uri, values: ContentValues?, table: String): Uri?{
        val db = helper.writableDatabase
        val id = db.insert(table, null, values)
        if (id.equals(-1)){
            Log.d(TAG, "Error inserting $uri")
            return null
        }
        context!!.contentResolver.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, orderBy: String?): Cursor? {
        val db = helper.readableDatabase
        val cursor: Cursor
        val match = uriMatcher.match(uri)
        var selectionArgs = selectionArgs

        when (match){
            TASKS->{
                //val builder = SQLiteQueryBuilder()
                //cursor = builder.query(db, projection, selection, selectionArgs, null, null, orderBy)
                cursor = db.query(TodoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy)
            }

            TASKS_ID-> {
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString()) //as Array<out String>
                cursor = db.query(TodoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy)
            }

            else-> throw IllegalAccessException("Unknown URI")
        }

        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)
        var selection = selection
        var selectionArgs = selectionArgs

        when (match){
            TASKS->{
                return updateRecord(uri, values, selection, selectionArgs, TodoEntry.TABLE_NAME )
            }

            TASKS_ID-> {
                selection = "${TodoEntry.ID} =?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString()) //as Array<out String>
                return updateRecord(uri, values, selection, selectionArgs, TodoEntry.TABLE_NAME )
            }

            else-> throw IllegalAccessException("Update unknown URI $uri")
        }
    }

    private fun updateRecord(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?, tableName: String): Int{
        val db = helper.writableDatabase
        val id = db.update(tableName, values, selection, selectionArgs)
        if (id.equals(0)){
            Log.d(TAG, "Error updating $uri")
            return -1
        }
        return id
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)
        var selection = selection
        var selectionArgs = selectionArgs

        when (match){
            TASKS->{
                return deleteRecord(uri, null, null, TodoEntry.TABLE_NAME )
            }

            TASKS_ID-> {
                selection = "${TodoEntry.ID} =?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString()) //as Array<out String>
                return deleteRecord(uri, selection, selectionArgs, TodoEntry.TABLE_NAME )
            }

            else-> throw IllegalAccessException("Delete unknown URI $uri")
        }
    }

    private fun deleteRecord(uri: Uri, selection: String?, selectionArgs: Array<out String>?, tableName: String): Int{
        val db = helper.writableDatabase
        val id = db.delete(tableName, selection, selectionArgs)
        if (id.equals(0)){
            Log.d(TAG, "Error deleting $uri")
            return -1
        }
        context!!.contentResolver.notifyChange(uri, null)
        return id
    }

    override fun getType(p0: Uri): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}