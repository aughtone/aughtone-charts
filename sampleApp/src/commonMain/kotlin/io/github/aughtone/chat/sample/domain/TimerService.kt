package io.github.aughtone.chat.sample.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.time.Duration

interface TimerService {
    fun flow(interval: Duration): Flow<Instant>

    fun now(): Instant
}