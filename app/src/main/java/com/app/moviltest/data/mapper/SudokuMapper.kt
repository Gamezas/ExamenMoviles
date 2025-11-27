package com.app.moviltest.data.mapper

import com.app.moviltest.data.remote.dto.SudokuDto
import com.app.moviltest.domain.model.Sudoku

fun SudokuDto.toDomain(): Sudoku =
    Sudoku(
        puzzle = puzzle,
        solution = solution
    )