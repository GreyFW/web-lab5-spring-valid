package com.example.delivery.domain.port

import com.example.delivery.domain.model.Order
import com.example.delivery.domain.model.OrderStatus

interface OrderRepositoryPort {
    fun create(order: Order): Order
    fun findById(id: Long): Order?
    fun findAll(userId: Long?, status: OrderStatus?): List<Order>
    fun update(order: Order): Order
}