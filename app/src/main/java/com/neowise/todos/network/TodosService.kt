package com.neowise.todos.network

import com.neowise.todos.dto.*
import retrofit2.Call
import retrofit2.http.*

interface TodosService {

    @POST("account")
    fun login(@Body request: LoginRequest): Call<User>

    @GET("todos")
    fun getTodos(@Header("Authorization") token: String): Call<TodoList>

    @POST("todos")
    fun postTodo(@Header("Authorization") token: String, @Body createTodo: CreateTodo) : Call<Todo>

    @DELETE("todos/{id}")
    fun deleteTodo(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ) : Call<Todo>

    @PATCH("todos/{id}")
    fun completeTodo(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body complete: Complete
    ): Call<Todo>

    @PUT("todos/{id}")
    fun updateTodo(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body update: CreateTodo
    ) : Call<Todo>
}