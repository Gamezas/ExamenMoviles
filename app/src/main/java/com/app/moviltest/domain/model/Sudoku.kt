package com.app.moviltest.domain.model

data class Sudoku (
    val puzzle: List<List<Int?>>,
    val solution: List<List<Int?>>
)