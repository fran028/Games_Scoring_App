package com.example.games_scoring_app

import android.net.Uri
import androidx.compose.ui.graphics.Color

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
    object SetUp : Screen("SetUp/{gameType}/{gameColor}"){
        fun createRoute(gameType: Int?, gameColor: Color) = "SetUp/$gameType/${gameColor.value.toString(16)}"
    }
    object SavedGames : Screen("SavedGames")
    object RollDice : Screen("RollDice")
    object Settings : Screen("Settings")

}