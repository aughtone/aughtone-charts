package io.github.aughtone.chat.sample.data.utils

internal suspend fun <T> tryResult(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}