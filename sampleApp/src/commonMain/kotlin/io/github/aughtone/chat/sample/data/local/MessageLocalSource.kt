package io.github.aughtone.chat.sample.data.local

import io.github.aughtone.chat.domain.Message
import io.github.aughtone.chat.sample.data.utils.ListMemCache
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi

class MessageLocalSource {
    private val memCache = ListMemCache<String, Message>()

    val messages: Flow<List<Message>> = memCache.flow()

    @OptIn(ExperimentalUuidApi::class)
    suspend fun save(message: Message): String {
        memCache.write(message.id, message)
        return message.id
    }

    suspend fun get(id: String): Message? = memCache.read(id)

    suspend fun delete(id: String) {
        memCache.delete(id)
    }

    suspend fun all() = memCache.readAll()
}