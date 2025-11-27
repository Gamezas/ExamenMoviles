package com.app.moviltest.data.repository

import com.app.moviltest.data.mapper.toDomain
import com.app.moviltest.data.remote.api.SudokuApi
import com.app.moviltest.domain.model.Sudoku
import com.app.moviltest.domain.repository.SudokuRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepositoryImpl
@Inject
constructor(
    private val api: SudokuApi
) : SudokuRepository {

    override suspend fun getSudoku(
        width: Int?,
        height: Int?,
        difficulty: String?
    ): Sudoku {

        val response = api.getSudoku(
            apiKey = "ozOP7IOzi505n7W1zz1Lfw==Gk7zLTIXJ0kFG4nv",
            width = width,
            height = height,
            difficulty = difficulty,
        )

        return response.toDomain()
    }
}
