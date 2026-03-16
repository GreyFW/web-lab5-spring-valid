package com.example.delivery.application.service

import com.example.delivery.application.exception.AlreadyExistsException
import com.example.delivery.application.exception.NotFoundException
import com.example.delivery.domain.model.Dish
import com.example.delivery.domain.model.Restaurant
import com.example.delivery.domain.port.RestaurantRepositoryPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
open class RestaurantService(
    private val restaurantRepository: RestaurantRepositoryPort
) {
    private val logger = KotlinLogging.logger {}

    fun create(restaurant: Restaurant): Restaurant {
        val existing = restaurantRepository.findByName(restaurant.name)
        if (existing != null) throw AlreadyExistsException("Restaurant with name='${restaurant.name}' already exists")

        val created = restaurantRepository.create(restaurant)
        logger.info { "Создан ресторан id=${created.id}, name='${created.name}'" }
        return created
    }

    fun getById(id: Long): Restaurant {
        logger.info { "Запрос ресторана id=$id" }
        return restaurantRepository.findById(id) ?: throw NotFoundException("Restaurant with id=$id not found")
    }

    fun getAll(): List<Restaurant> = restaurantRepository.findAll()

    fun update(id: Long, updated: Restaurant): Restaurant {
        restaurantRepository.findById(id) ?: throw NotFoundException("Restaurant with id=$id not found")
        val withSameName = restaurantRepository.findByName(updated.name)
        if (withSameName != null && withSameName.id != id)
            throw AlreadyExistsException("Restaurant with name='${updated.name}' already exists")

        val saved = restaurantRepository.update(updated.copy(id = id))
        logger.info { "Ресторан id=$id успешно обновлен" }
        return saved
    }

    fun delete(id: Long) {
        restaurantRepository.findById(id) ?: throw NotFoundException("Restaurant with id=$id not found")
        restaurantRepository.delete(id)
        logger.info { "Удален ресторан id=$id" }
    }

    fun getDishes(restaurantId: Long): List<Dish> {
        restaurantRepository.findById(restaurantId) ?: throw NotFoundException("Restaurant with id=$restaurantId not found")
        return restaurantRepository.findDishesByRestaurantId(restaurantId)
    }
}