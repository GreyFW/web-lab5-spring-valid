package com.example.delivery.adapter.persistence.mock

import com.example.delivery.application.exception.AlreadyExistsException
import com.example.delivery.application.exception.NotFoundException
import com.example.delivery.domain.model.Dish
import com.example.delivery.domain.port.DishRepositoryPort
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("mock")
class DishMockRepository : DishRepositoryPort {
    private val dishesStorage = mutableMapOf<Long, Dish>()
    private var idCounter = 1L

    override fun create(dish: Dish): Dish {
        val existingDish = dishesStorage.values.find { it.name == dish.name }
        if (existingDish != null) {
            throw AlreadyExistsException("Dish with name='${dish.name}' already exists")
        }
        val id = idCounter++
        val newDish = dish.copy(id = id)
        dishesStorage[id] = newDish
        return newDish
    }

    override fun findById(id: Long): Dish? = dishesStorage[id]

    override fun findAll(namePart: String?): List<Dish> {
        if (namePart == null) return dishesStorage.values.toList()
        return dishesStorage.values
            .filter { it.name.contains(namePart, ignoreCase = true) }
            .toList()
    }

    override fun findByName(name: String): Dish? =
        dishesStorage.values.find { it.name.equals(name, ignoreCase = true) }

    override fun update(dish: Dish): Dish {
        val id = dish.id ?: throw NotFoundException("Dish id cannot be null")
        val existingDish = dishesStorage[id] ?: throw NotFoundException("Dish with id=$id not found")
        val updatedDish = existingDish.copy(
            name = dish.name,
            description = dish.description,
            price = dish.price,
            isAvailable = dish.isAvailable
        )
        dishesStorage[id] = updatedDish
        return updatedDish
    }

    override fun delete(id: Long) {
        dishesStorage[id] ?: throw NotFoundException("Dish with id=$id not found")
        dishesStorage.remove(id)
    }
}