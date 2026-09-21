package io.github.aughtone.chat.sample.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.aughtone.chat.sample.ui.details.DetailsRoute
import io.github.aughtone.chat.sample.ui.details.DetailsScreen
import io.github.aughtone.chat.sample.ui.list.ListRoute
import io.github.aughtone.chat.sample.ui.list.ListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun RootUi() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ListRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ListRoute> { ListScreen(navController) }
            composable<DetailsRoute> { DetailsScreen(navController) }
        }
    }
}