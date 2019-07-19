package com.melardev.android.crud.todos.show

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.melardev.android.crud.R
import com.melardev.android.crud.datasource.local.DbTodoRepository
import com.melardev.android.crud.datasource.local.Todo
import com.melardev.android.crud.extensions.longTimeToStr
import com.melardev.android.crud.todos.write.TodoCreateEditActivity
import kotlinx.android.synthetic.main.activity_todo_details.*

class TodoDetailsActivity : AppCompatActivity() {

    private val todoRepository = DbTodoRepository(this)
    private var todo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)


        val intent = intent
        val todoId = intent.getLongExtra("TODO_ID", -1)
        if (todoId.toInt() == -1) {
            Toast.makeText(this, "Invalid Todo Id", Toast.LENGTH_SHORT).show()
            finish()
        }

        this.todo = todoRepository.getById(todoId)
        if (this.todo == null) {
            Toast.makeText(this, "Todo Does not exist", Toast.LENGTH_SHORT).show()
            finish()
        }

        txtDetailsId.text = todo!!.id.toString()
        txtDetailsTitle.text = todo!!.title
        txtDetailsDescription.text = todo!!.description
        txtDetailsCreatedAt.text = todo!!.createdAt!!.longTimeToStr()
        txtDetailsUpdatedAt.text = todo!!.updatedAt!!.longTimeToStr()
    }

    private fun delete() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure You want to delete this todo?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            if (todoRepository.deleteById(todo!!.id!!) > 0) {
                Toast.makeText(this@TodoDetailsActivity, "Todo Deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@TodoDetailsActivity, "Todo does not exist", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
    }

    fun onButtonClicked(view: View) {
        when {
            btnDetailsEditTodo === view -> {
                val intent = Intent()
                intent.component = ComponentName(this, TodoCreateEditActivity::class.java)
                intent.putExtra("TODO_ID", todo!!.id)
                startActivity(intent)
                finish()
            }
            btnDetailsDeleteTodo === view -> {
                delete()
                return
            }
            btnDetailsGoHome === view -> finish()
        }
    }
}
