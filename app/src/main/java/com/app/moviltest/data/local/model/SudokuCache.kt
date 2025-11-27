package com.app.moviltest.data.local.model

data class SudokuCache(
    val puzzle: String,
    val solution: String,
    val currentBoard: String,
    val timestamp: Long
)