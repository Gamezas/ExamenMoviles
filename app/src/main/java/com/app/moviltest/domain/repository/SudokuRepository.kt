package com.app.moviltest.domain.repository

import com.app.moviltest.domain.model.Sudoku

interface SudokuRepository {
    suspend fun getSudoku(
        apiKey: String,
        width: Int? = null,
        height: Int? = null,
        difficulty: String? = null,
    ): Sudoku
}
