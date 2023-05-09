package com.neowise.todos.network

import com.neowise.todos.dto.LoginRequest
import com.neowise.todos.dto.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TodosService {

    @POST("account")
    fun login(@Body request: LoginRequest): Call<UserResponse>
}