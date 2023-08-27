package com.indexer.timeouttask

import com.indexer.timeouttask.screen.mainscreen.PomodoroTask
import com.indexer.timeouttask.screen.pomodoroscreen.domain.AlarmTimerState
import com.indexer.timeouttask.screen.pomodoroscreen.domain.PomodoroScreenUseCase
import com.indexer.timeouttask.screen.pomodoroscreen.viewmodel.PomodoroScreenViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PomodoroScreenViewModelTest {

  lateinit var useCase : PomodoroScreenUseCase
  var viewModel : PomodoroScreenViewModel? = null


  @Before
  fun setUp(){
    useCase = PomodoroScreenUseCase()
    // Arrange
     viewModel = PomodoroScreenViewModel(useCase)
  }

  @Test
  fun `should swap pomodoro items when swap function is called`() {
    // Set up mock data
    val mockList = mutableListOf(
      PomodoroTask(title = "Task 2","", AlarmTimerState(), 0f),
      PomodoroTask(title = "Task 1",",", AlarmTimerState(), 0f)
    )
    viewModel?.pomodoroListScreenState?.value = mockList

    // Act
    val swapFunction = viewModel?.getSwapFunction()
    swapFunction?.invoke(0, 1)

    // Assert
    val swappedList = viewModel?.pomodoroListScreenState?.value
    assertEquals("Task 1", swappedList?.get(0)?.title ?:"" )
    assertEquals("Task 2", swappedList?.get(1)?.title ?:"" )
  }

  @Test
  fun `should return correct pomodoro description`() {
    // Act
    val desc1 = viewModel?.getPomodoroDescription(1)
    val desc2 = viewModel?.getPomodoroDescription(2)

    // Assert
    assertEquals("Total Time: 25 min", desc1)
    assertEquals("Total Time: 50 min", desc2)
  }

}