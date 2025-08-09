package io.github.aughtone.chat.sample.ui.di

import io.github.aughtone.chat.sample.ui.details.DetailsViewModel
import io.github.aughtone.chat.sample.ui.list.ListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ListViewModel(get()) }
    viewModel { DetailsViewModel(get(), get(), get(), get()) }
}
