package com.example.delivery.adapter.web.dto.restaurant

import jakarta.validation.constraints.NotBlank

data class RestaurantUpdateRequest(
    @field:NotBlank(message = "Название не может быть пустым")
    val name: String? = null,

    @field:NotBlank(message = "Адрес не может быть пустым")
    val address: String? = null
)