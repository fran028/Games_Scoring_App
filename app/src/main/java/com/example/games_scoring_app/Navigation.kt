package com.example.games_scoring_app

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Game : Screen("Game/{game_id}/{new}"){
        fun createRoute(game_id: Int?, new: Boolean = true) = "Game/$game_id/$new"
    }
    object SetUp : Screen("Game/{game_type}"){
        fun createRoute(game_type: Int?) = "Game/$game_type"
    }
    object SavedGames : Screen("SavedGames")
}