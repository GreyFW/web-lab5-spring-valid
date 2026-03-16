package com.example.lab3.application.service

import com.example.lab3.application.exception.AlreadyExistsException
import com.example.lab3.application.exception.NotFoundException
import com.example.lab3.domain.model.Dish
import com.example.lab3.domain.port.DishRepositoryPort
import com.example.lab3.domain.port.RestaurantRepositoryPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
open class DishService(
    private val dishRepository: DishRepositoryPort,
    private val restaurantRepository: RestaurantRepositoryPort
) {
    private val logger = KotlinLogging.logger {}

    fun create(dish: Dish): CreateDishResult {
        restaurantRepository.findById(dish.restaurantId)
            ?: throw NotFoundException("Restaurant with id=${dish.restaurantId} not found")

        val existing = dishRepository.findByName(dish.name)
        return if (existing != null) {
            CreateDishResult(existing, false)
        } else {
            val created = dishRepository.create(dish)
            logger.info { "Создано блюдо id=${created.id}, name='${created.name}'" }
            CreateDishResult(created, true)
        }
    }

    fun getById(id: Long): Dish {
        logger.info { "Запрос блюда id=$id" }
        return dishRepository.findById(id) ?: throw NotFoundException("Dish with id=$id not found")
    }

    fun update(id: Long, updatedDish: Dish): Dish {
        val existingDish = dishRepository.findById(id)
            ?: throw NotFoundException("Dish with id=$id not found")

        val dishWithSameName = dishRepository.findByName(updatedDish.name)
        if (dishWithSameName != null && dishWithSameName.id != id)
            throw AlreadyExistsException("Dish with name='${updatedDish.name}' already exists")

        val dishToSave = existingDish.copy(
            name = updatedDish.name,
            description = updatedDish.description,
            price = updatedDish.price,
            isAvailable = updatedDish.isAvailable
        )
        val updated = dishRepository.update(dishToSave)
        logger.info { "Блюдо id=$id успешно обновлено" }
        return updated
    }

    fun delete(id: Long) {
        dishRepository.findById(id) ?: throw NotFoundException("Dish with id=$id not found")
        dishRepository.delete(id)
        logger.info { "Удалено блюдо id=$id" }
    }

    fun getAll(namePart: String? = null): List<Dish> =
        dishRepository.findAll(namePart)
}