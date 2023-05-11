package com.neowise.todos.viewmodels

import android.app.Application
import android.content.Context
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

    private val preferences = application.applicationContext
        .getSharedPreferences("user", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        val token = preferences.getString("token", "")
        return token?.isNotEmpty() ?: false
    }

    fun login(name: String, username: String, password: String) {

        val request = LoginRequest(name, username, password)

        liveData.value = LoginState.Loading

        todosService.login(request).enqueue(object : Callback<UserResponse> {

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val user: User = response.body()!!.user
                    storeUser(user)
                    liveData.value = LoginState.Success(user)
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

    fun storeUser(user: User) {
        preferences.edit()
            .putInt("id", user.id)
            .putString("name", user.name)
            .putString("username", user.username)
            .putString("token", user.token)
            .apply()
    }
}