package com.example.taskscheduler.Data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


const val DATABASE_NAME = "TaskScheduler.db"
const val DATABASE_VERSION = 1
const val TABLE_TODOS_CREATE =
    "CREATE TABLE ${TodoEntry.TABLE_NAME}(" +
            "${TodoEntry.ID} INTEGER PRIMARY KEY," +
            "${TodoEntry.COLUMN_TEXT} TEXT," +
            "${TodoEntry.COLUMN_DESCRIPTION} TEXT, " +
            "${TodoEntry.COLUMN_DONE} INTEGER, " +
            "${TodoEntry.HOUR_CREATED} INTEGER, " +
            "${TodoEntry.MIN_CREATED} INTEGER )"


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(TABLE_TODOS_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${TodoEntry.TABLE_NAME}")
        onCreate(db)
    }
}