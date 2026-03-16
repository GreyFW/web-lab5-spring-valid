package com.example.delivery.adapter.persistence.jpa

import com.example.delivery.adapter.persistence.jpa.entity.UserEntity
import com.example.delivery.adapter.persistence.jpa.repository.UserJpaRepository
import com.example.delivery.domain.model.User
import com.example.delivery.domain.port.UserRepositoryPort
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("jpa")
open class UserJpaAdapter(
    private val userJpaRepository: UserJpaRepository
) : UserRepositoryPort {
    override fun create(user: User): User = userJpaRepository.save(UserEntity.fromDomain(user)).toDomain()

    override fun findById(id: Long): User? = userJpaRepository.findById(id).orElse(null)?.toDomain()
    override fun findAll(): List<User> = userJpaRepository.findAll().map { it.toDomain() }
    override fun findByEmail(email: String): User? = userJpaRepository.findByEmail(email)?.toDomain()

    override fun update(user: User): User = userJpaRepository.save(UserEntity.fromDomain(user)).toDomain()

    override fun delete(id: Long) = userJpaRepository.deleteById(id)
}