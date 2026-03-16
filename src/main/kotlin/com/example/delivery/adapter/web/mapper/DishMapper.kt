package com.example.delivery.adapter.web.mapper

import com.example.delivery.adapter.web.dto.dish.DishCreateRequest
import com.example.delivery.adapter.web.dto.dish.DishResponse
import com.example.delivery.adapter.web.dto.dish.DishUpdateRequest
import com.example.delivery.domain.model.Dish

object DishMapper {
    fun toDomain(request: DishCreateRequest, restaurantId: Long) =
        Dish(
            id = null,
            name = request.name ?: "",
            description = request.description ?: "",
            price = request.price ?: java.math.BigDecimal.ZERO,
            isAvailable = request.isAvailable,
            restaurantId = restaurantId
        )

    fun toDomain(id: Long, request: DishUpdateRequest, restaurantId: Long) =
        Dish(
            id = id,
            name = request.name ?: "",
            description = request.description ?: "",
            price = request.price ?: java.math.BigDecimal.ZERO,
            isAvailable = request.isAvailable,
            restaurantId = restaurantId
        )

    fun toResponse(dish: Dish) =
        DishResponse(
            id = dish.id!!,
            name = dish.name,
            description = dish.description,
            price = dish.price,
            isAvailable = dish.isAvailable,
            restaurantId = dish.restaurantId
        )
}