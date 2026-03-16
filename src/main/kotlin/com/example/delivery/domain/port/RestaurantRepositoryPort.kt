package com.example.delivery.domain.port

import com.example.delivery.domain.model.Restaurant
import com.example.delivery.domain.model.Dish

interface RestaurantRepositoryPort {
    fun create(restaurant: Restaurant): Restaurant
    fun findById(id: Long): Restaurant?
    fun findAll(): List<Restaurant>
    fun findByName(name: String): Restaurant?
    fun update(restaurant: Restaurant): Restaurant
    fun delete(id: Long)
    fun findDishesByRestaurantId(restaurantId: Long): List<Dish>
}