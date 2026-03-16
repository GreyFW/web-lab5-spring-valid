package com.example.delivery.domain.model

import java.time.LocalDateTime

enum class OrderStatus {
    PENDING, CONFIRMED, DELIVERED, CANCELLED
}

data class Order(
    val id: Long?,
    val userId: Long,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
    val dishes: List<Dish>
)