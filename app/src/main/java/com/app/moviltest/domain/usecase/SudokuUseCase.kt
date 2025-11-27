package com.app.moviltest.domain.usecase

import com.app.moviltest.presentation.common.Result
import com.app.moviltest.domain.model.Sudoku
import com.app.moviltest.domain.repository.SudokuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SudokuUseCase
@Inject
constructor(
    private val repository: SudokuRepository
) {

    operator fun invoke(
        width: Int,
        height: Int,
        difficulty: String
    ): Flow<Result<Sudoku>> = flow {

        emit(Result.Loading)

        try {
            val sudoku = repository.getSudoku(
                width = width,
                height = height,
                difficulty = difficulty,
            )

            emit(Result.Success(sudoku))

        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
