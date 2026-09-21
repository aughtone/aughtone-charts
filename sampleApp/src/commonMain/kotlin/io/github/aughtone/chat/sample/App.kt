package io.github.aughtone.chat.sample

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import io.github.aughtone.chat.sample.ui.RootUi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        RootUi()
    }
}
