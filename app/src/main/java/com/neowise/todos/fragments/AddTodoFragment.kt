package com.neowise.todos.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.neowise.todos.R
import com.neowise.todos.databinding.FragmentAddTodoBinding
import com.neowise.todos.viewmodels.AddState
import com.neowise.todos.viewmodels.AddTodoViewModel

class AddTodoFragment : Fragment(R.layout.fragment_add_todo) {

    private lateinit var binding: FragmentAddTodoBinding

    private val viewModel: AddTodoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAddTodoBinding.bind(view)

        binding.addBtn.setOnClickListener {
            val title = binding.titleEdit.text.toString()
            viewModel.createTodo(title)
        }

        viewModel.liveData.value = AddState.Nothing

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is AddState.Nothing -> {}
                is AddState.Loading -> {
                    binding.addBtn.isEnabled = false
                }
                is AddState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    binding.addBtn.isEnabled = true
                }
                is AddState.Success -> {
                    binding.addBtn.isEnabled = true
                    findNavController().popBackStack()
                }
            }
        }


    }
}