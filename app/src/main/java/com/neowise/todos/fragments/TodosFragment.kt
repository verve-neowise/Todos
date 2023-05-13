package com.neowise.todos.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neowise.todos.R
import com.neowise.todos.adapters.TodosAdapter
import com.neowise.todos.databinding.FragmentTodosBinding
import com.neowise.todos.dto.Todo
import com.neowise.todos.viewmodels.TodosState
import com.neowise.todos.viewmodels.TodosViewModel

class TodosFragment : Fragment(R.layout.fragment_todos) {

    private lateinit var binding: FragmentTodosBinding

    private val viewModel: TodosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTodosBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_todos)

        binding.toolbar.setOnMenuItemClickListener { menu ->
           when(menu.itemId) {
               R.id.logout -> {
                   viewModel.logout()
               }
           }
            true
        }

        binding.recucler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is TodosState.Success -> {
                    binding.recucler.adapter = TodosAdapter(state.todos)
                }
                is TodosState.Error -> {
                    Toast.makeText(requireContext(), "Error: ${state.message}" , Toast.LENGTH_SHORT).show()
                }
                is TodosState.Loading -> {
                }
                is TodosState.Logout -> {
                    findNavController().navigate(R.id.from_todos_to_login)
                }
            }
        }


        viewModel.fetch()
    }
}