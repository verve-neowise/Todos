package com.neowise.todos.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neowise.todos.R
import com.neowise.todos.adapters.TodosAdapter
import com.neowise.todos.databinding.FragmentTodosBinding
import com.neowise.todos.dto.Todo

class TodosFragment : Fragment(R.layout.fragment_todos) {

    private lateinit var binding: FragmentTodosBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireContext()
            .getSharedPreferences("user", Context.MODE_PRIVATE)

        binding = FragmentTodosBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_todos)

        binding.toolbar.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
                R.id.logout -> {
                    preferences.edit()
                        .clear()
                        .apply()

                    findNavController().navigate(R.id.from_todos_to_login)
                }
            }
            true
        }

        binding.recucler.layoutManager = LinearLayoutManager(requireContext())
        binding.recucler.adapter = TodosAdapter(
            listOf(
                Todo(id = 1, title = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem", isCompleted = true),
                Todo(id = 2, title = "when an unknown printer took a galley of type and scrambled", isCompleted = false),
                Todo(id = 3, title = "Lorem Ipsum passages, and more recently with desktop publishing software like ", isCompleted = false),
            )
        )
    }
}