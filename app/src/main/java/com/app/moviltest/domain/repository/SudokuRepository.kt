package com.app.moviltest.domain.repository

import com.app.moviltest.domain.model.Sudoku

interface SudokuRepository {
    suspend fun getSudoku(
        width: Int? = null,
        height: Int? = null,
        difficulty: String? = null,
    ): Sudoku
}
