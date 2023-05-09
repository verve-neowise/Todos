package com.neowise.todos.dto

class User(
    val id: Int,
    val name: String,
    val username: String,
    val token: String
)

class UserResponse(
    val user: User
)