package io.github.aughtone.chat.sample

import android.app.Application
import io.github.aughtone.chat.sample.data.di.dataModule
import io.github.aughtone.chat.sample.ui.di.uiModule
import org.koin.core.context.GlobalContext.startKoin

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(uiModule, dataModule)
        }
    }
}