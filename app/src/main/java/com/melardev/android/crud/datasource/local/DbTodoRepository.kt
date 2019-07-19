package com.melardev.android.crud.datasource.local

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteException
import android.widget.Toast
import java.util.*

class DbTodoRepository(private val context: Context) : TodoRepository {

    override fun getAll(): List<Todo> {

        val appDbHelper = AppDbHelper.getInstance(context)

        try {
            /*
            // The equivalent using rawQuery()
            String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", COLUMN_TODO_ID, COLUMN_TODO_TITLE, COLUMN_TODO_COMPLETED, COLUMN_TODO_CREATED_AT, COLUMN_TODO_UPDATED_AT, TABLE_TODOS);
            cursor = db.rawQuery(SELECT_QUERY, null);
            */
            appDbHelper.readableDatabase.use { db ->
                db.query(TABLE_TODOS, null, null, null, null, null, null, null).use { cursor ->

                    if (cursor != null)
                        if (cursor.moveToFirst()) {
                            val todos = ArrayList<Todo>()
                            do {
                                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_ID))
                                val title = cursor.getString(cursor.getColumnIndex(COLUMN_TODO_TITLE))
                                val description = cursor.getString(cursor.getColumnIndex(COLUMN_TODO_DESCRIPTION))
                                val completed = cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_COMPLETED)) == 1
                                val createdAt = cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_CREATED_AT))
                                val updatedAt = cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_UPDATED_AT))

                                todos.add(Todo(id, title, description, completed, createdAt, updatedAt))
                            } while (cursor.moveToNext())

                            return todos
                        }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        return emptyList()
    }

    override fun insert(todo: Todo): Long {

        var id: Long = -1
        val appDbHelper = AppDbHelper.getInstance(context)
        val now = Date().time

        try {
            appDbHelper.writableDatabase.use { db ->
                val contentValues = ContentValues()
                contentValues.put(COLUMN_TODO_TITLE, todo.title)
                contentValues.put(COLUMN_TODO_DESCRIPTION, todo.description)
                contentValues.put(COLUMN_TODO_COMPLETED, todo.isCompleted)
                contentValues.put(COLUMN_TODO_CREATED_AT, now)
                contentValues.put(COLUMN_TODO_UPDATED_AT, now)

                id = db.insertOrThrow(TABLE_TODOS, null, contentValues)

                todo.id = id
                todo.createdAt = now
                todo.updatedAt = now
            }
        } catch (e: SQLiteException) {
            Toast
                .makeText(context, "Operation failed: " + e.message, Toast.LENGTH_LONG)
                .show()
        }

        return id
    }

    override fun count(): Long {
        val appDbHelper = AppDbHelper.getInstance(context)
        val db = appDbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT $COLUMN_TODO_ID FROM $TABLE_TODOS", null)
        val count = cursor.count
        cursor.close()
        return count.toLong()
    }

    fun count2(): Long {
        val appDbHelper = AppDbHelper.getInstance(context)
        val db = appDbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT count(*) FROM $TABLE_TODOS", null)

        if (cursor.moveToFirst()) {
            val count = cursor.getInt(0)
            cursor.close()
            return count.toLong()
        }

        return 0
    }

    fun count3(): Long {
        val appDbHelper = AppDbHelper.getInstance(context)
        appDbHelper.readableDatabase.use { db ->
            db.query(TABLE_TODOS, null, null, null, null, null, null).use { cursor ->
                val result = cursor.count
                return result.toLong()
            }
        }
    }

    private fun count4(): Long {
        val appDbHelper = AppDbHelper.getInstance(context)
        val db = appDbHelper.writableDatabase
        return DatabaseUtils.queryNumEntries(db, TABLE_TODOS)
    }

    override fun getById(todoId: Long): Todo? {

        val appDbHelper = AppDbHelper.getInstance(context)

        var todo: Todo? = null
        try {
            appDbHelper.readableDatabase.use { db ->
                db.query(
                    TABLE_TODOS, null,
                    "$COLUMN_TODO_ID = ? ", arrayOf(todoId.toString()), null, null, null
                ).use { cursor ->

                    /*
            // The same using rawQuery()
             String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", TABLE_TODOS, COLUMN_TODO_ID, String.valueOf(todoId));
             cursor = db.rawQuery(SELECT_QUERY, null);
             */

                    if (cursor.moveToFirst()) {
                        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_ID))
                        val title = cursor.getString(cursor.getColumnIndex(COLUMN_TODO_TITLE))
                        val description = cursor.getString(cursor.getColumnIndex(COLUMN_TODO_DESCRIPTION))
                        val isCompleted = cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_COMPLETED)) == 1
                        val createdAt = cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_CREATED_AT))
                        val updatedAt = cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_UPDATED_AT))

                        todo = Todo(id, title, description, isCompleted, createdAt, updatedAt)
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show()
        }

        return todo
    }


    fun update(todo: Todo): Long? {

        var rowCount: Long = 0
        val appDbHelper = AppDbHelper.getInstance(context)

        try {
            appDbHelper.writableDatabase.use { db ->
                val contentValues = ContentValues()
                contentValues.put(COLUMN_TODO_TITLE, todo.title)
                contentValues.put(COLUMN_TODO_DESCRIPTION, todo.description)
                contentValues.put(COLUMN_TODO_COMPLETED, todo.isCompleted)
                contentValues.put(COLUMN_TODO_CREATED_AT, todo.createdAt)
                contentValues.put(COLUMN_TODO_UPDATED_AT, Date().time)

                rowCount = db.update(
                    TABLE_TODOS, contentValues,
                    "$COLUMN_TODO_ID = ? ",
                    arrayOf(todo.id.toString())
                ).toLong()
            }
        } catch (e: SQLiteException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

        return rowCount
    }


    override fun delete(todo: Todo?): Int {
        return if (todo == null) 0 else deleteById(todo.id!!)
    }

    override fun deleteById(todoId: Long): Int {
        var deletedRowCount = -1
        val appDbHelper = AppDbHelper.getInstance(context)

        try {
            appDbHelper.writableDatabase.use { db ->
                deletedRowCount = db.delete(
                    TABLE_TODOS,
                    "$COLUMN_TODO_ID = ? ",
                    arrayOf(todoId.toString())
                )
            }
        } catch (e: SQLiteException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

        return deletedRowCount
    }

    override fun deleteAll(): Boolean {
        var deletedAll = false
        val appDbHelper = AppDbHelper.getInstance(context)

        try {
            appDbHelper.writableDatabase.use { db ->
                db.delete(TABLE_TODOS, null, null)

                val count = count4()

                if (count == 0L)
                    deletedAll = true

            }
        } catch (e: SQLiteException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

        return deletedAll
    }

    companion object {

        const val COLUMN_TODO_ID = "_id"
        const val TABLE_TODOS = "todos"
        const val COLUMN_TODO_TITLE = "title"
        const val COLUMN_TODO_DESCRIPTION = "description"
        const val COLUMN_TODO_COMPLETED = "completed"
        const val COLUMN_TODO_CREATED_AT = "created_at"
        const val COLUMN_TODO_UPDATED_AT = "updated_at"
    }
}
