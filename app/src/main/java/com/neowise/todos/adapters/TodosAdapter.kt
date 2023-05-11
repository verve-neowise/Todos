package com.neowise.todos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.neowise.todos.R
import com.neowise.todos.databinding.ItemTodoBinding
import com.neowise.todos.dto.Todo

class TodosAdapter(private val items: List<Todo>) : RecyclerView.Adapter<TodosAdapter.TodoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoHolder(view)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val todo = items[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TodoHolder(view: View) : ViewHolder(view) {

        private val binding = ItemTodoBinding.bind(view)

        fun bind(todo: Todo) {
            binding.id.text = todo.id.toString()
            binding.title.text = todo.title
            binding.checkBox.isChecked = todo.isCompleted
        }
    }
}