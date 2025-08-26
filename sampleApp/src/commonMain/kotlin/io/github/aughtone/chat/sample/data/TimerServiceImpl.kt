package io.github.aughtone.chat.sample.data

import io.github.aughtone.chat.sample.domain.TimerService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

class TimerServiceImpl : TimerService {
    override fun flow(interval: Duration): Flow<Instant> = kotlinx.coroutines.flow.flow {
        while (true) {
            emit(Clock.System.now())
            delay(interval)
        }
    }

    override fun now(): Instant = Clock.System.now()
}