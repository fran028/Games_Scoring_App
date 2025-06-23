package com.example.games_scoring_app

import android.net.Uri

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Game : Screen("Game/{gameTypeId}/{playerNames}"){
        fun createRoute(
            gameTypeId: Int,
            playerNames: Array<String> // Add playerNames parameter
        ): String {
            val playerNamesString = playerNames.joinToString(separator = "‚‗‚") // Using a less common separator
            val encodedPlayerNames = Uri.encode(playerNamesString)
            return "Game/$gameTypeId/$encodedPlayerNames"
        }
        //fun createRoute(gameTypeId: Int, playerNames: Array<String> ) = "Game/$gameTypeId/$playerNames"
    }
    object SetUp : Screen("Game/{gameType}"){
        fun createRoute(gameType: Int?) = "Game/$gameType"
    }
    object SavedGames : Screen("SavedGames")
    object RollDice : Screen("RollDice")
}