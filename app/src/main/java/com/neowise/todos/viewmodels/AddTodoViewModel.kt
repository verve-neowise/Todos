package com.neowise.todos.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neowise.todos.dto.CreateTodo
import com.neowise.todos.network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


sealed interface AddState {
    object Nothing : AddState
    object Loading : AddState
    class Error(val message: String) : AddState
    class Success : AddState
}

class AddTodoViewModel(application: Application) : AndroidViewModel(application) {

    val liveData = MutableLiveData<AddState>(AddState.Nothing)
    val todosService = RetrofitFactory.todosService

    private val preferences = application.applicationContext
        .getSharedPreferences("user", Context.MODE_PRIVATE)

    fun createTodo(title: String) {

        liveData.value = AddState.Loading

        val token = preferences.getString("token", "")
        val request = CreateTodo(title)

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = todosService.postTodo("Bearer $token", request).execute()

                launch(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        liveData.value = AddState.Success()
                    }
                    else {
                        liveData.value = AddState.Error(response.message())
                    }
                }
            }
            catch (e: java.lang.Exception) {
                Log.d("Create", "createTodo: $e")
                launch(Dispatchers.Main){
                    liveData.value = AddState.Error(e.message ?: "Error")
                }
            }
        }
        // success
        // error
    }
}