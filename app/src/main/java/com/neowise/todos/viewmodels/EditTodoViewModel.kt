package com.neowise.todos.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neowise.todos.dto.CreateTodo
import com.neowise.todos.dto.Todo
import com.neowise.todos.network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface EditState {
    object Nothing : EditState
    object Loading : EditState
    class Error(val message: String) : EditState
    class Success : EditState
}

class EditTodoViewModel(application: Application) : AndroidViewModel(application) {

    val liveData = MutableLiveData<EditState>(EditState.Nothing)
    val todosService = RetrofitFactory.todosService

    private val preferences = application.applicationContext
        .getSharedPreferences("user", Context.MODE_PRIVATE)

    fun updateTodo(id: Int, title: String) {
        val token = preferences.getString("token", "") ?: ""
        val update = CreateTodo(title)

        liveData.value = EditState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = todosService.updateTodo("Bearer $token", id, update).execute()
                launch(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        liveData.value = EditState.Success()
                    }
                    else {
                        liveData.value = EditState.Error(response.errorBody()?.string() ?: "Error")
                    }
                }
            }
            catch (e: Exception) {
                launch(Dispatchers.Main) {
                    liveData.value = EditState.Error(e.message ?: "Error")
                }
            }
        }
    }
}