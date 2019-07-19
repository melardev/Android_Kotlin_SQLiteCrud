package com.melardev.android.crud.todos.write

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.melardev.android.crud.R
import com.melardev.android.crud.datasource.local.DbTodoRepository
import com.melardev.android.crud.datasource.local.Todo
import kotlinx.android.synthetic.main.activity_todo_create_edit.*

class TodoCreateEditActivity : AppCompatActivity() {

    private var todo: Todo? = null
    private var editMode: Boolean = false
    private var todoRepository: DbTodoRepository? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_create_edit)

        val todoId = intent.getLongExtra("TODO_ID", -1)
        todoRepository = DbTodoRepository(this.applicationContext)

        if (todoId.toInt() != -1) {
            editMode = true

            todo = todoRepository!!.getById(todoId)

            eTxtTitle.setText(todo!!.title)
            eTxtDescription.setText(todo!!.description)
            eCheckboxCompleted.isChecked = todo!!.isCompleted
            btnSaveTodo.text = "Update"
        } else {
            btnSaveTodo.text = "Create"
        }
    }

    fun saveTodo(view: View) {
        val title = eTxtTitle!!.text.toString()
        val description = eTxtDescription!!.text.toString()

        if (editMode) {

            todo!!.title = title
            todo!!.description = description
            todo!!.isCompleted = eCheckboxCompleted!!.isChecked
            todoRepository!!.update(todo!!)
            Toast.makeText(this@TodoCreateEditActivity, "Todo Updated!", Toast.LENGTH_LONG).show()
            finish()

        } else {

            val todo = Todo()
            todo.title = title
            todo.description = description
            todo.isCompleted = eCheckboxCompleted!!.isChecked

            todoRepository!!.insert(todo)
            Toast.makeText(this@TodoCreateEditActivity, "Todo Created!", Toast.LENGTH_LONG).show()

            intent.putExtra("todo", todo)
            setResult(RESULT_OK, intent)
            finish()

        }


    }
}
