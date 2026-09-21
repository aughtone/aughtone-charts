package io.github.aughtone.chat.sample.data

import io.github.aughtone.chat.domain.Channel
import io.github.aughtone.chat.domain.ChannelService
import io.github.aughtone.chat.sample.data.utils.ListMemCache
import io.github.aughtone.chat.sample.data.utils.tryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalTime::class)
class ChannelServiceImpl : ChannelService {
    private val memCache = ListMemCache<String, Channel>()

    override val channels: Flow<List<Channel>> = memCache.flow()

    override fun channels(
        participantId: String
    ): Flow<List<Channel>> = memCache.flow().map { list ->
        list.filter { it.participantIds.contains(participantId) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createChannel(channel: Channel): Result<String> = tryResult {
        val newId = Uuid.random().toString()
        memCache.write(newId, channel.copy(id = newId))
        channel.id
    }

    override suspend fun readChannel(id: String): Result<Channel> = tryResult {
        memCache.read(id) ?: throw NoSuchElementException()
    }

    override suspend fun updateChannel(channel: Channel): Result<Unit> = tryResult {
        memCache.write(
            channel.id,
            channel.copy(modified = Clock.System.now().toEpochMilliseconds())
        )
    }

    override suspend fun deleteChannel(id: String): Result<Boolean> = tryResult {
        memCache.delete(id)
        true
    }

    override suspend fun readChannels(ids: List<String>): Result<List<Channel>> = tryResult {
        memCache.readAll().filter { ids.contains(it.id) }
    }

    override suspend fun markCheckpoint(
        channelId: String,
        participantId: String,
        timestamp: Long
    ): Result<Boolean> = tryResult {
        val channel = memCache.read(channelId) ?: throw NoSuchElementException()
        memCache.write(
            id = channel.id, item = channel.copy(
                modified = Clock.System.now().toEpochMilliseconds(),
                checkpoints = channel.checkpoints.toMutableMap()
                    .apply { set(participantId, timestamp) }.toMap()
            )
        )
        true
    }

    override suspend fun readCheckpoint(
        channelId: String,
        participantId: String
    ): Result<Long> = tryResult {
        memCache.read(channelId)?.checkpoints?.get(participantId) ?: 0
    }
}
