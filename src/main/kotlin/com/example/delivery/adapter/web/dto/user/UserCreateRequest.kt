package com.example.delivery.adapter.web.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserCreateRequest(
    @field:Email(message = "Некорректный формат email")
    @field:NotBlank(message = "Email не может быть пустым")
    val email: String? = null,

    @field:NotBlank(message = "Имя не может быть пустым")
    val firstName: String? = null,

    @field:NotBlank(message = "Фамилия не может быть пустой")
    val lastName: String? = null,

    val isActive: Boolean = true
)