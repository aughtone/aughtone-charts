package io.github.aughtone.chat.sample.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aughtone.chat.domain.Channel
import io.github.aughtone.chat.domain.ChannelService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ListViewModel(
    private val channelService: ChannelService
) : ViewModel() {
    val uiState = MutableStateFlow(ListUiState())

    init {
        viewModelScope.launch {
            channelService.channels.collect {
                uiState.update { state -> state.copy(channels = it) }
            }
        }
    }

    fun onChannelClicked(channel: Channel) {
        //TODO log click
    }

    fun onNewChatClicked() {
        viewModelScope.launch {
            channelService.createChannel(Channel.new())
        }
    }
}

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
private fun Channel.Companion.new() = Channel(
    id = Uuid.random().toString(),
    name = "New chat"
)