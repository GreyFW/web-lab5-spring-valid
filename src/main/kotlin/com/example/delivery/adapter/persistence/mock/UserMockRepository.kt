package com.example.delivery.adapter.persistence.mock

import com.example.delivery.application.exception.AlreadyExistsException
import com.example.delivery.application.exception.NotFoundException
import com.example.delivery.domain.model.User
import com.example.delivery.domain.port.UserRepositoryPort
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("mock")
open class UserMockRepository : UserRepositoryPort {
    private val usersStorage = mutableMapOf<Long, User>()
    private var idCounter = 1L

    override fun create(user: User): User {
        val existingUser = usersStorage.values.find { it.email == user.email }
        if (existingUser != null) {
            throw AlreadyExistsException("User with email='${user.email}' already exists")
        }
        val id = idCounter++
        val newUser = user.copy(id = id)
        usersStorage[id] = newUser
        return newUser
    }

    override fun findById(id: Long): User? = usersStorage[id]

    override fun findAll(): List<User> = usersStorage.values.toList()

    override fun findByEmail(email: String): User? =
        usersStorage.values.find { it.email == email }

    override fun update(user: User): User {
        val id = user.id ?: throw NotFoundException("User id cannot be null")
        val existingUser = usersStorage[id] ?: throw NotFoundException("User with id=$id not found")
        val updatedUser = existingUser.copy(
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            isActive = user.isActive
        )
        usersStorage[id] = updatedUser
        return updatedUser
    }

    override fun delete(id: Long) {
        usersStorage[id] ?: throw NotFoundException("User with id=$id not found")
        usersStorage.remove(id)
    }
}