package com.example.delivery.application.service

import com.example.delivery.domain.model.User

data class CreateUserResult(
    val user: User,
    val isCreated: Boolean
)