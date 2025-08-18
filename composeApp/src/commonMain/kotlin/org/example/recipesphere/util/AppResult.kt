package org.example.recipesphere.util

sealed class AppResult<out T> {
    data class Ok<T>(val value: T) : AppResult<T>()
    data class Err(val error: Throwable) : AppResult<Nothing>()

    inline fun <R> map(transform: (T) -> R): AppResult<R> = when (this) {
        is Ok -> Ok(transform(value))
        is Err -> this
    }

    fun getOrNull(): T? = when (this) {
        is Ok -> value
        is Err -> null
    }
}
