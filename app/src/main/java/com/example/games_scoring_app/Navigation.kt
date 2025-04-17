package com.example.games_scoring_app

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Game : Screen("Game/{game_id}"){
        fun createRoute(game_id: Int?) = "Game/$game_id"
    }
    object SetUp : Screen("Game/{game_type}"){
        fun createRoute(game_type: Int?) = "Game/$game_type"
    }
    object SavedGames : Screen("SavedGames")
}