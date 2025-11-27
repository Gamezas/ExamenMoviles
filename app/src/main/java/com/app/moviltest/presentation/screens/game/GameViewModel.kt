package com.app.moviltest.presentation.screens.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.moviltest.domain.model.Sudoku
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    fun initializeSudoku(sudoku: Sudoku) {
        _uiState.update {
            it.copy(
                sudoku = sudoku,
                currentBoard = sudoku.puzzle.map { row -> row.toMutableList() }
            )
        }
    }

    fun selectCell(row: Int, col: Int) {
        _uiState.update {
            it.copy(selectedCell = Pair(row, col))
        }
    }

    fun updateCellValue(value: Int?) {
        val selectedCell = _uiState.value.selectedCell ?: return
        val sudoku = _uiState.value.sudoku ?: return
        val (row, col) = selectedCell

        if (sudoku.puzzle[row][col] == null) {
            val newBoard = _uiState.value.currentBoard.map { it.toMutableList() }
            newBoard[row][col] = value

            _uiState.update {
                it.copy(currentBoard = newBoard)
            }
        }
    }

    fun checkSolution() {
        val sudoku = _uiState.value.sudoku ?: return
        val currentBoard = _uiState.value.currentBoard
        val errors = mutableSetOf<Pair<Int, Int>>()

        for (row in currentBoard.indices) {
            for (col in currentBoard[row].indices) {
                val currentValue = currentBoard[row][col]
                val solutionValue = sudoku.solution[row][col]

                if (currentValue != null && currentValue != solutionValue) {
                    errors.add(Pair(row, col))
                }
            }
        }

        val isCompleted = errors.isEmpty() && currentBoard.all { row ->
            row.all { it != null }
        }

        _uiState.update {
            it.copy(
                errorCells = errors,
                isCompleted = isCompleted,
                showCompletedDialog = isCompleted
            )
        }
    }

    fun dismissCompletedDialog() {
        _uiState.update {
            it.copy(showCompletedDialog = false)
        }
    }

    fun clearErrors() {
        _uiState.update {
            it.copy(errorCells = emptySet())
        }
    }
}