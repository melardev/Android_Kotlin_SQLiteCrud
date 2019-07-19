package com.melardev.android.crud.todos.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melardev.android.crud.R
import com.melardev.android.crud.datasource.local.Todo
import com.melardev.android.crud.extensions.longTimeToStr
import java.util.*

class TodoRecyclerAdapter(private val todoRowEventListener: TodoRowEventListener) :
    RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>() {

    private val todos: ArrayList<Todo> = ArrayList()

    interface TodoRowEventListener {


        fun onClicked(todo: Todo)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = layoutInflater.inflate(R.layout.todo_row, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val todo = todos[position]

        viewHolder.txtId.text = todo.id.toString()
        viewHolder.txtTitle.text = todo.title
        viewHolder.txtDescription.text = todo.description
        viewHolder.checkboxCompleted.isChecked = todo.isCompleted
        viewHolder.txtCreatedAt.text = todo.createdAt!!.longTimeToStr()
        viewHolder.txtUpdatedAt.text = todo.updatedAt!!.longTimeToStr()

        viewHolder.itemView.setOnClickListener { todoRowEventListener.onClicked(todo) }
    }

    fun addItems(todos: List<Todo>?) {
        if (todos == null)
            return
        val startPosition = this.todos.size
        this.todos.addAll(todos)
        notifyItemRangeChanged(startPosition, todos.size)
    }

    fun addItem(todo: Todo?) {
        if (todo == null)
            return
        val startPosition = this.todos.size
        this.todos.add(todo)
        notifyItemRangeChanged(startPosition, todos.size)
    }

    fun setItems(todos: List<Todo>?) {
        this.todos.clear()
        addItems(todos!!)
    }

    fun getItem(position: Int): Todo {
        return todos[position]
    }


    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtId: TextView = view.findViewById(R.id.txtId)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtDescription: TextView = view.findViewById(R.id.txtDescription)
        val txtCreatedAt: TextView = view.findViewById(R.id.txtCreatedAt)
        val txtUpdatedAt: TextView = view.findViewById(R.id.txtUpdatedAt)
        val checkboxCompleted: CheckBox = view.findViewById(R.id.checkboxCompleted)
    }
}
