package com.neowise.todos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.neowise.todos.dto.LoginRequest
import com.neowise.todos.dto.User
import com.neowise.todos.dto.UserResponse
import com.neowise.todos.network.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// state
//   +login
//   +error

sealed interface LoginState {
    class Success(val user: User) : LoginState
    class Error(val message: String): LoginState
    object Loading: LoginState
    object Nothing : LoginState
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val liveData = MutableLiveData<LoginState>(LoginState.Nothing)

    private val todosService = RetrofitFactory.todosService

    fun login(name: String, username: String, password: String) {

        val request = LoginRequest(name, username, password)

        liveData.value = LoginState.Loading

        todosService.login(request).enqueue(object : Callback<UserResponse> {

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    liveData.value = LoginState.Success(response.body()!!.user)
                }
                else {
                    liveData.value = LoginState.Error(response.message())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                liveData.value = LoginState.Error(t.message ?: "Error on request")
            }
        })
    }
}