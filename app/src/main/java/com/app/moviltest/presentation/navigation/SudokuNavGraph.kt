package com.app.moviltest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.moviltest.domain.model.Sudoku
import com.app.moviltest.presentation.screens.game.GameScreen
import com.app.moviltest.presentation.screens.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Game : Screen("game")
}

@Composable
fun SudokuNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    var currentSudoku by remember { mutableStateOf<Sudoku?>(null) }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToGame = { sudoku ->
                    currentSudoku = sudoku
                    navController.navigate(Screen.Game.route)
                }
            )
        }

        composable(Screen.Game.route) {
            currentSudoku?.let { sudoku ->
                GameScreen(
                    sudoku = sudoku,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}