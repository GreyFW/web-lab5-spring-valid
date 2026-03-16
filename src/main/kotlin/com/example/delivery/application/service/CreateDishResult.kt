package com.example.delivery.application.service

import com.example.delivery.domain.model.Dish

data class CreateDishResult(
    val dish: Dish,
    val isCreated: Boolean
)