package com.indexer.timeouttask.screen.pomodoroscreen.domain

import android.util.Log
import com.indexer.timeouttask.screen.mainscreen.PomodoroTask

class PomodoroScreenUseCase {
  fun updatePomodoroNumber(
    state: PomodoroScreenState,
    value: Int
  ): PomodoroScreenState {
    return state.copy(pomodoroDurationInMinutes = value)
  }

  fun calculateProgress(
    remainingTime: Long,
    initialTime: Long
  ): Float {
    val progress = ((initialTime - remainingTime) / initialTime.toFloat()) * 100
    return progress.coerceIn(0f, 100f)
  }

  fun dismissCompletedTask(
    taskList: List<PomodoroTask>, completedTask: Int): List<PomodoroTask> {
    val updatedTaskList = taskList.toMutableList()
    updatedTaskList[completedTask].progress = 100f
    updatedTaskList.add(updatedTaskList[completedTask])
    updatedTaskList.remove(updatedTaskList[completedTask])
    return updatedTaskList
  }

  fun findIndexOfNextIncompleteTask(
    taskList: List<PomodoroTask>,
    startIndex: Int
  ): Int {
    for (index in startIndex until taskList.size) {
      if (taskList[index].progress != 100f) {
        return index
      }
    }
    return -1
  }

  fun updatePomodoroTitle(
    state: PomodoroScreenState,
    value: String
  ): PomodoroScreenState {
    return state.copy(pomodoroTitle = value)
  }

  fun increasePomodoroNumber(
    state: PomodoroScreenState,
    value: Int
  ): PomodoroScreenState {
    return state.copy(pomodoroDurationInMinutes = value.plus(1))
  }

  fun decreasePomodoroNumber(
    state: PomodoroScreenState,
    value: Int
  ): PomodoroScreenState {
    return if (value > 1) {
      state.copy(pomodoroDurationInMinutes = value.minus(1))
    } else {
      state.copy(pomodoroDurationInMinutes = 1)
    }
  }
}