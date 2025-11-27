package com.app.moviltest.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.moviltest.domain.usecase.SudokuUseCase
import com.app.moviltest.presentation.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sudokuUseCase: SudokuUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun updateWidth(width: Int) {
        _uiState.update { it.copy(width = width) }
    }

    fun updateDifficulty(difficulty: String) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun generateSudoku() {
        viewModelScope.launch {
            sudokuUseCase(
                width = _uiState.value.width,
                height = _uiState.value.width,
                difficulty = _uiState.value.difficulty
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                sudoku = result.data,
                                error = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Error desconocido"
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearSudoku() {
        _uiState.update { it.copy(sudoku = null) }
    }
}