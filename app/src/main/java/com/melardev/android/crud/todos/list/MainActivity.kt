package com.melardev.android.crud.todos.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melardev.android.crud.R
import com.melardev.android.crud.datasource.local.DbTodoRepository
import com.melardev.android.crud.datasource.local.Todo
import com.melardev.android.crud.todos.show.TodoDetailsActivity
import com.melardev.android.crud.todos.write.TodoCreateEditActivity

class MainActivity : AppCompatActivity(), TodoRecyclerAdapter.TodoRowEventListener {

    private lateinit var todos: List<Todo>
    private val todoRepository = DbTodoRepository(this)

    private var recyclerView: RecyclerView? = null
    private var todoRecyclerAdapter: TodoRecyclerAdapter? = null
    val TODO_CREATE: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<RecyclerView>(R.id.rvTodos)

        todos = todoRepository.getAll()

        todoRecyclerAdapter = TodoRecyclerAdapter(this)
        todoRecyclerAdapter!!.setItems(todos)
        recyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = todoRecyclerAdapter

    }

    fun createTodo(view: View) {
        val intent = Intent(this, TodoCreateEditActivity::class.java)
        startActivityForResult(intent, TODO_CREATE)
    }

    override fun onClicked(todo: Todo) {
        val intent = Intent(this, TodoDetailsActivity::class.java)
        intent.putExtra("TODO_ID", todo.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == TODO_CREATE) {
                val todo = data!!.getParcelableExtra<Todo>("todo")
                todoRecyclerAdapter!!.addItem(todo)
            }
        }
    }
}
