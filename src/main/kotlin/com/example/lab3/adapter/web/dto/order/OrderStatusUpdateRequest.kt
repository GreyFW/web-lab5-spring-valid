package com.example.lab3.adapter.web.dto.order

import com.example.lab3.domain.model.OrderStatus
import jakarta.validation.constraints.NotNull

data class OrderStatusUpdateRequest(
    @field:NotNull(message = "Статус обязателен")
    val status: OrderStatus
)
