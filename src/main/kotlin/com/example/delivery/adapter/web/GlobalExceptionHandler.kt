package com.example.delivery.adapter.web

import com.example.delivery.adapter.web.dto.ErrorResponse
import com.example.delivery.adapter.web.dto.ValidationErrorResponse
import com.example.delivery.application.exception.AppException
import com.example.delivery.application.exception.InvalidOrderStateException
import com.example.delivery.application.exception.NotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import com.example.delivery.application.exception.AlreadyExistsException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.dao.DataIntegrityViolationException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AppException::class)
    fun handleAppException(e: AppException): ResponseEntity<ErrorResponse> {
        val status = when (e) {
            is NotFoundException -> HttpStatus.NOT_FOUND
            is AlreadyExistsException -> HttpStatus.CONFLICT
            is InvalidOrderStateException -> HttpStatus.BAD_REQUEST
        }
        logger.warn { "${e::class.simpleName}: ${e.message}" }
        return ResponseEntity
            .status(status)
            .body(ErrorResponse(status.value(), e.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Некорректное значение")
        }
        logger.warn { "Ошибка валидации: $errors" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ValidationErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    message = "Ошибка валидации",
                    errors = errors
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e) { "Непредвиденная ошибка" }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(500, "Внутренняя ошибка сервера"))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        logger.warn { "Нечитаемое тело запроса: ${ex.message}" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(400, "Некорректное тело запроса"))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        logger.warn { "Нарушение уникальности: ${ex.message}" }
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(409, "Ресурс с такими данными уже существует"))
    }

}