package com.example.delivery.application.service

import com.example.delivery.application.exception.InvalidOrderStateException
import com.example.delivery.application.exception.NotFoundException
import com.example.delivery.domain.model.Order
import com.example.delivery.domain.model.OrderStatus
import com.example.delivery.domain.port.DishRepositoryPort
import com.example.delivery.domain.port.OrderRepositoryPort
import com.example.delivery.domain.port.UserRepositoryPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class OrderService(
    private val orderRepository: OrderRepositoryPort,
    private val userRepository: UserRepositoryPort,
    private val dishRepository: DishRepositoryPort
) {
    private val logger = KotlinLogging.logger {}

    fun create(userId: Long, dishIds: List<Long>): Order {
        userRepository.findById(userId)
            ?: throw NotFoundException("User with id=$userId not found")

        val dishes = dishIds.map { dishId ->
            dishRepository.findById(dishId)
                ?: throw NotFoundException("Dish with id=$dishId not found")
        }

        val order = Order(
            id = null,
            userId = userId,
            status = OrderStatus.PENDING,
            createdAt = LocalDateTime.now(),
            dishes = dishes
        )
        val created = orderRepository.create(order)
        logger.info { "Создан заказ id=${created.id}, userId=$userId, блюд=${dishes.size}" }
        return created
    }

    fun getById(id: Long): Order {
        logger.info { "Запрос заказа id=$id" }
        return orderRepository.findById(id)
            ?: throw NotFoundException("Order with id=$id not found")
    }

    fun getAll(userId: Long?, status: OrderStatus?): List<Order> =
        orderRepository.findAll(userId, status)

    fun updateStatus(id: Long, newStatus: OrderStatus): Order {
        val order = orderRepository.findById(id)
            ?: throw NotFoundException("Order with id=$id not found")

        val allowed = mapOf(
            OrderStatus.PENDING   to setOf(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
            OrderStatus.CONFIRMED to setOf(OrderStatus.DELIVERED, OrderStatus.CANCELLED),
            OrderStatus.DELIVERED to emptySet(),
            OrderStatus.CANCELLED to emptySet()
        )
        if (newStatus !in (allowed[order.status] ?: emptySet<OrderStatus>())) {
            throw InvalidOrderStateException(
                "Invalid status transition from ${order.status} to $newStatus"
            )
        }

        val updated = orderRepository.update(order.copy(status = newStatus))
        logger.warn { "Статус заказа id=$id изменён: ${order.status} → $newStatus" }
        return updated
    }
}