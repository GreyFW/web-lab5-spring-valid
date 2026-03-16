package com.example.delivery.adapter.web.mapper

import com.example.delivery.adapter.web.dto.user.*
import com.example.delivery.domain.model.User

object UserMapper {
    fun toDomain(request: UserCreateRequest): User =
        User(
            id = null,
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName,
            isActive = request.isActive
        )
    fun toDomain(id: Long, request: UserUpdateRequest): User =
        User(
            id = id,
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName,
            isActive = request.isActive
        )
    fun toResponse(user: User): UserResponse =
        UserResponse(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            isActive = user.isActive
        )
}