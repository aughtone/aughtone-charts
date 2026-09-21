package io.github.aughtone.chat.sample.data.remote

import io.github.aughtone.chat.domain.Message
import io.github.aughtone.chat.sample.data.utils.ListMemCache
import io.github.aughtone.chat.sample.data.utils.tryResult
import kotlinx.datetime.Instant

class MessageRemoteSource(

) {
    //TODO using memory cache instead of remote API
    private val memCache = ListMemCache<String, Message>()

    suspend fun sendMessage(message: Message): Result<String> = tryResult {
        memCache.write(message.id, message)
        message.id
    }

    suspend fun pollMessages(
        since: Instant? = null
    ): Result<List<Message>> = tryResult {
        memCache.readAll().filter { it.created > (since?.toEpochMilliseconds() ?: 0) }
    }
}
