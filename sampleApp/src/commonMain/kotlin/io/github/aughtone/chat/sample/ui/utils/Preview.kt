package io.github.aughtone.chat.sample.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
fun SamplePreview(
    content: @Composable () -> Unit
) {
    MaterialTheme {
        Surface { content() }
    }
}