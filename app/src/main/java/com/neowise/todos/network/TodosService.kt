package com.neowise.todos.network

import com.neowise.todos.dto.LoginRequest
import com.neowise.todos.dto.TodoList
import com.neowise.todos.dto.User
import com.neowise.todos.dto.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TodosService {

    @POST("account")
    fun login(@Body request: LoginRequest): Call<User>

    @GET("todos")
    fun getTodos(@Header("Authorization") token: String): Call<TodoList>
}