package com.example.delivery.adapter.web.dto.dish

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class DishUpdateRequest(
    @field:NotBlank(message = "Название не может быть пустым")
    val name: String,

    @field:NotBlank(message = "Описание не может быть пустым")
    val description: String,

    @field:NotNull(message = "Цена обязательна")
    @field:Min(value = 1, message = "Цена должна быть больше 0")
    val price: BigDecimal,

    val isAvailable: Boolean = true
)