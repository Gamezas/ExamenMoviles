package com.app.moviltest.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.moviltest.domain.model.Sudoku

@Composable
fun GameScreen(
    sudoku: Sudoku,
    onBack: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(sudoku) {
        viewModel.initializeSudoku(sudoku)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sudoku",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (state.currentBoard.isNotEmpty()) {
            SudokuGrid(
                currentBoard = state.currentBoard,
                originalBoard = sudoku.puzzle,
                selectedCell = state.selectedCell,
                errorCells = state.errorCells,
                onCellClick = { row, col ->
                    viewModel.selectCell(row, col)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            NumberPad(
                boardSize = state.boardSize,
                onNumberClick = { number ->
                    viewModel.updateCellValue(number)
                },
                onClearClick = {
                    viewModel.updateCellValue(null)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.checkSolution()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Verificar Solución",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = {
                    viewModel.resetBoard()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                    )
            ) {
                    Text(
                        text = "Reiniciar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = "Volver al Inicio",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    if (state.showCompletedDialog) {
        CompletedDialog(
            onDismiss = {
                viewModel.dismissCompletedDialog()
                onBack()
            }
        )
    }
}

@Composable
fun SudokuGrid(
    currentBoard: List<List<Int?>>,
    originalBoard: List<List<Int?>>,
    selectedCell: Pair<Int, Int>?,
    errorCells: Set<Pair<Int, Int>>,
    onCellClick: (Int, Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White)
        ) {
            currentBoard.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    row.forEachIndexed { colIndex, value ->
                        SudokuCell(
                            value = value,
                            isOriginal = originalBoard[rowIndex][colIndex] != null,
                            isSelected = selectedCell == Pair(rowIndex, colIndex),
                            isError = errorCells.contains(Pair(rowIndex, colIndex)),
                            onClick = { onCellClick(rowIndex, colIndex) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuCell(
    value: Int?,
    isOriginal: Boolean,
    isSelected: Boolean,
    isError: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
            .background(
                when {
                    isError -> Color.Red.copy(alpha = 0.3f)
                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    isOriginal -> Color.LightGray.copy(alpha = 0.3f)
                    else -> Color.White
                }
            )
            .clickable(enabled = !isOriginal) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        value?.let {
            Text(
                text = it.toString(),
                fontSize = 18.sp,
                fontWeight = if (isOriginal) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isError -> Color.Red
                    isOriginal -> Color.Black
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}

@Composable
fun NumberPad(
    onNumberClick: (Int) -> Unit,
    onClearClick: () -> Unit,
    boardSize: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 1..5) {
                NumberButton(number = i, onClick = { onNumberClick(i) })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 6..9) {
                NumberButton(number = i, onClick = { onNumberClick(i) })
            }
            ClearButton(onClick = onClearClick)
        }
    }
}

@Composable
fun NumberButton(
    number: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(60.dp)
            .height(60.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = number.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ClearButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(60.dp)
            .height(60.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text(
            text = "X",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CompletedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "¡Felicidades!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "¡Completado!\n\nHas resuelto el Sudoku correctamente.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}