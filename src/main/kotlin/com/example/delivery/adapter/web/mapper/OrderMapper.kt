package com.example.delivery.adapter.web.mapper

import com.example.delivery.adapter.web.dto.order.OrderDishResponse
import com.example.delivery.adapter.web.dto.order.OrderResponse
import com.example.delivery.domain.model.Order

object OrderMapper {
    fun toResponse(order: Order) = OrderResponse(
        id = order.id!!,
        userId = order.userId,
        status = order.status,
        createdAt = order.createdAt,
        dishes = order.dishes.map { dish ->
            OrderDishResponse(
                id = dish.id!!,
                name = dish.name,
                description = dish.description,
                price = dish.price,
                isAvailable = dish.isAvailable,
                restaurantId = dish.restaurantId
            )
        }
    )
}