package io.github.aughtone.chat.sample.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.aughtone.chat.component.LazyMessageColumn
import io.github.aughtone.chat.component.MessageItemParams
import io.github.aughtone.chat.sample.ui.details.component.DetailsTopBar
import io.github.aughtone.chat.sample.ui.details.component.MessageInput
import io.github.aughtone.chat.sample.ui.utils.SamplePreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DetailsContent(
        uiState = uiState,
        onBackClick = { navController.navigateUp() },
        onSendClick = { message, remote -> viewModel.onSendClick(message, remote) }
    )
}

@Composable
fun DetailsContent(
    uiState: DetailsUiState,
    onBackClick: () -> Unit,
    onSendClick: (message: String, remote: Boolean) -> Unit
) {
    Column {
        DetailsTopBar(
            title = uiState.channelName,
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBackClick
        )

        var lastVisibleMessage by remember { mutableStateOf<MessageItemParams?>(null) }

        LazyMessageColumn(
            messages = uiState.messages,
            modifier = Modifier.weight(1f),
            onLastVisibleMessageChanged = {
                it?.let { message ->
                    lastVisibleMessage = message
                }
            }
        )

        var remoteEmulated by remember { mutableStateOf(false) }
        var message by remember { mutableStateOf("") }
        MessageInput(
            value = message,
            onValueChange = { message = it },
            onSendClick = {
                onSendClick(message, remoteEmulated)
                remoteEmulated = !remoteEmulated
                message = ""
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Last message: ${
                    lastVisibleMessage?.messageElapsedTime ?: "Unknown"
                }",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Remote Emulated:")
                Checkbox(
                    checked = remoteEmulated,
                    onCheckedChange = { remoteEmulated = it })
            }
        }
    }
}

@Preview
@Composable
fun DetailsContentPreview() {
    SamplePreview {
        DetailsContent(
            uiState = DetailsUiState(
                channelName = "Channel 1",
                messages = listOf(
                    MessageItemParams(
                        id = "1",
                        senderName = "Participant 1",
                        senderPhotoUrl = "https://picsum.photos/200",
                        messageElapsedTime = "1 minute ago",
                        messageContent = "Message 1",
                        isLocal = true
                    ),
                ),
            ),
            onBackClick = {},
            onSendClick = { _, _ -> }
        )
    }
}
