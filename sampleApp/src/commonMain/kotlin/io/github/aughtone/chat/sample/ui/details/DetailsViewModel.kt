package io.github.aughtone.chat.sample.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.aughtone.chat.component.MessageItemParams
import io.github.aughtone.chat.domain.ChannelService
import io.github.aughtone.chat.domain.Message
import io.github.aughtone.chat.domain.MessageService
import io.github.aughtone.chat.domain.Participant
import io.github.aughtone.chat.sample.domain.TimerService
import io.github.aughtone.datetime.format.DateTimeStyle
import io.github.aughtone.datetime.format.RelativeTime
import io.github.aughtone.datetime.format.formatRelative
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalTime::class)
class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val channelService: ChannelService,
    private val messageService: MessageService,
    private val timerService: TimerService
) : ViewModel() {
    private val detailsRoute: DetailsRoute = savedStateHandle.toRoute()

    val modelState = MutableStateFlow(DetailsModelState())
    val uiState = modelState.map {
        it.asUiState()
    }.stateIn(
        viewModelScope, SharingStarted.Eagerly, DetailsModelState().asUiState()
    )

    init {
        viewModelScope.launch {
            channelService.readChannel(detailsRoute.channelId).onSuccess {
                modelState.update { state ->
                    state.copy(channel = it)
                }
            }
        }
        viewModelScope.launch {
            messageService.messages(detailsRoute.channelId).collect { messages ->
                modelState.update { state ->
                    state.copy(messages = messages)
                }
            }
        }
        viewModelScope.launch {
            timerService.flow(1.seconds).collect {
                modelState.update { state ->
                    state.copy(now = it)
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onSendClick(
        message: String,
        remote: Boolean,
    ) {
        viewModelScope.launch {
            modelState.value.channel?.let {
                messageService.createMessage(
                    Message(
                        senderId = if (remote) "other" else "me",
                        channelId = it.id,
                        content = message,
                        created = timerService.now().toEpochMilliseconds()
                    )
                )
            }
        }
    }
}

private fun DetailsModelState.asUiState() = DetailsUiState(
    channelName = channel?.name ?: "",
    messages = messages.map { message ->
        message.asUiState(
            sender = participants.first { message.senderId == it.id },
            isLocal = message.senderId == localUser.id
        )
    }
)

private fun Message.asUiState(
    sender: Participant,
    isLocal: Boolean
) = MessageItemParams(
    id = id,
    senderName = sender.name,
    senderPhotoUrl = sender.photoUrl,
    messageElapsedTime = Instant.fromEpochMilliseconds(created)
        .formatRelative(
            until = 1.days,
            dateStyle = DateTimeStyle.Medium,
            timeStyle = DateTimeStyle.Short,
            relativeTime = RelativeTime.Past
        ),
    messageContent = content,
    isLocal = isLocal
)
