package com.example.games_scoring_app

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Game : Screen("Game/{gameId}/{new}/{gameTypeId}"){
        fun createRoute(gameId: Int, new: Boolean , gameTypeId: Int) = "Game/$gameId/$new/$gameTypeId"
    }
    object SetUp : Screen("Game/{gameType}"){
        fun createRoute(gameType: Int?) = "Game/$gameType"
    }
    object SavedGames : Screen("SavedGames")
    object RollDice : Screen("RollDice")
}