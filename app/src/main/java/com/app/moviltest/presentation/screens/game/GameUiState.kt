package com.app.moviltest.presentation.screens.game

import com.app.moviltest.domain.model.Sudoku

data class GameUiState(
    val sudoku: Sudoku? = null,
    val currentBoard: List<List<Int?>> = emptyList(),
    val selectedCell: Pair<Int, Int>? = null,
    val errorCells: Set<Pair<Int, Int>> = emptySet(),
    val isCompleted: Boolean = false,
    val showCompletedDialog: Boolean = false
)