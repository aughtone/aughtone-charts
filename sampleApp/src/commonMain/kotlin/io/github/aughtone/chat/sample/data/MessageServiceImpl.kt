package io.github.aughtone.chat.sample.data

import io.github.aughtone.chat.domain.Message
import io.github.aughtone.chat.domain.MessageService
import io.github.aughtone.chat.sample.data.local.MessageLocalSource
import io.github.aughtone.chat.sample.data.remote.MessageRemoteSource
import io.github.aughtone.chat.sample.data.utils.tryResult
import io.github.aughtone.chat.sample.domain.TimerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
class MessageServiceImpl(
    val remoteSource: MessageRemoteSource,
    val localSource: MessageLocalSource,
    val timerService: TimerService,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : MessageService {

    private var lastSync: Instant = timerService.now()

    init {
        scope.launch {
            timerService.flow(1.seconds).collect { now ->
                remoteSource.pollMessages(since = lastSync).onSuccess { messages ->
                    messages.forEach { message ->
                        localSource.save(message)
                    }
                }
                lastSync = now
            }
        }
    }

    override fun messages(
        channelId: String
    ): Flow<List<Message>> = localSource.messages.map { list ->
        list.filter { it.channelId == channelId }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun createMessage(message: Message): Result<String> = tryResult {
        val newId = Uuid.random().toString()
        remoteSource.sendMessage(message.copy(id = newId))
        newId
    }

    override suspend fun readMessage(messageId: String): Result<Message> = tryResult {
        localSource.get(messageId) ?: throw NoSuchElementException()
    }

    override suspend fun updateMessage(message: Message): Result<Unit> = tryResult {
        localSource.save(
            message.copy(modified = timerService.now().toEpochMilliseconds())
        )
    }

    override suspend fun deleteMessage(message: Message): Result<Boolean> = tryResult {
        localSource.delete(message.id)
        true
    }

    /**
     * TODO apply all filters
     */
    override suspend fun readMessages(
        participantId: String,
        limit: Int?,
        afterMessageId: String?
    ): Result<List<Message>> = tryResult {
        localSource.all().filter { it.senderId == participantId }
    }

    override suspend fun countAfterCheckpoint(
        channelId: String,
        timestamp: Long
    ): Result<Int> = tryResult {
        localSource.all().count { it.modified > timestamp }
    }
}
