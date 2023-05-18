package com.neowise.todos.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neowise.todos.dto.Complete
import com.neowise.todos.dto.Todo
import com.neowise.todos.dto.TodoList
import com.neowise.todos.network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

sealed interface TodosState {
    object Loading : TodosState
    class Success(val todos: List<Todo>) : TodosState
    class Error(val message: String) : TodosState
    object Logout : TodosState
}

class TodosViewModel(application: Application) : AndroidViewModel(application) {

    val liveData = MutableLiveData<TodosState>(TodosState.Loading)

    val todosService = RetrofitFactory.todosService

    private val preferences = application.applicationContext
        .getSharedPreferences("user", Context.MODE_PRIVATE)

    fun logout() {
        preferences.edit()
            .clear()
            .apply()

        liveData.value = TodosState.Logout
    }

    fun fetch() {
        val token = preferences.getString("token", "")

        Log.d("TAG", "fetch: $token")

        todosService.getTodos("Bearer $token")
            .enqueue(object : Callback<TodoList> {
                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        liveData.value = TodosState.Success(list ?: listOf())
                    }
                    else {
                        Log.d("TAG", "onResponse: ${response.errorBody()?.string()}")
                        liveData.value = TodosState.Error("Error ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<TodoList>, t: Throwable) {
                    Log.d("TODOS", "${t}")
                    liveData.value = TodosState.Error("Error ${t.message}")
                }
            })
    }

    fun checked(todo: Todo, check: Boolean) {
        val token = preferences.getString("token", "") ?: ""

        viewModelScope.launch(Dispatchers.IO) {
            val complete = Complete(check)
            try {
                val response = todosService.completeTodo("Bearer $token", todo.id, complete).execute()
                if (response.isSuccessful) {
                    fetch()
                    Log.d("CHECK TODO", "checked: ${response.body()}")
                }
                else {
                    Log.e("CHECK TODO", "checked: ${response.errorBody()?.string()}")
                }
            }
            catch (e: java.lang.Exception) {
                Log.e("CHECK TODO", "checked: $e", )
            }
        }
    }

    fun delete(todo: Todo) {
        val token = preferences.getString("token", "") ?: ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = todosService.deleteTodo("Bearer $token", todo.id).execute()
                if (response.isSuccessful) {
                    fetch()
                    Log.d("DELETE TODO", "DELETE: ${response.body()}")
                }
                else {
                    Log.e("DELETE TODO", "DELETE: ${response.errorBody()?.string()}")
                }
            }
            catch (e: java.lang.Exception) {
                Log.e("DELETE", "DELETE: $e", )
            }
        }
    }
}