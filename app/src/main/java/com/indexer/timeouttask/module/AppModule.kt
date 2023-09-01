package com.indexer.timeouttask.module

import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenUseCase
import com.indexer.timeouttask.screen.pomodoroscreen.viewmodel.PomodoroScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  single { PomodoroScreenUseCase() }
  viewModel { PomodoroScreenViewModel( androidContext(),get()) }
}
