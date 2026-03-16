package com.example.delivery.application.service

import com.example.delivery.application.exception.AlreadyExistsException
import com.example.delivery.application.exception.NotFoundException
import com.example.delivery.domain.model.User
import com.example.delivery.domain.port.UserRepositoryPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
open class UserService(
    private val userRepository: UserRepositoryPort
) {
    private val logger = KotlinLogging.logger {}

    fun create(user: User): CreateUserResult {
        val existing = userRepository.findByEmail(user.email)

        return if (existing != null) {
            CreateUserResult(existing, false)
        } else {
            val created = userRepository.create(user)
            logger.info { "Создан пользователь id=${created.id}, email='${created.email}'" }
            CreateUserResult(created, true)
        }
    }

    fun getById(id: Long): User {
        logger.info { "Запрос пользователя id=$id" }
        return userRepository.findById(id) ?: throw NotFoundException("User with id=$id not found")
    }

    fun update(id: Long, updatedUser: User): User {
        val existingUser = userRepository.findById(id) ?: throw NotFoundException("User with id=$id not found")

        val userWithSameEmail = userRepository.findByEmail(updatedUser.email)
        if (userWithSameEmail != null && userWithSameEmail.id != id) {
            throw AlreadyExistsException("User with email='${updatedUser.email}' already exists")
        }

        val userToSave = existingUser.copy(
            email = updatedUser.email,
            firstName = updatedUser.firstName,
            lastName = updatedUser.lastName,
            isActive = updatedUser.isActive
        )
        val updated = userRepository.update(userToSave)
        logger.info { "Пользователь id=$id успешно обновлен" }
        return updated
    }

    fun delete(id: Long) {
        userRepository.findById(id) ?: throw NotFoundException("User with id=$id not found")
        userRepository.delete(id)
        logger.info { "Удален пользователь id=$id" }
    }

    fun getAll(): List<User> = userRepository.findAll()
}