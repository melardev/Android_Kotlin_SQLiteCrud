package com.melardev.android.crud.datasource.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDbHelper
private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        // Create tables SQL execution
        val createTodosTable = ("CREATE TABLE " + DbTodoRepository.TABLE_TODOS + "("
                + DbTodoRepository.COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbTodoRepository.COLUMN_TODO_TITLE + " TEXT NOT NULL, "
                + DbTodoRepository.COLUMN_TODO_DESCRIPTION + " TEXT, "
                + DbTodoRepository.COLUMN_TODO_COMPLETED + " INTEGER, " //nullable
                + DbTodoRepository.COLUMN_TODO_CREATED_AT + " INTEGER, " //nullable
                + DbTodoRepository.COLUMN_TODO_UPDATED_AT + " INTEGER " //nullable
                + ")")


        db.execSQL(createTodosTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DbTodoRepository.TABLE_TODOS)

        // Create tables again
        onCreate(db)
    }

    companion object {
        private var databaseHelper: AppDbHelper? = null

        // All Static variables
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "todos"

        @Synchronized
        fun getInstance(context: Context): AppDbHelper {
            if (databaseHelper == null) {
                databaseHelper = AppDbHelper(context)
            }
            return databaseHelper as AppDbHelper
        }
    }
}
