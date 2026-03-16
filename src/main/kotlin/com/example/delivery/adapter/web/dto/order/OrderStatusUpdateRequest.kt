package com.example.delivery.adapter.web.dto.order

import com.example.delivery.domain.model.OrderStatus
import jakarta.validation.constraints.NotNull

data class OrderStatusUpdateRequest(
    @field:NotNull(message = "Статус обязателен")
    val status: OrderStatus
)
