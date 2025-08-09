package io.github.aughtone.chat.sample.data.di

import io.github.aughtone.chat.domain.ChannelService
import io.github.aughtone.chat.domain.MessageService
import io.github.aughtone.chat.domain.ParticipantService
import io.github.aughtone.chat.sample.data.ChannelServiceImpl
import io.github.aughtone.chat.sample.data.MessageServiceImpl
import io.github.aughtone.chat.sample.data.ParticipantServiceImpl
import io.github.aughtone.chat.sample.data.TimerServiceImpl
import io.github.aughtone.chat.sample.data.local.MessageLocalSource
import io.github.aughtone.chat.sample.data.remote.MessageRemoteSource
import io.github.aughtone.chat.sample.domain.TimerService
import org.koin.dsl.module

val dataModule = module {
    single { MessageRemoteSource() }
    single { MessageLocalSource() }

    single<ChannelService> { ChannelServiceImpl() }
    single<MessageService> { MessageServiceImpl(get(), get(), get()) }
    single<ParticipantService> { ParticipantServiceImpl() }
    single<TimerService> { TimerServiceImpl() }
}