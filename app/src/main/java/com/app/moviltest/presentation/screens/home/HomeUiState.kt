package com.app.moviltest.presentation.screens.home

import com.app.moviltest.domain.model.Sudoku

data class HomeUiState(
    val isLoading: Boolean = false,
    val sudoku: Sudoku? = null,
    val error: String? = null,
    val width: Int = 9,
    val difficulty: String = "easy"
)