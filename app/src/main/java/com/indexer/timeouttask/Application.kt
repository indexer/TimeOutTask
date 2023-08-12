package com.indexer.timeouttask

import android.app.Application
import com.indexer.timeouttask.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class Application : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@Application)
      modules(appModule)
    }
  }
}
