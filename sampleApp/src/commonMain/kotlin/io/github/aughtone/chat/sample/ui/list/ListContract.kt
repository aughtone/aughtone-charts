package io.github.aughtone.chat.sample.ui.list

import io.github.aughtone.chat.domain.Channel
import kotlinx.serialization.Serializable

@Serializable
data object ListRoute

data class ListUiState(
    val channels: List<Channel> = emptyList()
)
