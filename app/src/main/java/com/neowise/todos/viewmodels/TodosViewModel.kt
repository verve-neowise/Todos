package com.neowise.todos.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.neowise.todos.dto.Todo
import com.neowise.todos.dto.TodoList
import com.neowise.todos.network.RetrofitFactory
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
}