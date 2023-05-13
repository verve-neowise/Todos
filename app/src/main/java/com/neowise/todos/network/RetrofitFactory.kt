package com.neowise.todos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://faveo.uz:8080/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val todosService: TodosService = retrofit.create(TodosService::class.java)
}