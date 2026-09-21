package io.github.aughtone.chat.sample.data

import io.github.aughtone.chat.domain.Participant
import io.github.aughtone.chat.domain.ParticipantService
import io.github.aughtone.chat.sample.data.utils.ListMemCache
import io.github.aughtone.chat.sample.data.utils.tryResult

class ParticipantServiceImpl : ParticipantService {
    private val memCache = ListMemCache<String, Participant>()

    override suspend fun createParticipant(
        participant: Participant
    ): Result<Participant> = tryResult {
        memCache.write(participant.id, participant)
        participant
    }

    override suspend fun readParticipant(id: String): Result<Participant> = tryResult {
        memCache.read(id) ?: throw NoSuchElementException()
    }

    override suspend fun updateParticipant(
        participant: Participant
    ): Result<Participant> = tryResult {
        memCache.write(participant.id, participant)
        participant
    }

    override suspend fun deleteParticipant(id: String): Result<Boolean> = tryResult {
        memCache.delete(id)
        true
    }

    override suspend fun readParticipants(
        ids: List<String>
    ): Result<List<Participant>> = tryResult {
        memCache.readAll().filter { ids.contains(it.id) }
    }
}