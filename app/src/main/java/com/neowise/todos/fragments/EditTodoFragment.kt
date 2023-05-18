package com.neowise.todos.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.neowise.todos.R
import com.neowise.todos.databinding.FragmentAddTodoBinding
import com.neowise.todos.databinding.FragmentEditTodoBinding
import com.neowise.todos.viewmodels.AddState
import com.neowise.todos.viewmodels.AddTodoViewModel
import com.neowise.todos.viewmodels.EditState
import com.neowise.todos.viewmodels.EditTodoViewModel

class EditTodoFragment : Fragment(R.layout.fragment_edit_todo) {

    private val viewModel: EditTodoViewModel by viewModels()

    private lateinit var binding: FragmentEditTodoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentEditTodoBinding.bind(view)

        val id = arguments?.getInt("id") ?: 0
        val title = arguments?.getString("title")
        val isCompleted = arguments?.getBoolean("isCompleted")

        binding.addBtn.setOnClickListener {
            val title = binding.titleEdit.text.toString()
            viewModel.updateTodo(id, title)
        }

        binding.titleEdit.setText(title)

        viewModel.liveData.value = EditState.Nothing

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is EditState.Nothing -> {}
                is EditState.Loading -> {
                    binding.addBtn.isEnabled = false
                }
                is EditState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    binding.addBtn.isEnabled = true
                }
                is EditState.Success -> {
                    binding.addBtn.isEnabled = true
                    findNavController().popBackStack()
                }
            }
        }
    }
}