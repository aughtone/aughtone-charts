package io.github.aughtone.chat.sample.ui.details

import io.github.aughtone.chat.component.MessageItemParams
import io.github.aughtone.chat.domain.Channel
import io.github.aughtone.chat.domain.Message
import io.github.aughtone.chat.domain.Participant
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class DetailsRoute(
    val channelId: String
)

data class DetailsUiState(
    val channelName: String,
    val messages: List<MessageItemParams>,
)

data class DetailsModelState(
    val messages: List<Message> = emptyList(),
    val channel: Channel? = null,
    val localUser: Participant = Participant(
        id = "me",
        name = "Bob MacKenzie",
        photoUrl = "https://i.pravatar.cc/150?u=me"
    ),
    val now: Instant? = null,
    val participants: List<Participant> = listOf(
        localUser,
        Participant(
            id = "other",
            name = "Doug MacKenzie",
            photoUrl = "https://i.pravatar.cc/150?u=Remote"
        )
    )
)
