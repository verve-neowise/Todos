package com.neowise.todos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.neowise.todos.R
import com.neowise.todos.databinding.ItemTodoBinding
import com.neowise.todos.dto.Todo

interface TodoCallback {
    fun checked(todo: Todo, check: Boolean)
    fun delete(todo: Todo)
    fun edit(todo: Todo)
}

class TodosAdapter(
    private val items: List<Todo>,
    private val callback: TodoCallback
) : RecyclerView.Adapter<TodosAdapter.TodoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_todo, parent, false)

        return TodoHolder(view, callback)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val todo = items[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TodoHolder(view: View, private val callback: TodoCallback) : ViewHolder(view) {

        private val binding = ItemTodoBinding.bind(view)

        private var isOpen = false

        fun bind(todo: Todo) {
            binding.id.text = todo.id.toString()
            binding.title.text = todo.title
            binding.checkBox.isChecked = todo.isCompleted

            isOpen = false

            binding.root.setOnClickListener {
                isOpen = !isOpen
                binding.buttons.visibility = if (isOpen) View.VISIBLE else View.GONE
            }

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                callback.checked(todo, isChecked)
            }

            binding.deleteBtn.setOnClickListener {
                callback.delete(todo)
            }

            binding.editBtn.setOnClickListener {
                callback.edit(todo)
            }
        }
    }
}