package com.example.delivery.adapter.web.mapper

import com.example.delivery.adapter.web.dto.restaurant.RestaurantCreateRequest
import com.example.delivery.adapter.web.dto.restaurant.RestaurantResponse
import com.example.delivery.adapter.web.dto.restaurant.RestaurantUpdateRequest
import com.example.delivery.domain.model.Restaurant

object RestaurantMapper {
    fun toDomain(request: RestaurantCreateRequest) =
        Restaurant(id = null, name = request.name, address = request.address)

    fun toDomain(id: Long, request: RestaurantUpdateRequest) =
        Restaurant(id = id, name = request.name, address = request.address)

    fun toResponse(restaurant: Restaurant) =
        RestaurantResponse(id = restaurant.id!!, name = restaurant.name, address = restaurant.address)
}