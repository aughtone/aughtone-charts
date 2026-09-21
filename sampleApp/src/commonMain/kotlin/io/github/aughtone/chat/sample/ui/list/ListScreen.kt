package io.github.aughtone.chat.sample.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.aughtone.chat.component.ChannelColumn
import io.github.aughtone.chat.domain.Channel
import io.github.aughtone.chat.sample.ui.details.DetailsRoute
import io.github.aughtone.chat.sample.ui.utils.SamplePreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    ListContent(
        uiState = uiState,
        onItemClicked = { channel ->
            viewModel.onChannelClicked(channel)
            navController.navigate(DetailsRoute(channel.id))
        },
        onNewChatClicked = {
            viewModel.onNewChatClicked()
        }
    )
}

@Composable
private fun ListContent(
    uiState: ListUiState,
    onItemClicked: (channel: Channel) -> Unit,
    onNewChatClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ChannelColumn(
            items = uiState.channels,
            onItemClicked = onItemClicked,
            modifier = Modifier.fillMaxSize()
        )

        FloatingActionButton(
            onClick = { onNewChatClicked() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, "Add")
        }
    }
}

@Preview
@Composable
fun ListContentPreview() {
    SamplePreview {
        ListContent(
            uiState = ListUiState(
                channels = listOf(
                    Channel(
                        id = "1",
                        name = "Channel 1"
                    ),
                    Channel(
                        id = "2",
                        name = "Channel 2"
                    )
                )
            ),
            onItemClicked = {},
            onNewChatClicked = {}
        )
    }
}