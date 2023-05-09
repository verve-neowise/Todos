package com.neowise.todos.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.neowise.todos.viewmodels.LoginState
import com.neowise.todos.viewmodels.LoginViewModel
import com.neowise.todos.R
import com.neowise.todos.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by activityViewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is LoginState.Success -> {
                    findNavController().navigate(R.id.from_login_to_todos)
                }
                is LoginState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    binding.continueBtn.isEnabled = true
                }
                is LoginState.Loading -> {
                    binding.continueBtn.isEnabled = false
                }
                is LoginState.Nothing -> {}
            }
        }

        binding.continueBtn.setOnClickListener {
            viewModel.login(
                name = binding.nameEdit.text.toString(),
                username = binding.usernameEdit.text.toString(),
                password = binding.passwordEdit.text.toString()
            )
        }
    }
}

/*
git config --global user.name your-nickname
git config --global user.email your-email
 */